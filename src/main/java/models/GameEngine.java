package models;

import java.security.spec.ECField;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class GameEngine {

    public GameEngine() {
        p1 = new Player();
        p2 = new Player();
        game = true;
        playing = true;
        deck = new Deck();
        turn = 1;
        attack = new Attack();
    }

    private Player p1, p2;
    private Player currentPlayer;
    private Player opponentPlayer;
    private Deck deck;
    private boolean game;
    private boolean playing;
    private int turn;
    private Attack attack ;

    public void setP1(Player p) {
        this.p1 = p;
    }

    public void setP2(Player p) {
        this.p2 = p;
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

    public void startGame() {
        while (game) {
            initGame();
            while (playing) {
                playerMenu();
            }
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

            playing = false;

        }
        if (p2.getCurrentDeck().size() == 0 && p2.getPlayerHand().size() == 0 && p2.getTableCards().size() == 0) {
            System.out.println("Congratulations!" + p1 + " is the Winner");


            playing = false;
        }

    }

    public void checkPlayerHealth() {
        if (p1.getHealth() <= 0) {
            System.out.println("Congratulations! Player2 is the Winner");

            playing = false;

        } else if (p2.getHealth() <= 0) {
            System.out.println("Congratulations! Player1 is the Winner");

            playing = false;
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

    public void endTurn() {
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

    public void attack(CreatureCard currentPlayerCard, CreatureCard opponentCard) {
        boolean playerAttack = false;
        int currentPlayerAttack = currentPlayerCard.getAttack();
        int opponentPlayerAttack;
        try {
            opponentPlayerAttack = opponentCard.getAttack();
        } catch (NullPointerException e) {
            System.out.println("Attacking Opponent!");
            playerAttack = true;
            opponentPlayerAttack = 0;
        }

        int damage = currentPlayerAttack - opponentPlayerAttack;
        damage = Math.abs(damage);

        System.out.println("---------------------------DICE ROLLED-------------------------------");
        System.out.println("You rolled " + currentPlayerAttack);
        System.out.println("Your opponent rolled " + opponentPlayerAttack);
        if (currentPlayerAttack > opponentPlayerAttack) {
            int amountOfAttack = currentPlayerAttack - opponentPlayerAttack;
            System.out.println("You get to attack your opponent with: " + amountOfAttack + " dmg");
        } else {
            int amountOfAttack = opponentPlayerAttack - currentPlayerAttack;
            System.out.println("Your opponent gets to attack you with : " + amountOfAttack + " dmg");
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println();


        if (!playerAttack && currentPlayerAttack > opponentPlayerAttack) {
            opponentCard.removeHp(damage);
            if (isCardKilled( opponentCard)) {
                opponentPlayer.sendToGraveyard(opponentCard);
                System.out.println("You killed a card\n");
                System.out.println("---------------------------------------------------------------------");
            }
        } else if (playerAttack && currentPlayerAttack > opponentPlayerAttack) {
            opponentPlayer.removeHp(damage);
        } else if (currentPlayerAttack < opponentPlayerAttack) {
            currentPlayerCard.removeHp(damage);
            if (isCardKilled((CreatureCard) currentPlayerCard)) {
                currentPlayer.sendToGraveyard(currentPlayerCard);
                System.out.println("You lost a card\n");
                System.out.println("---------------------------------------------------------------------");
            }
        }

        currentPlayerCard.tap();
        checkPlayerHealth();
    }
    public  void chooseAttack(String nameOfAttack){
        nameOfAttack=nameOfAttack.toUpperCase();
        for (AttacksNames attackName : AttacksNames.values()) {
            if (attackName.name().equals(nameOfAttack)) {
                switch (attackName){
                    case BASIC:
                      attack.basicAttack();
                        break;
                    case IGNITE:
                      attack.ignite();


                    case DUAlATTACK:
                       attack.dualAttack();
                        break;

                    case PLAYERATTACK:
                        attack.attackPlayer();
                        break;

                    case ATTACKALL:
                        attack.attackAllMC();
                        break;

                        default:
                        break;


                }
            }
        }





    private void playerMenu() {
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

                currentPlayer.playCard(playCard);
                break;
            case 3:
                if (turn > 2) {
                    int attackCard;
                    int cardToAttack;

                    System.out.println("what card you like to attack with? (0 to cancel)");

                    attackCard = getInput();
                    if (attackCard == 0) {
                            playerMenu();
                            return;
                    }

                    try {
                        if (checkIfTapped((CreatureCard) currentPlayer.getTableCards().get(attackCard - 1))) {
                            break;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("That card does not exist");
                        break;
                    }

                    if (!opponentPlayer.getTableCards().isEmpty()) {
                        System.out.println("what card do you want to attack?");
                        try {
                            cardToAttack = getInput();
                            attack((CreatureCard) currentPlayer.getTableCards().get(attackCard - 1), (CreatureCard) opponentPlayer.getTableCards().get(cardToAttack - 1));

                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("That card does not exist");
                        }
                    } else {
                        attack((CreatureCard) currentPlayer.getTableCards().get(attackCard - 1), null);
                    }
                } else {
                    System.out.println("You can't attack the first round!");
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
        ArrayList<Card> currentHandCards = currentPlayer.getPlayerHand();
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
            if( card instanceof CreatureCard)

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
}
