package models;

import java.util.ArrayList;
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
    }

    private Player p1, p2;
    private ArrayList<Card> gameCards;
    private Player currentPlayer;
    private Player opponentPlayer;
    private Deck deck;
    private boolean game;
    private boolean playing;
    private int turn;

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
        if (p1.getCurrentDeck().size() == 0) {
            System.out.println("Congratulations!" + p2 + " is the Winner");

            playing = false;

        } else if (p2.getCurrentDeck().size() == 0) {
            System.out.println("Congratulations!" + p1 + " is the Winner");

            playing = false;
        }

    }

    public void checkPlayerHealth() {
        if (p1.getHealth() <= 0) {
            System.out.println("Congratulations!" + p2 + " is the Winner");

            playing = false;

        } else if (p2.getHealth() <= 0) {
            System.out.println("Congratulations!" + p1 + " is the Winner");

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
        checkPlayerHealth();
        checkCardsLeft();

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
     * @param card
     * @return true if hp is 0
     */
    public boolean isCardKilled(Card card) {

        if (card.getHp() <= 0)
            return true;
        return false;
    }

    public void attack(Card currentPlayerCard, Card opponentsCard) {
        int currentPlayerAttack = currentPlayerCard.attack();
        if (opponentsCard == null) {
            opponentPlayer.removeHp(currentPlayerAttack);

        } else {
            int opponentPlayerAttack = opponentsCard.attack();
            int damage = currentPlayerAttack - opponentPlayerAttack;
            damage = Math.abs(damage);

            if (currentPlayerAttack > opponentPlayerAttack) {

                opponentsCard.removeHp(damage);
                if (isCardKilled(opponentsCard)) {
                    opponentPlayer.sendToGraveyard(opponentsCard);
                }

            } else if (currentPlayerAttack < opponentPlayerAttack)

                currentPlayerCard.removeHp(damage);
            if (isCardKilled(currentPlayerCard)) {
                currentPlayer.sendToGraveyard(currentPlayerCard);
            }
        }
        currentPlayerCard.tap();
    }

    private void playerMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Here is your choices: \n" +
                "1. Show table \n" +
                "2. Play card on hand \n" +
                "3. Attack a card \n" +
                "4. End Turn");
        int input = sc.nextInt();
        switch (input) {
            case 1:
                showTable();
                break;
            case 2:
                System.out.println("what card do you want to play out?");
                int playCard = sc.nextInt();
                currentPlayer.playCard(playCard);
                break;
            case 3:
                if (turn > 2) {
                    System.out.println("what card you like to attack with");
                    int attackCard = sc.nextInt();
                    if (checkIfTapped(currentPlayer.getTableCards().get(attackCard - 1))) {
                        break;
                    }

                    System.out.println("what card do you want to attack?");
                    int cardToAttack = sc.nextInt();
                    if (opponentPlayer.getTableCards().isEmpty()) {
                        attack(currentPlayer.getTableCards().get(attackCard), opponentPlayer.getTableCards().get(cardToAttack));
                    } else {
                        attack(currentPlayer.getTableCards().get(attackCard - 1), opponentPlayer.getTableCards().get(cardToAttack - 1));
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


    public boolean checkIfTapped(Card card) {
        if (card.getTapped()) {
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

            currentHand[i] = currentHandCards.get(i).getHp();
            System.out.print(i + 1 + ": " + currentHand[i] + " hp  ");

        }
        System.out.println();
        System.out.println();
        System.out.print("Your table: ");
        for (int i = 0; i < currentTableCards.size(); i++) {

            currentTable[i] = currentTableCards.get(i).getHp();
            System.out.print(i + 1 + ": " + currentTable[i] + " hp  ");
        }
        System.out.println();
        System.out.println();
        System.out.print("Opponents table: ");
        for (int j = 0; j < opponentTableCards.size(); j++) {

            opponentTable[j] = opponentTableCards.get(j).getHp();
            System.out.print(j + 1 + ": " + opponentTable[j] + " hp  ");
        }
        System.out.println();
        System.out.println();
        System.out.print("Opponents health: " + opponentHealth + " hp");
        System.out.println();


        System.out.println("--------------------------------------------------------------------");
    }
}
