package models;

import app.Server;
import utilities.ScoreHandler;

import java.io.IOException;
import java.util.*;

public class GameEngine {

    public GameEngine() {
        p1 = new Player();
        p2 = new Player();
        playing = true;
        deck = new Deck();
        turn = 1;
        attacks = new Attack();
        scoreHandler = new ScoreHandler();
        try {
            server = Server.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Player p1, p2;
    private Player currentPlayer;
    private Player opponentPlayer;
    private Deck deck;
    private boolean playing;
    private int turn;
    private Attack attacks;
    private ScoreHandler scoreHandler;
    private boolean consoleGame = true;
    private Server server;

    public void setP1(Player p) {
        this.p1 = p;
    }

    public void setP2(Player p) {
        this.p2 = p;
    }

    public int getRound() {
        return (turn + 1) / 2;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public void startGame(String startArgs) throws IOException {
        System.out.println("starting game");
        if (startArgs.equals("fx")) {
            consoleGame = false;
        }
        initGame();
        if (startArgs.equals("console")) {
            while (playing) {
                playerMenu();
            }

            //TODO: Some endscreen in here!
        }
    }


    public void initGame() throws IOException {
        deck.createFullDeck();
        initPlayer();
    }


    /**
     * initializes the players by
     * setting the players decks and cards in hands
     */
    public void initPlayer() {
        p1.setPlayer(1);
        p2.setPlayer(2);
        determinePlayerToStart();
        ArrayList<Card> playerOneDeck, playerTwoDeck;
        deck.playerDeck();
        playerOneDeck = deck.getPlayerOneDeck();
        playerTwoDeck = deck.getPlayerTwoDeck();
        p1.setCurrentDeck(playerOneDeck);
        p2.setCurrentDeck(playerTwoDeck);
        for (int i = 0; i < 5; i++) {
            p1.pickupCard();
            p2.pickupCard();
        }

    }

    public void checkCardsLeft() throws IOException {
        checkPlayerCards(p1, p2);
        checkPlayerCards(p2, p1);
    }

    public void checkPlayerCards(Player p, Player q) throws IOException {
        if (p.getCurrentDeck().size() == 0 && p.getPlayerHand().size() == 0 && p.getTableCards().size() == 0) {
            System.out.println("Congratulations!" + q.getName() + " is the Winner");
            scoreHandler.checkScore(q);
            playing = false;
            if (!consoleGame) {
                Server.getInstance().msgToFX("gameover");
            }

        }
    }

    public void checkHealthLeft() throws IOException {
        checkPlayerHealth(p1, p2);
        checkPlayerHealth(p2, p1);
    }

    public void checkPlayerHealth(Player p, Player q) throws IOException {
        if (p.getHealth() <= 0) {
            System.out.println("Congratulations! " + q.getName() + " is the Winner");
            scoreHandler.checkScore(q);
            playing = false;
            if (!consoleGame) {
                Server.getInstance().msgToFX("gameover");
            }
        }
    }

    /**
     * Determines if p1 or p2 is going to start
     */
    public void determinePlayerToStart() {
        Random rand = makeRandom();
        boolean random = rand.nextBoolean();
        getPlayerToStart(random);
    }

    public void getPlayerToStart(boolean random) {
        int active;
        if (random) {
            active = 1;
            currentPlayer = p1;
            opponentPlayer = p2;
        } else {
            active = 2;
            currentPlayer = p2;
            opponentPlayer = p1;
        }
        if (!consoleGame)
            server.msgToFX("player" + Integer.toString(active));
    }

    public Random makeRandom() {
        return new Random();
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void endTurn() throws IOException {
        checkCardsLeft();
        unTap();
        int active;
        if (currentPlayer == p1) {
            active = 2;
            currentPlayer = p2;
            opponentPlayer = p1;
        } else {
            active = 1;
            currentPlayer = p1;
            opponentPlayer = p2;
        }
        currentPlayer.pickupCard();
        turn++;
        increaseIgnCounter(currentPlayer.getTableCards());
        increaseIgnCounter(opponentPlayer.getTableCards());
        if (!consoleGame)
            server.msgToFX("player" + Integer.toString(active));
    }

    public void increaseIgnCounter(ArrayList<Card> playerTableCards) {
        //increase ignited card counter for new round and take damage automatically
        for (Card card : playerTableCards) {
            if (card instanceof CreatureCard) {
                if (((CreatureCard) card).getIgnRoundCounter() >= 1 && ((CreatureCard) card).getIgnRoundCounter() < 3) {
                    ((CreatureCard) card).increaseIgnRoundCounter();
                    ((CreatureCard) card).removeHp(2);

                } else if (((CreatureCard) card).getIgnRoundCounter() == 3) {
                    ((CreatureCard) card).setIgniteCounter(0);
                }
            }
        }
    }

    /**
     * checks if the card hp is 0
     *
     * @param creatureCard
     * @return true if hp is 0
     */
    public boolean isCardKilled(CreatureCard creatureCard) {

        if (creatureCard.getHp() <= 0)
            return true;
        return false;
    }

    public enum AttackNames {BASIC, PLAYERATTACK, DUALATTACK, IGNITE, ATTACKALL}

    public void attack(Card selectedCard, List<CreatureCard> opponentCards) throws IOException {
        //TODO: change to string instead of ENUM . works with string to since java 8
        boolean notTapped = true;

        if (getRound() > 1) {
            if (selectedCard instanceof CreatureCard) {
                notTapped = !checkIfTapped((CreatureCard) selectedCard);
            }
            if (notTapped) {
                chooseAttack(selectedCard, opponentCards);
                for (int i = 0; i < opponentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
                    if (isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(i))) {
                        opponentPlayer.sendToGraveyard(opponentPlayer.getTableCards().get(i));
                    }
                }
                if (selectedCard instanceof MagicCard) {
                    currentPlayer.sendToGraveyard(selectedCard);
                }
                if (selectedCard.getClass() == CreatureCard.class) {
                    ((CreatureCard) selectedCard).tap();
                    if (isCardKilled((CreatureCard) selectedCard)) {
                        currentPlayer.sendToGraveyard(selectedCard);
                    }
                }
                checkHealthLeft();
            } else {
                Server.getInstance().msgToFX("tapped");
            }
        } else {
            Server.getInstance().msgToFX("tosoon");
        }
    }

    public void chooseAttack(Card selectedCard, List<CreatureCard> opponentCards) {
        String nameOfAttack = selectedCard.getSpecialAttack().toUpperCase();
        for (AttackNames attackName : AttackNames.values()) {
            if (attackName.name().equals(nameOfAttack)) {
                switch (attackName) {
                    case BASIC:
                        attacks.basicAttack(selectedCard, opponentCards.get(0));
                        break;
                    case IGNITE:
                        //ignition attack will be last for 3 turns, every turn ignited card will takes damage by 2 points
                        if (opponentCards.get(0).getIgnRoundCounter() == 0) {
                            attacks.ignite(selectedCard, opponentCards.get(0));
                        } else {
                            System.out.println("The targeted cart is already ignited");
                        }
                        break;

                    case DUALATTACK:
                        attacks.dualAttack((CreatureCard) selectedCard, opponentCards.get(0), opponentCards.get(1));
                        break;

                    case PLAYERATTACK:
                        attacks.attackPlayer(selectedCard, opponentPlayer);
                        break;

                    case ATTACKALL:
                        attacks.attackAll(selectedCard, opponentPlayer.getTableCards());
                        break;

                    default:
                        break;
                }
            }
        }
    }

    public void chooseConsoleAttack(Card selectedCard) {
        String nameOfAttack = selectedCard.getSpecialAttack().toUpperCase();
        for (AttackNames attackName : AttackNames.values()) {
            if (attackName.name().equals(nameOfAttack)) {
                switch (attackName) {
                    case BASIC:
                        System.out.println("Choose a card to attack");
                        int attackedCardNr = getInput();
                        CreatureCard attackedCard = (CreatureCard) opponentPlayer.getTableCards().get(attackedCardNr - 1);
                        attacks.basicAttack(selectedCard, attackedCard);
                        break;
                    case IGNITE:
                        //ignition attack will be last for 3 turns, every turn ignited card will takes damage by 2 points
                        System.out.println("Choose a card to attack");
                        attackedCardNr = getInput();
                        attackedCard = (CreatureCard) opponentPlayer.getTableCards().get(attackedCardNr - 1);
                        if (attackedCard.getIgnRoundCounter() == 0) {
                            attacks.ignite(selectedCard, attackedCard);
                        } else {
                            System.out.println("The targeted cart is already ignited");
                        }
                        break;

                    case DUALATTACK:
                        System.out.println("Choose two cards to attack");
                        attackedCardNr = getInput();
                        attackedCard = (CreatureCard) opponentPlayer.getTableCards().get(attackedCardNr - 1);
                        attackedCardNr = getInput();
                        CreatureCard attackedCard2 = (CreatureCard) opponentPlayer.getTableCards().get(attackedCardNr - 1);
                        attacks.dualAttack((CreatureCard) selectedCard, attackedCard, attackedCard2);
                        break;

                    case PLAYERATTACK:
                        attacks.attackPlayer(selectedCard, opponentPlayer);
                        break;

                    case ATTACKALL:
                        attacks.attackAll(selectedCard, opponentPlayer.getTableCards());
                        break;

                    default:
                        break;
                }
            }
        }
    }


    private void playerMenu() throws IOException {
        int input;
        boolean isOpponentTableEmpty = opponentPlayer.getTableCards().isEmpty();
        System.out.println(
                "------------------------------------------------- \n" +
                        (currentPlayer == p1 ? "Player 1 \n" : "Player 2 \n") +
                        "Here are your choices: \n" +
                        "1. Show table \n" +
                        "2. Play card on hand \n" +
                        (isOpponentTableEmpty ? "3. Attack player \n" : "3. Attack a card \n") +
                        "4. End Turn \n" +
                        "-------------------------------------------------");
        input = getInput();

        switch (input) {
            case 0:
                playerMenu();
                break;
            case 1:
                showTable();
                break;
            case 2:
                int playCard;
                System.out.println("what card do you want to play out? (0 to cancel)");

                playCard = getInput();
                if (playCard == 0) {
                    playerMenu();
                    return;
                }
                Card card = currentPlayer.getPlayerHand().get(playCard - 1);
                currentPlayer.playCard(card, getRound());
                break;
            case 3:
                if (turn > 2) {
                    System.out.println("Attack with: 1. Magic card or 2. Creature");
                    int choice = getInput();
                    System.out.println("Choose a card to attack with");
                    int cardNr = getInput();
                    if (choice == 1) {
                        MagicCard magicCard = (MagicCard) currentPlayer.getPlayerHand().get(cardNr - 1);
                        if (isOpponentTableEmpty) {
                            attackPlayerWhenTableEmpty(magicCard);
                            return;
                        }
                        chooseConsoleAttack(magicCard);
                        currentPlayer.sendToGraveyard(magicCard);
                        for (int i = 0; i < opponentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
                            if (isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(i))) {
                                opponentPlayer.sendToGraveyard(opponentPlayer.getTableCards().get(i));
                            }
                        }
                        checkHealthLeft();
                    } else if (choice == 2) {
                        CreatureCard creatureCard = (CreatureCard) currentPlayer.getTableCards().get(cardNr - 1);
                        if (!checkIfTapped(creatureCard)) {
                            if (isCardReadyToAttack(creatureCard)) {
                                if (isOpponentTableEmpty) {
                                    attackPlayerWhenTableEmpty(creatureCard);
                                    return;
                                }
                                chooseConsoleAttack(creatureCard);
                                for (int i = 0; i < opponentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
                                    if (isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(i))) {
                                        opponentPlayer.sendToGraveyard(opponentPlayer.getTableCards().get(i));
                                    }
                                }
                                if (isCardKilled(creatureCard)) {
                                    currentPlayer.sendToGraveyard(creatureCard);
                                } else {
                                    creatureCard.tap();
                                }
                                checkHealthLeft();
                            } else {
                                System.out.println("Card is not ready to attack yet");
                            }
                        }
                    } else {
                        playerMenu();
                    }

                } else {
                    System.out.println("You can't attacks the first round!");
                }
                break;
            case 4:
                endTurn();
                break;
        }


    }

    public int getInput() {
        Scanner sc = new Scanner(System.in);
        int input = 0;

        try {
            input = sc.nextInt();

        } catch (InputMismatchException e) {
            System.out.println("You need to enter a number, try again");
        }
        return input;
    }

    public boolean checkIfTapped(CreatureCard creatureCard) {
        if (creatureCard.isTapped()) {
            System.out.println("Card is tapped, use another!");
            return true;
        }

        return false;
    }

    /**
     * prints the cards on the table,
     * presented with hp values
     */
    public void showTable() {
        System.out.println("--------------------------------------------------------------------");
        if (currentPlayer == p1)
            System.out.println("Player 1");
        if (currentPlayer == p2)
            System.out.println("Player 2");
        System.out.println("Turn " + turn);
        System.out.println("----------");
        CreatureCard creatureCard;

        int currentHealth = currentPlayer.getHealth();
        int opponentHealth = opponentPlayer.getHealth();
        System.out.print("Your health: " + currentHealth + " hp");
        System.out.println();
        System.out.println();

        System.out.print("Your hand: \n");
        printCards(currentPlayer.getPlayerHand());
        System.out.println();
        System.out.println();

        System.out.println("Your table: ");
        printCards(currentPlayer.getTableCards());
        System.out.println();
        System.out.println();

        System.out.print("Opponents table: ");
        printCards(opponentPlayer.getTableCards());
        System.out.println();
        System.out.println();
        System.out.print("Opponents health: " + opponentHealth + " hp");
        System.out.println();


        System.out.println("--------------------------------------------------------------------");
    }

    void printCards(ArrayList<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card instanceof CreatureCard)
                System.out.println(i + 1 + ": creature card with " + ((CreatureCard) card).getHp() + " hp  " + card.getAttack() + " attack ");


            if (card instanceof MagicCard) {

                System.out.println(i + 1 + ": magic card with special attack " + card.getSpecialAttack());

            }

        }
    }

    /**
     * untaps all current players tapped cards
     */
    public void unTap() {

        ArrayList<Card> cards = currentPlayer.getTableCards();
        CreatureCard creatureCard;

        for (int i = 0; i < cards.size(); i++) {

            creatureCard = (CreatureCard) cards.get(i);
            if (creatureCard.isTapped())
                creatureCard.unTap();
        }
    }

    public boolean isCardReadyToAttack(CreatureCard creatureCard) {
        if ((creatureCard.getPlayedOnRound() + creatureCard.getPower()) < getRound())
            return true;
        return false;
    }

    public void attackPlayerWhenTableEmpty(Card card) {
        attacks.attackPlayer(card, opponentPlayer);
    }
}
