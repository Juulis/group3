package models;

import app.Server;
import utilities.ScoreHandler;

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

    public void startGame(String startArgs) {
        System.out.println("starting game");
        if (startArgs.equals("fx")) {
            server.msgToFX("setplayernames," + p1.getName() + "," + p2.getName());
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


    public void initGame() {
        deck.createFullDeck();
        initPlayer();
    }


    /**
     * initializes the players by
     * setting the players decks and cards in hands
     */
    public void initPlayer() {
        int startEnergy = 2;
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
        p1.setPlayerEnergy(startEnergy);
        p2.setPlayerEnergy(startEnergy);
    }

    public void checkCardsLeft() {
        checkPlayerCards(p1, p2);
        checkPlayerCards(p2, p1);
    }

    public void checkPlayerCards(Player p, Player q) {
        if (p.getCurrentDeck().size() == 0 && p.getPlayerHand().size() == 0 && p.getTableCards().size() == 0) {
            System.out.println("Congratulations!" + q.getName() + " is the Winner");
            scoreHandler.checkScore(q);
            playing = false;
            if (!consoleGame) {
                Server.getInstance().msgToFX("gameover");
            }

        }
    }

    public void checkHealthLeft() {
        checkPlayerHealth(p1, p2);
        checkPlayerHealth(p2, p1);
    }

    public void checkPlayerHealth(Player p, Player q) {
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

    public void endTurn() {
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
        if (currentPlayer.getCurrentDeck().size() != 0) {
            currentPlayer.pickupCard();
        }
        turn++;
        increaseIgnCounter(currentPlayer.getTableCards());
        increaseIgnCounter(opponentPlayer.getTableCards());
        if (turn > 2) {
            regeneratePlayerEnergy(currentPlayer);
        }
        if (!consoleGame)
            server.msgToFX("player" + Integer.toString(active));
        for (int i = 0; i < opponentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
            if (isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(i))) {
                opponentPlayer.sendToGraveyard(opponentPlayer.getTableCards().get(i));
            }
        }
        for (int i = 0; i < currentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
            if (isCardKilled((CreatureCard) currentPlayer.getTableCards().get(i))) {
                currentPlayer.sendToGraveyard(currentPlayer.getTableCards().get(i));
            }
        }
    }

    public void regeneratePlayerEnergy(Player p) {
        p.setPlayerEnergy(p.getPlayerEnergy() + 2);
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

    public void attack(Card selectedCard, List<CreatureCard> opponentCards) {
        //TODO: change to string instead of ENUM . works with string to since java 8

        if (currentPlayer.getPlayerHand().contains(selectedCard) && !(selectedCard instanceof MagicCard)) {
            return;
        }
        if (getRound() > 1) {
            if (selectedCard instanceof MagicCard) {
                if (selectedCard.getCardEnergy() <= currentPlayer.getPlayerEnergy()) {
                    chooseAttack(selectedCard, opponentCards);
                    currentPlayer.sendToGraveyard(selectedCard);
                    currentPlayer.setPlayerEnergy(currentPlayer.getPlayerEnergy() - selectedCard.getCardEnergy());
                } else {
                    Server.getInstance().msgToFX("showmessage,To low on mana");
                }
            }
            if (selectedCard instanceof CreatureCard) {
                if (!checkIfTapped((CreatureCard) selectedCard)) {
                    if (isCardReadyToAttack((CreatureCard) selectedCard)) {
                        chooseAttack(selectedCard, opponentCards);
                        ((CreatureCard) selectedCard).tap();
                        if (isCardKilled((CreatureCard) selectedCard)) {
                            currentPlayer.sendToGraveyard(selectedCard);
                        }
                    } else {
                        Server.getInstance().msgToFX("showmessage,Card not ready!");
                    }
                } else {
                    Server.getInstance().msgToFX("showmessage, Card is tapped!");
                }
            }
            for (int i = 0; i < opponentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
                if (isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(i))) {
                    opponentPlayer.sendToGraveyard(opponentPlayer.getTableCards().get(i));
                }
            }
            checkHealthLeft();
        } else {
            Server.getInstance().msgToFX("tosoon");
        }
    }

    public void chooseAttack(Card selectedCard, List<CreatureCard> opponentCards) {
        String nameOfAttack = selectedCard.getSpecialAttack().toUpperCase();
        for (AttackNames attackName : AttackNames.values()) {
            if (attackName.name().equals(nameOfAttack)) {
                if (opponentPlayer.getTableCards().isEmpty())
                    attackName = AttackNames.PLAYERATTACK;
                switch (attackName) {
                    case BASIC:
                        attacks.basicAttack(selectedCard, opponentCards.get(0));
                        break;
                    case IGNITE:
                        //ignition attack will be last for 3 turns, every turn ignited card will takes damage by 2 points
                        if (opponentCards.get(0).getIgnRoundCounter() == 0) {
                            attacks.ignite(selectedCard, opponentCards.get(0));
                        } else {
                            server.msgToFX("Already ignited");
                        }
                        break;

                    case DUALATTACK:
                        try {
                            attacks.dualAttack((CreatureCard) selectedCard, opponentCards.get(0), opponentCards.get(1));
                        } catch (Exception e) {
                            nameOfAttack = "playerattack";
                        }
                        break;
                    case ATTACKALL:
                        attacks.attackAll(selectedCard, opponentPlayer.getTableCards());
                        break;

                    case PLAYERATTACK:
                        attacks.attackPlayer(selectedCard, opponentPlayer);
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
                        performBasicAttack(selectedCard);
                        break;

                    case IGNITE:
                        performIgniteAttack(selectedCard);
                        break;

                    case DUALATTACK:
                        performDualAttack(selectedCard);
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

    private void performBasicAttack(Card selectedCard) {
        CreatureCard cardToAttack = getCardToAttackBasicOrIgnite();
        attacks.basicAttack(selectedCard, cardToAttack);
    }

    private void performIgniteAttack(Card selectedCard) {
        CreatureCard cardToAttack = getCardToAttackBasicOrIgnite();

        if (cardToAttack.getIgnRoundCounter() == 0) {
            attacks.ignite(selectedCard, cardToAttack);
        } else {
            System.out.println("The targeted cart is already ignited");
        }
    }

    private void performDualAttack(Card selectedCard) {
        System.out.println("Choose two cards to attack");
        CreatureCard attackedCard = getCardToAttack();

        if (opponentPlayer.getTableCards().size() >= 2) {
            CreatureCard attackedCard2 = getCardToAttack();
            attacks.dualAttack((CreatureCard) selectedCard, attackedCard, attackedCard2);
        } else {
            attacks.basicAttack(selectedCard, attackedCard);
            attacks.attackPlayer(selectedCard, opponentPlayer);
        }
    }

    private CreatureCard getCardToAttackBasicOrIgnite() {
        System.out.println("Choose a card to attack");
        return getCardToAttack();
    }

    private CreatureCard getCardToAttack() {
        int attackedCardNr = getInput();
        return (CreatureCard) opponentPlayer.getTableCards().get(attackedCardNr - 1);
    }

    private void playerMenu() {
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
                        "9. Quit Game \n" +
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
                        Card magicCard = currentPlayer.getPlayerHand().get(cardNr - 1);
                        if (!(magicCard instanceof MagicCard)) {
                            System.out.println("Wrong card. Try again");
                        } else {
                            if (magicCard.getCardEnergy() <= currentPlayer.getPlayerEnergy()) {
                                if (isOpponentTableEmpty) {
                                    attackPlayerWhenTableEmpty(magicCard);
                                } else {
                                    chooseConsoleAttack(magicCard);
                                }
                                currentPlayer.sendToGraveyard(magicCard);
                                currentPlayer.setPlayerEnergy(currentPlayer.getPlayerEnergy() - magicCard.getCardEnergy());
                                currentPlayer.setPlayerEnergy(currentPlayer.getPlayerEnergy() - magicCard.getCardEnergy());
                                for (int i = 0; i < opponentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
                                    if (isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(i))) {
                                        opponentPlayer.sendToGraveyard(opponentPlayer.getTableCards().get(i));
                                    }
                                }
                                checkHealthLeft();
                            } else {
                                System.out.println("You don't have enough energy to use this card");
                                try {
                                    server.msgToFX("showmessage,not enough energy");
                                } catch (Exception e) {
                                }
                            }
                        }
                    } else if (choice == 2) {
                        CreatureCard creatureCard = (CreatureCard) currentPlayer.getTableCards().get(cardNr - 1);
                        if (!checkIfTapped(creatureCard)) {
                            if (isCardReadyToAttack(creatureCard)) {
                                if (isOpponentTableEmpty) {
                                    attackPlayerWhenTableEmpty(creatureCard);
                                } else {
                                    chooseConsoleAttack(creatureCard);
                                    for (int i = 0; i < opponentPlayer.getTableCards().size(); i++) { //checks all opponent table cards if they died by the attack
                                        if (isCardKilled((CreatureCard) opponentPlayer.getTableCards().get(i))) {
                                            opponentPlayer.sendToGraveyard(opponentPlayer.getTableCards().get(i));
                                        }
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
            case 9:
                System.out.println("Thank you for playing!");
                System.exit(0);
                break;
            case 1337:
                System.out.println("\n**************************************");
                System.out.println("This game was created by:");
                System.out.println("Danny");
                System.out.println("Fatlum");
                System.out.println("Lidia");
                System.out.println("Mikael");
                System.out.println("Mohammed");
                System.out.println("Rickard");
                System.out.println("Tobias");
                System.out.println("************************************** \n");
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

        int currentHealth = currentPlayer.getHealth();
        int opponentHealth = opponentPlayer.getHealth();
        int currentEnergy = currentPlayer.getPlayerEnergy();
        System.out.print("Your health: " + currentHealth + " hp     Your energy: " + currentEnergy);
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
                System.out.println(i + 1 + ": creature card with " + ((CreatureCard) card).getHp() + " hp  " + card.getAttack() + " attack " + card.getSpecialAttack() + " " + card.getCardEnergy() + " energy ");

            if (card instanceof MagicCard) {

                System.out.println(i + 1 + ": magic card with special attack " + card.getAttack() + " " + card.getSpecialAttack() + " " + card.getCardEnergy() + " energy ");

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
