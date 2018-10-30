package models;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private Card card;
    private int getTotalCards;
    private ArrayList<Card> cards;
    private ArrayList<Card> playerOneDeck;
    private ArrayList<Card> playerTwoDeck;
    public int playerDeckSize;
    private int totalCards;

    public Deck() {
        cards=new ArrayList<Card>();
        totalCards=50;
    }

    /**
     *      create array of deck cards with 50 cards
     */
    public  void createFullDeck(){
        for (int i=0;i<totalCards;i++){
            cards.add(new Card());
        }
    }

    public void playerDeck() { // Shuffle game cards and split them into two player decks
        Collections.shuffle(cards);

        playerDeckSize = 25;

        playerOneDeck.addAll(cards.subList(0, cards.size() / 2 - 1));
        playerTwoDeck.addAll(cards.subList(cards.size() / 2, cards.size()-1));

        Collections.shuffle(playerOneDeck);
        Collections.shuffle(playerTwoDeck);
    }

    /**
     *
     * @return  Deck Array of cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
}
