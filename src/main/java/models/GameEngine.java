package models;

import java.util.ArrayList;
import java.util.Scanner;

public class GameEngine {

    public GameEngine(){
        p1 = new Player();
        p2 = new Player();
        game = true;
        playing = true;
        deck = new Deck();
    }

    private Player p1, p2;
    private ArrayList<Card> gameCards;
    private Player currentPlayer;
    private Player opponent;
    private Deck deck;
    private boolean game;
    private boolean playing;

    public void setP1(Player p) {
        this.p1 = p;
    }

    public void setP2(Player p) {
        this.p2 = p;
    }

    public void setDeck(Deck deck){
        this.deck=deck;
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
                //TODO: implement some checks here, firstRun etc
                playerMenu();
                //TODO: implement turnchange here, playerToggle
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

        ArrayList<Card> playerOneDeck, playerTwoDeck;
        deck.playerDeck();
        playerOneDeck=deck.getPlayerOneDeck();
        playerTwoDeck=deck.getPlayerTwoDeck();
        p1.setCurrentDeck(playerOneDeck);
        p2.setCurrentDeck(playerTwoDeck);
        for (int i = 0; i < 5; i++) {
            p1.pickupCard();
            p2.pickupCard();
        }
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
                break;
            case 2:
                System.out.println("what card do you want to play out?");
                int playCard = sc.nextInt();
                currentPlayer.playCard(playCard);
                break;
            case 3:
                break;
            case 4:
                break;
        }

    }
}
