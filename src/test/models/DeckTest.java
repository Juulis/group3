package models;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


class DeckTest {
    public Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        deck.createFullDeck();
    }

    @DisplayName("testing deck cards not empty ")
    @Test
    void testCreateFullDeck() {
        assertNotNull(deck.getCards());
        assertFalse(deck.getCards().isEmpty());
    }

    @DisplayName("testing deck cards size")
    @Test
    void testSize() {
        assertEquals(50, deck.getCards().size());


    }

    @DisplayName("test if cards is of type ArrayList and has elements of type Card")
    @Test
    void testElementTypeOfdeck() {
        assertThat(deck.getCards().get(5), isA(Card.class));
        assertThat(deck.getCards(), isA(ArrayList.class));
    }

    ArrayList<String> playerOneDeck;
    ArrayList<String> playerTwoDeck;

    @BeforeEach
    void setupInstance() {
        playerOneDeck = new ArrayList<String>();
        playerTwoDeck = new ArrayList<String>();

        playerOneDeck.add("playerOneDeck Card nr1");
        playerOneDeck.add("playerOneDeck Card nr2");
        playerTwoDeck.add("playerTwoDeck Card nr3");
        playerTwoDeck.add("playerTwoDeck Card nr4");
    }

    @DisplayName("Add,Check if Contains And not Empty")
    @Test
    void add() {

        assertTrue(playerOneDeck.add("playerOneDeck Card nr1"));
        assertFalse(playerOneDeck.contains("Epic Card"));
        assertNotNull(playerOneDeck);
        assertFalse(playerOneDeck.isEmpty());

        assertTrue(playerTwoDeck.add("playerTwoDeck Card nr2"));
        assertFalse(playerTwoDeck.contains("Epic Card"));
        assertNotNull(playerTwoDeck);
        assertFalse(playerTwoDeck.isEmpty());
    }

    @DisplayName("Check Size")
    @Test
    void checkSize() {
        assertThat(playerOneDeck.size(), is(equalTo(2)));
        assertThat(playerTwoDeck.size(), is(equalTo(2)));
    }

    @DisplayName("Check If Player 1 Contains")
    @Test
    void doesP1Contain() {
        assertTrue(playerOneDeck.contains("playerOneDeck Card nr1"));
    }

    @DisplayName("Check If Player 2 Contains")
    @Test
    void doesP2Contain() {
        assertTrue(playerTwoDeck.contains("playerTwoDeck Card nr3"));
    }

    @DisplayName("Check If Player 1 Not Contains")
    @Test
    void doesP1NotContain() {
        assertFalse(playerOneDeck.contains("Epic Card"));
    }

    @DisplayName("Check If Player 2 Not Contains")
    @Test
    void doesP2NotContain() {
        assertFalse(playerTwoDeck.contains("Epic Card"));
    }

}