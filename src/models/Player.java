package models;

import java.util.ArrayList;

public class Player {

    private ArrayList<Card> playerHand = new ArrayList<>();
    private ArrayList<Card> tableCards = new ArrayList<>();


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

    public ArrayList getPlayerHand() {
        return playerHand;
    }
    public ArrayList getTableCards() {
        return tableCards;
    }

}
