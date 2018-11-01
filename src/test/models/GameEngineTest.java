import models.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;


import java.util.ArrayList;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {

    private GameEngine gameEngine;

    @Mock
    Player p1Mock, p2Mock;
    @Mock
    ArrayList<Card> playerOneDeckMock, playerTwoDeckMock;
    @Mock
    Card cardMock;
    @Mock
    Deck deckMock;
    int currentPlayer;
    @Mock
    ArrayList<Card> p1TableCardsMock, p2TableCardsMock;

    @BeforeEach
    void setUp() {

        gameEngine = new GameEngine();
        Random rand = new Random();
        currentPlayer = rand.nextInt(2) + 1;
    }

    @Test
    void initPlayer() {

        gameEngine.setP1(p1Mock);
        gameEngine.setP2(p2Mock);
        gameEngine.setDeck(deckMock);
        assertNotNull(gameEngine.getP1());
        assertNotNull(gameEngine.getP2());

        when(deckMock.getPlayerOneDeck()).thenReturn(playerOneDeckMock);
        when(deckMock.getPlayerTwoDeck()).thenReturn(playerTwoDeckMock);

        gameEngine.initPlayer();

        verify(deckMock,times(1)).playerDeck();
        verify(deckMock, times(1)).getPlayerOneDeck();
        verify(deckMock, times(1)).getPlayerTwoDeck();
        verify(p1Mock, times(1)).setCurrentDeck(playerOneDeckMock);
        verify(p2Mock, times(1)).setCurrentDeck(playerTwoDeckMock);
        verify(p1Mock, times(5)).pickupCard();
        verify(p2Mock, times(5)).pickupCard();
    }

    @Test
    void isCardKilled() {

        when(cardMock.getHp()).thenReturn(0).thenReturn(2);
        assertTrue(gameEngine.isCardKilled(cardMock));
        assertFalse(gameEngine.isCardKilled(cardMock));
    }

    @Test
    void endTurn_testThatCurrentPlayerTogglesAfterEachRound() {
        gameEngine.getPlayerToStart(); // Set player one to start
        assertEquals(gameEngine.getP1(), gameEngine.getCurrentPlayer());
        assertEquals(gameEngine.getP2(), gameEngine.getOpponentPlayer());

        gameEngine.endTurn();
        assertEquals(gameEngine.getP2(), gameEngine.getCurrentPlayer());
        assertEquals(gameEngine.getP1(), gameEngine.getOpponentPlayer());

        gameEngine.endTurn();
        assertEquals(gameEngine.getP1(), gameEngine.getCurrentPlayer());
        assertEquals(gameEngine.getP2(), gameEngine.getOpponentPlayer());

        gameEngine.endTurn();
        assertEquals(gameEngine.getP2(), gameEngine.getCurrentPlayer());
        assertEquals(gameEngine.getP1(), gameEngine.getOpponentPlayer());
    }

    @RepeatedTest(1000)
    void getStartingPlayer_testThatRandomIsBetween1Or2() {
        assertThat(currentPlayer).isBetween(1, 2);
    }

    @Test
    void testSetCurrentPlayerSetToPlayer1() {
        gameEngine.getPlayerToStart();
        assertEquals(gameEngine.getP1(), gameEngine.getCurrentPlayer());
    }

    @Test
    void testSetCurrentPlayerSetToPlayer2() {
        gameEngine.getPlayerToStart();
        assertEquals(gameEngine.getP2(), gameEngine.getCurrentPlayer());
    }

    @Test
    void showTable() {

        int size1=2;
        int size2=3;
        gameEngine.setP1(p1Mock);
        gameEngine.setP2(p2Mock);
        when(p1Mock.getTableCards()).thenReturn(p1TableCardsMock);
        when(p2Mock.getTableCards()).thenReturn(p2TableCardsMock);
        when(p1TableCardsMock.size()).thenReturn(size1);
        when(p2TableCardsMock.size()).thenReturn(size2);
        when(p1TableCardsMock.get(anyInt())).thenReturn(cardMock);
        when(p2TableCardsMock.get(anyInt())).thenReturn(cardMock);
        gameEngine.showTable();
        verify(p1Mock, times(1)).getTableCards();
        verify(p2Mock, times(1)).getTableCards();
        verify(p1TableCardsMock, times(size1)).get(anyInt());
        verify(p2TableCardsMock, times(size2)).get(anyInt());
        verify(cardMock, times(size1+size2)).getHp();

    }
}