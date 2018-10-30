package models;

import java.util.ArrayList;

public class Player {
    private int health;
    private ArrayList<Card> currentDeck=new ArrayList<Card>();
    private ArrayList<Card> playerHand=new ArrayList<Card>();

    public Player() {
        this.health = 10;
    }

    public int getHealth() {
        return health;
    }

    public void setCurrentDeck(ArrayList<Card> gameCards){

    }

    public ArrayList<Card> getCurrentDeck(){

        return currentDeck;
    }

    public ArrayList<Card> getPlayerHand(){

        return playerHand;
    }

    /**
     * takes one card from players deck and
     * puts it in players hand
     */
    public void pickupCard(){

        int index=currentDeck.size()-1;
        Card card=currentDeck.remove(index);
        playerHand.add(card);
    }

    public void newRound() {

    }
}
