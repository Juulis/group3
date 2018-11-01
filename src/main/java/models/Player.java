package models;

import java.util.ArrayList;

public class Player {

    private int health;

    private ArrayList<Card> currentDeck;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> tableCards;

    public Player() {
        this.health = 10;
        this.currentDeck = new ArrayList<Card>();
        this.playerHand = new ArrayList<Card>();
        this.tableCards = new ArrayList<Card>();
    }

    public int getHealth() {
        return health;
    }

    public void setCurrentDeck(ArrayList<Card> gameCards) {
        this.currentDeck = gameCards;
    }

    public ArrayList<Card> getCurrentDeck() {

        return currentDeck;
    }

    public ArrayList<Card> getPlayerHand() {

        return playerHand;
    }

    public ArrayList<Card> getTableCards() {

        return tableCards;
    }

    /**
     * takes one card from players deck and
     * puts it in players hand
     */
    public void pickupCard() {

        int index = currentDeck.size() - 1;
        Card card = currentDeck.remove(index);
        playerHand.add(card);
    }

    /**
     * removes the card from tableCards
     *
     * @param card
     */
    public void sendToGraveyard(Card card) {

        tableCards.remove(card);
    }

    /**
     * @param playCardNr takes an int showing what card to play
     *                   play the chosen card:
     *                   remove it from hand
     *                   add it to table
     */
    public void playCard(int playCardNr) {
        int correctedPlayCardNr = playCardNr - 1;
        tableCards.add(playerHand.get(correctedPlayCardNr));
        playerHand.remove((correctedPlayCardNr));
    }

    /**
     * When a player gets attacked, remove the amount of attack from the health
     *
     * @param healthToRemove of attack
     */
    public void removeHp(int healthToRemove) {
        this.health -= healthToRemove;
    }
}
