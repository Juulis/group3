package java;
import models.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


class DeckTest {
    public models.Deck deck;

    @BeforeEach
    void setUp() {
        deck=new models.Deck();
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
        assertEquals(50,deck.getCards().size());


    }
    @DisplayName("test if cards is of type ArrayList and has elements of type Card")
    @Test
    void testElementTypeOfdeck(){
        assertThat(deck.getCards().get(5),isA(Card.class));
        assertThat(deck.getCards(),isA(ArrayList.class));
    }

}