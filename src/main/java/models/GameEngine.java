package models;

import app.Server;
import utilities.ScoreHandler;

import java.io.IOException;
import java.util.*;

public class GameEngine {

    public GameEngine() throws IOException {
        p1 = new Player();
        p2 = new Player();
        playing = true;
        deck = new Deck();
        turn = 1;
        attacks = new Attack();
        scoreHandler = new ScoreHandler();
        server = Server.getInstance();
    }

    private Player p1, p2;
    private Player currentPlayer;
    private Player opponentPlayer;
    private Deck deck;
    private boolean playing;
    private int turn;
    private Attack attacks;
    private ScoreHandler scoreHandler;
    private Server server;

    public void setP1(Player p) {
        this.p1 = p;
    }

    public void setP2(Player p) {
        this.p2 = p;
    }

    public int getTurn() {
        return turn;
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

        String player1hand = getStringFromList(p2.getPlayerHand());
        String player2hand = getStringFromList(p1.getPlayerHand());
        server.msgToFX(player1hand);
        server.msgToFX(player2hand);
    }

    private String getStringFromList(ArrayList<Card> playerHand) {
        String string = "";
        for (Card card : playerHand) {
            string += Integer.toString(card.getId());
            string += ",";
        }
        System.out.println(string);
        return string;
    }

    /**
     * initializes the players by
     * setting the players decks and cards in hands
     */
    public void initPlayer() {
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


    public void checkCardsLeft() {

        if (p1.getCurrentDeck().size() == 0 && p1.getPlayerHand().size() == 0 && p1.getTableCards().size() == 0) {
            System.out.println("Congratulations!" + p2 + " is the Winner");
            scoreHandler.checkScore(p2);
            playing = false;
            //server.msgToFX("gameover");
        }
        if (p2.getCurrentDeck().size() == 0 && p2.getPlayerHand().size() == 0 && p2.getTableCards().size() == 0) {
            System.out.println("Congratulations!" + p1 + " is the Winner");
            scoreHandler.checkScore(p1);
            playing = false;
            //server.msgToFX("gameover");
        }

    }

    public void checkPlayerHealth() throws IOException {
        if (p1.getHealth() <= 0) {
            System.out.println("Congratulations! Player2 is the Winner");
            scoreHandler.checkScore(p2);
            playing = false;
            Server.getInstance().msgToFX("gameover");

        } else if (p2.getHealth() <= 0) {
            System.out.println("Congratulations! Player1 is the Winner");
            scoreHandler.checkScore(p1);
            playing = false;
            Server.getInstance().msgToFX("gameover");
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
        if (random) {
            currentPlayer = p1;
            opponentPlayer = p2;
        } else {
            currentPlayer = p2;
            opponentPlayer = p1;
        }
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

        if (currentPlayer == p1) {
            currentPlayer = p2;
            opponentPlayer = p1;
        } else {
            currentPlayer = p1;
            opponentPlayer = p2;
        }
        currentPlayer.pickupCard();
        turn++;
        increaseIgnCounter(currentPlayer.getTableCards());
        increaseIgnCounter(opponentPlayer.getTableCards());
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

    public void chooseAttack(Card selectedCard, List<CreatureCard> opponentCards) throws IOException {
        //TODO: change to string instead of ENUM . works with string to since java 8
        boolean notTapped = true;
        String nameOfAttack = selectedCard.getSpecialAttack().toUpperCase();
        if (getRound() > 1) {
            if (selectedCard instanceof CreatureCard) {
                notTapped = checkIfTapped((CreatureCard) selectedCard);
            }
            if (notTapped) {
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
                                attacks.attackPlayer(selectedCard, getOpponentPlayer());
                                break;

                            case ATTACKALL:
                                attacks.attackAll(selectedCard, opponentPlayer.getTableCards());
                                break;

                            default:
                                break;
                        }
                    }
                }
                for (int i = 0; i < opponentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
                    if (
                            isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(i))) {
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
                checkPlayerHealth();
            } else {
                Server.getInstance().msgToFX("tapped");
            }
        } else {
            Server.getInstance().msgToFX("tosoon");
        }
    }

    private void playerMenu() throws IOException {
        int input;
        System.out.println(
                "------------------------------------------------- \n" +
                        (currentPlayer == p1 ? "Player 1 \n" : "Player 2 \n") +
                        "Here are your choices: \n" +
                        "1. Show table \n" +
                        "2. Play card on hand \n" +
                        "3. Attack a card \n" +
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
                currentPlayer.playCard(deck.getCards().get(playCard), getRound());
                break;
            case 3:
                if (turn > 2) {
                    int attackCard;
                    int cardToAttack;

                    System.out.println("what card do you like to attack with? (0 to cancel)");

                    attackCard = getInput();
                    if (attackCard == 0) {
                        playerMenu();
                        return;
                    }

                    try {
                        if (checkIfTapped((CreatureCard) currentPlayer.getTableCards().get(attackCard - 1))) {
                            break;
                        }
                        if (!isCardReadyToAttack((CreatureCard) currentPlayer.getTableCards().get(attackCard - 1))) {
                            System.out.println("Card is not ready for attack!");
                            break;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("That card does not exist");
                        break;
                    }

                    if (!opponentPlayer.getTableCards().isEmpty()) {
                        System.out.println("What card do you want to attack?");
                        try {
                            cardToAttack = getInput();
                            attacks.basicAttack(currentPlayer.getTableCards().get(attackCard - 1), (CreatureCard) opponentPlayer.getTableCards().get(cardToAttack - 1));
                            if (isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(cardToAttack - 1))) {
                                opponentPlayer.sendToGraveyard(opponentPlayer.getTableCards().get(cardToAttack - 1));

                            }
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("That card does not exist");
                        }
                    } else {
                        attacks.basicAttack(currentPlayer.getTableCards().get(attackCard - 1), null);
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
        ArrayList<Card> currentTableCards = currentPlayer.getTableCards();
        ArrayList<Card> opponentTableCards = opponentPlayer.getTableCards();
        List<Card> currentHandCards = currentPlayer.getPlayerHand();
        int[] currentTable = new int[currentTableCards.size()];
        int[] opponentTable = new int[opponentTableCards.size()];
        int[] currentHand = new int[currentHandCards.size()];
        int currentHealth = currentPlayer.getHealth();
        int opponentHealth = opponentPlayer.getHealth();
        System.out.print("Your health: " + currentHealth + " hp");
        System.out.println();
        System.out.println();
        System.out.print("Your hand: ");
        for (int i = 0; i < currentHandCards.size(); i++) {
            Card card = currentHandCards.get(i);
            if (card instanceof CreatureCard)

                currentHand[i] = ((CreatureCard) card).getHp();
            System.out.print(i + 1 + ": " + currentHand[i] + " hp  ");

        }
        System.out.println();
        System.out.println();
        System.out.print("Your table: ");
        for (int i = 0; i < currentTableCards.size(); i++) {
            creatureCard = (CreatureCard) currentTableCards.get(i);
            currentTable[i] = creatureCard.getHp();
            System.out.print(i + 1 + ": " + currentTable[i] + " hp  ");
        }
        System.out.println();
        System.out.println();
        System.out.print("Opponents table: ");
        for (int j = 0; j < opponentTableCards.size(); j++) {
            creatureCard = (CreatureCard) opponentTableCards.get(j);
            opponentTable[j] = creatureCard.getHp();
            System.out.print(j + 1 + ": " + opponentTable[j] + " hp  ");
        }
        System.out.println();
        System.out.println();
        System.out.print("Opponents health: " + opponentHealth + " hp");
        System.out.println();


        System.out.println("--------------------------------------------------------------------");
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
}
