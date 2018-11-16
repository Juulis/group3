package models;

import java.util.ArrayList;

public class Player {

    private int health;
    private String name;
    private int score;

    private ArrayList<Card> currentDeck;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> tableCards;

    public Player() {
        this.health = 20;
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
        server.msgToFX("showplayerhand,1," + player1hand);
//        server.msgToFX("showplayerhand,2," + player2hand);
    }

    /**
     * removes the card from tableCards
     *
     * @param card
     */
    public void sendToGraveyard(Card card) {
        if (card instanceof MagicCard) {
            playerHand.remove(card);
        }
        tableCards.remove(card);
    }

    /**
     * @param card takes an int showing what card to play
     *                   play the chosen card:
     *                   remove it from hand
     *                   add it to table
     */
    public void playCard(Card card, int round) {
        try {
            if (card.getClass() == CreatureCard.class) {
                tableCards.add(card);
                playerHand.remove(card);
                ((CreatureCard) card).tap();
                ((CreatureCard) card).setPlayedOnRound(round);
            }
        } catch (Exception e) {
            System.out.println("That card does not exist");
        }
    }

    /**
     * When a player gets attacked, remove the amount of attack from the health
     *
     * @param healthToRemove of attack
     */
    public void removeHp(int healthToRemove) {
        this.health -= healthToRemove;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
