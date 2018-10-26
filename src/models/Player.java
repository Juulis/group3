package models;

import java.util.ArrayList;

public class Player {

    private ArrayList<Card> currentDeck = new ArrayList<>();
    private ArrayList<Card> playerHand = new ArrayList<>();
    private ArrayList<Card> tableCards = new ArrayList<>();


    void playCard(int playCard) {
    /*
    * takes an int showing what card to play
    * play the chosen card:
    * remove it from hand
    * add it to table
    * */
    }

    public ArrayList getCurrentDeck() {
        return currentDeck;
    }
    public ArrayList getPlayerHand() {
        return playerHand;
    }
    public ArrayList getTableCards() {
        return tableCards;
    }

}
