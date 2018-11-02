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
    private Player opponentPlayer;
    private Deck deck;
    private boolean game;
    private boolean playing;

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
        playerOneDeck = deck.getPlayerOneDeck();
        playerTwoDeck = deck.getPlayerTwoDeck();
        p1.setCurrentDeck(playerOneDeck);
        p2.setCurrentDeck(playerTwoDeck);
        for (int i = 0; i < 5; i++) {
            p1.pickupCard();
            p2.pickupCard();
        }
    }

    /**
     * Determines if p1 or p2 is going to start
     *
     * @param randomNr
     */
    public void getPlayerToStart(int randomNr) {
        if (randomNr == 1) {
            currentPlayer = p1;
        } else {
            currentPlayer = p2;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void endTurn() {
        if (currentPlayer == p1) {
            currentPlayer = p2;
        } else {
            currentPlayer = p1;
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

    public void attack(Card currentPlayerCard, Card opponentsCard){
        int currentPlayerAttack=currentPlayerCard.attack();
        int opponentPlayerAttack=opponentsCard.attack();

        int damage=currentPlayerAttack-opponentPlayerAttack;
        damage=Math.abs(damage);

        if (currentPlayerAttack>opponentPlayerAttack){
            opponentsCard.removeHp(damage);
            if (isCardKilled(opponentsCard))
            {
                opponentPlayer.sendToGraveyard(opponentsCard);
            }

        }else if (currentPlayerAttack<opponentPlayerAttack)

            currentPlayerCard.removeHp(damage);
        if (isCardKilled(currentPlayerCard))
        {
            currentPlayer.sendToGraveyard(currentPlayerCard);

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
                System.out.println("what card you like to attack with");
                int attackCard=sc.nextInt();

                System.out.println("what card do you want to attack?");
                int cardToAttack=sc.nextInt();
                attack(currentPlayer.getTableCards().get(attackCard),opponentPlayer.getTableCards().get(cardToAttack));

            case 4:
                break;
        }

    }

    /**
     * prints the cards on the table,
     * presented with hp values
     */
    public void showTable(){

        ArrayList<Card> tableCards1=p1.getTableCards();
        ArrayList<Card> tableCards2=p2.getTableCards();
        int[] cards1=new int[tableCards1.size()];
        int[] cards2=new int[tableCards2.size()];
        System.out.print("Player1: ");
        for (int i = 0; i <tableCards1.size() ; i++) {

            cards1[i]=tableCards1.get(i).getHp();
            System.out.print(cards1[i]+" ");
        }
        System.out.println();
        System.out.println();
        System.out.print("Player2: ");
        for (int j=0; j<tableCards2.size(); j++){

            cards2[j]=tableCards2.get(j).getHp();
            System.out.print(cards2[j]+" ");
        }
        System.out.println();

    }
}
