package models;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;
    private ArrayList<Card> playerOneDeck;
    private ArrayList<Card> playerTwoDeck;
    public int playerDeckSize;
    private int totalCards;

    public Deck() {
        cards = new ArrayList<Card>();
        totalCards = 50;
        playerOneDeck = new ArrayList<Card>();
        playerTwoDeck = new ArrayList<Card>();
    }

    /**
     * create array of deck cards with 50 cards
     */
    public void createFullDeck() {
        for (int i = 0; i < totalCards; i++) {
            cards.add(new Card(2, 3, "card", "basic"));
        }
    }

    public void playerDeck() { // Shuffle game cards and split them into two player decks
        Collections.shuffle(cards);

        playerDeckSize = 25;

        playerOneDeck.addAll(cards.subList(0, cards.size() / 2));
        playerTwoDeck.addAll(cards.subList(cards.size() / 2, cards.size()));

        Collections.shuffle(playerOneDeck);
        Collections.shuffle(playerTwoDeck);
    }

    /**
     * @return Deck Array of cards
     */
    public List<Card> getCards() {
        System.out.println("size of CardArray: " + cards.size());
        return cards;
    }

    public ArrayList<Card> getPlayerOneDeck() {
        return playerOneDeck;
    }

    public ArrayList<Card> getPlayerTwoDeck() {
        return playerTwoDeck;
    }

    public List<Card> getCardsFromJSON() throws IOException {
        JsonReader reader = new JsonReader(new FileReader("src/main/java/json/Deck.json"));
        List<CreatureCard> tempCards = Arrays.asList(new Gson().fromJson(reader, CreatureCard[].class));

        for (int i = 0; i < tempCards.size(); i++) {
            if (tempCards.get(i).getHp() == 0) {
                cards.add(new MagicCard(tempCards.get(i).attack, tempCards.get(i).cardEnergy, tempCards.get(i).cardName, tempCards.get(i).specialAttack));
            } else {
                cards.add(tempCards.get(i));
            }
        }
        return cards;
    }
}
