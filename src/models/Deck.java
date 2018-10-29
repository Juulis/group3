package models;

import java.util.ArrayList;

public class Deck {

    private Card card;
    private int getTotalCards;
    private ArrayList<Card> cards;
    private  int totalCards;

    public Deck() {
        cards=new ArrayList<>();
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
    /**
     *
     * @return  Deck Array of cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
}
