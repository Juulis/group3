package models;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
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

    public Deck() {
        cards = new ArrayList<Card>();
        playerOneDeck = new ArrayList<Card>();
        playerTwoDeck = new ArrayList<Card>();
    }

    /**
     * create array of deck cards with 50 cards
     */
    public void createFullDeck() {
        getCardsFromJSON();
    }

    public void playerDeck() { // Shuffle game cards and split them into two player decks
        List<Card> shuffledDeck = new ArrayList<>(cards);
        Collections.shuffle(shuffledDeck);

        playerDeckSize = 25;

        playerOneDeck.addAll(shuffledDeck.subList(0, shuffledDeck.size() / 2));
        playerTwoDeck.addAll(shuffledDeck.subList(shuffledDeck.size() / 2, shuffledDeck.size()));

        Collections.shuffle(playerOneDeck);
        Collections.shuffle(playerTwoDeck);
    }

    /**
     * @return Deck Array of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    public ArrayList<Card> getPlayerOneDeck() {
        return playerOneDeck;
    }

    public ArrayList<Card> getPlayerTwoDeck() {
        return playerTwoDeck;
    }

    public List<Card> getCardsFromJSON() {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("src/main/java/json/Deck.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<CreatureCard> tempCards = Arrays.asList(new Gson().fromJson(reader, CreatureCard[].class));

        for (int i = 0; i < tempCards.size(); i++) {
            if (tempCards.get(i).getHp() == 0) {
                cards.add(new MagicCard(i, tempCards.get(i).attack, tempCards.get(i).cardEnergy, tempCards.get(i).cardName, tempCards.get(i).specialAttack, tempCards.get(i).getImgURL()));
            } else {
                cards.add(tempCards.get(i));
                tempCards.get(i).setId(i);
            }
        }
        return cards;
    }
}
