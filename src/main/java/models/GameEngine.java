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
        checkTableCardsHp(opponentPlayer);
        checkTableCardsHp(currentPlayer);
    }

    public void checkTableCardsHp(Player p) {
        for (int i = 0; i < p.getTableCards().size(); i++) {
            if (isCardKilled((CreatureCard) p.getTableCards().get(i))) {
                p.sendToGraveyard(p.getTableCards().get(i));
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
                attackWithMagicCard(selectedCard, opponentCards);
            } else {
                attackWithCreatureCard(selectedCard, opponentCards);
            }
            checkTableCardsHp(opponentPlayer);
            checkHealthLeft();
        } else {
            Server.getInstance().msgToFX("tosoon");
        }
    }

    public void attackWithMagicCard(Card card, List<CreatureCard> opponentCards) {
        if (card.getCardEnergy() <= currentPlayer.getPlayerEnergy()) {
            chooseAttack(card, opponentCards);
            currentPlayer.sendToGraveyard(card);
            currentPlayer.setPlayerEnergy(currentPlayer.getPlayerEnergy() - card.getCardEnergy());
        } else {
            Server.getInstance().msgToFX("showmessage,To low on mana");
        }
    }

    public void attackWithCreatureCard(Card card, List<CreatureCard> opponentCards) {
        if (!checkIfTapped((CreatureCard) card)) {
            if (isCardReadyToAttack((CreatureCard) card)) {
                chooseAttack(card, opponentCards);
                ((CreatureCard) card).tap();
                checkAttackingCardHp(card);
            } else {
                Server.getInstance().msgToFX("showmessage,Card not ready!");
            }
        } else {
            Server.getInstance().msgToFX("showmessage, Card is tapped!");
        }
    }

    public void checkAttackingCardHp(Card card) {
        if (isCardKilled((CreatureCard) card)) {
            currentPlayer.sendToGraveyard(card);
        }
    }

    public void chooseAttack(Card selectedCard, List<CreatureCard> opponentCards) {
        String nameOfAttack = selectedCard.getSpecialAttack().toUpperCase();
        CreatureCard opponentCardOne = opponentCards == null || opponentCards.isEmpty() ? null : opponentCards.get(0);
        CreatureCard opponentCardTwo = opponentCards == null || opponentCards.size() < 2 ? null : opponentCards.get(1);

        for (AttackNames attackName : AttackNames.values()) {
            if (attackName.name().equals(nameOfAttack)) {
                if (opponentPlayer.getTableCards().isEmpty())
                    attackName = AttackNames.PLAYERATTACK;
                switch (attackName) {
                    case BASIC:
                        performBasicAttack(selectedCard, opponentCardOne);
                        break;
                    case IGNITE:
                        performIgniteAttack(selectedCard, opponentCardOne);
                        break;
                    case DUALATTACK:
                        try {
                            performDualAttack(selectedCard, opponentCardOne, opponentCardTwo);
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

    private void performBasicAttack(Card selectedCard, CreatureCard creatureCard) {
        CreatureCard cardToAttack;

        if (creatureCard == null) {
            cardToAttack = getCardToAttackConsole();
        } else {
            cardToAttack = creatureCard;
        }
        attacks.basicAttack(selectedCard, cardToAttack);
    }

    private void performIgniteAttack(Card selectedCard, CreatureCard creatureCard) {
        if (creatureCard == null) {
            performConsoleIgniteAttack(selectedCard);
        } else {
            performFxIgniteAttack(selectedCard, creatureCard);
        }
    }

    private void performConsoleIgniteAttack(Card selectedCard) {
        CreatureCard cardToAttack = getCardToAttackConsole();
        boolean cardNotIgnited = cardToAttack.getIgnRoundCounter() == 0;

        if (cardNotIgnited) {
            attacks.ignite(selectedCard, cardToAttack);
        } else {
            System.out.println("The targeted cart is already ignited");
        }
    }

    private void performFxIgniteAttack(Card selectedCard, CreatureCard cardToAttack) {
        boolean cardNotIgnited = cardToAttack.getIgnRoundCounter() == 0;

        if (cardNotIgnited) {
            attacks.ignite(selectedCard, cardToAttack);
        } else {
            server.msgToFX("Already ignited");
        }
    }

    private void performDualAttack(Card selectedCard, CreatureCard cardToAttackOne, CreatureCard cardToAttackTwo) {
        if (cardToAttackOne == null || cardToAttackTwo == null) {
            performConsoleDualAttack(selectedCard);
        } else {
            performFxDualAttack(selectedCard, cardToAttackOne, cardToAttackTwo);
        }
    }

    private void performConsoleDualAttack(Card selectedCard) {
        System.out.println("Choose two cards to attack");
        CreatureCard attackedCardOne = getCardToAttack();
        boolean opponentTableHasTwoCards = opponentPlayer.getTableCards().size() >= 2;

        if (opponentTableHasTwoCards) {
            CreatureCard attackedCardTwo = getCardToAttack();
            attacks.dualAttack((CreatureCard) selectedCard, attackedCardOne, attackedCardTwo);
        } else {
            attacks.basicAttack(selectedCard, attackedCardOne);
            attacks.attackPlayer(selectedCard, opponentPlayer);
        }
    }

    private void performFxDualAttack(Card selectedCard, CreatureCard cardToAttackOne, CreatureCard cardToAttackTwo) {
        attacks.dualAttack((CreatureCard) selectedCard, cardToAttackOne, cardToAttackTwo);
    }

    private CreatureCard getCardToAttackConsole() {
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
                playCard();
                showTable();
                break;
            case 3:
                attackInConsole(isOpponentTableEmpty);
                showTable();
                break;
            case 4:
                endTurn();
                showTable();
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

    public void playCard() {
        int playCard;
        Card card;
        System.out.println("what card do you want to play out? (0 to cancel)");
        playCard = getInput();
        if (playCard == 0) {
            playerMenu();
        } else {
            card = getACardFromList(playCard, currentPlayer.getPlayerHand());
            if (card == null) {
                playCard();
                return;
            }
            currentPlayer.playCard(card, getRound());
        }
    }

    public void attackInConsole(boolean isOpponentTableEmpty) {
        if (turn > 2) {
            System.out.println("Attack with: 1. Magic card or 2. Creature");
            int choice = getInput();
            System.out.println("Choose a card to attack with");
            int cardNr = getInput();
            Card card;
            if (choice == 1) {
                card = getACardFromList(cardNr, currentPlayer.getPlayerHand());
                if (card == null) {
                    attackInConsole(isOpponentTableEmpty);
                    return;
                }
                if (!(card instanceof MagicCard)) {
                    System.out.println("Wrong card. Try again");
                    attackInConsole(isOpponentTableEmpty);
                    return;
                } else {
                    attackWithMagicCardInConsole(card, isOpponentTableEmpty);
                }
            } else if (choice == 2) {
                card = getACardFromList(cardNr, currentPlayer.getTableCards());
                if (card == null) {
                    attackInConsole(isOpponentTableEmpty);
                    return;
                }
                attackWithCreatureCardInConsole((CreatureCard) card, isOpponentTableEmpty);
            } else {
                attackInConsole(isOpponentTableEmpty);
            }
        } else {
            System.out.println("You can't attack on the first round!");
        }
    }

    public void attackWithMagicCardInConsole(Card card, boolean isOpponentTableEmpty) {
        if (card.getCardEnergy() <= currentPlayer.getPlayerEnergy()) {
            if (isOpponentTableEmpty) {
                attackPlayerWhenTableEmpty(card);
            } else {
                chooseAttack(card, null);
            }
            currentPlayer.sendToGraveyard(card);
            currentPlayer.setPlayerEnergy(currentPlayer.getPlayerEnergy() - card.getCardEnergy());
            checkTableCardsHp(opponentPlayer);
            checkHealthLeft();
        } else {
            System.out.println("You don't have enough energy to use this card");
        }
    }

    public void attackWithCreatureCardInConsole(CreatureCard card, boolean isOpponentTableEmpty) {
        if (!checkIfTapped(card)) {
            if (isCardReadyToAttack(card)) {
                if (isOpponentTableEmpty) {
                    attackPlayerWhenTableEmpty(card);
                } else {
                    chooseAttack(card, null);
                    checkTableCardsHp(opponentPlayer);
                }
                checkAttackingCardHp(card);
                card.tap();
                checkHealthLeft();
            } else {
                System.out.println("Card is not ready to attack yet");
            }
        }
    }

    public Card getACardFromList(int cardNr, List<Card> cardList) {
        Card card = null;
        try {
            card = cardList.get(cardNr - 1);
        } catch (Exception e) {
            System.out.println("That card doesn't exist. Try again.");
        }
        return card;
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
