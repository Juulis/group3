package models;

import java.util.ArrayList;

public class Player {
    private int health;
    private ArrayList<Card> currentDeck=new ArrayList<>();
    private ArrayList<Card> playerHand=new ArrayList<>();
    private ArrayList<Card> tableCards=new ArrayList<>();

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

    /**
     * @param playCard
     * takes an int showing what card to play
     * play the chosen card:
     * remove it from hand
     * add it to table
     */
    void playCard(int playCardNr) {
        int correctedPlayCardNr = playCardNr-1;
        tableCards.add(playerHand.get(correctedPlayCardNr));
        playerHand.remove((correctedPlayCardNr));
    }

}
