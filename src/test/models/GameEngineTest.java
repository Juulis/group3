
package models;
import models.Card;
import models.Deck;
import models.GameEngine;
import models.Player;
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

    @RepeatedTest(1000)
    void endTurn_testThatCurrentPlayerTogglesAfterEachRound() {
        gameEngine.getPlayerToStart(1); // Set player one to start
        assertEquals(gameEngine.getP1(), gameEngine.getCurrentPlayer());

        gameEngine.endTurn();
        assertEquals(gameEngine.getP2(), gameEngine.getCurrentPlayer());

        gameEngine.endTurn();
        assertEquals(gameEngine.getP1(), gameEngine.getCurrentPlayer());

        gameEngine.endTurn();
        assertEquals(gameEngine.getP2(), gameEngine.getCurrentPlayer());
    }

    @RepeatedTest(1000)
    void getStartingPlayer_testThatRandomIsBetween1Or2() {
        assertThat(currentPlayer).isBetween(1, 2);
    }

    @Test
    void testSetCurrentPlayerSetToPlayer1() {
        gameEngine.getPlayerToStart(1);
        assertEquals(gameEngine.getP1(), gameEngine.getCurrentPlayer());
    }

    @Test
    void testSetCurrentPlayerSetToPlayer2() {
        gameEngine.getPlayerToStart(2);
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

    @Test
    void newTurnNewCard(){
        gameEngine.getPlayerToStart(1);
        Player player1 = gameEngine.getCurrentPlayer();
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.pickupCard();
        player1.pickupCard();

        assertEquals(2, player1.getPlayerHand().size());
        player1.playCard(1);
        assertEquals(1, player1.getPlayerHand().size());

        gameEngine.getPlayerToStart(2);
        Player player2 = gameEngine.getCurrentPlayer();
        player2.getCurrentDeck().add(new Card());
        player2.getCurrentDeck().add(new Card());
        player2.getCurrentDeck().add(new Card());
        player2.getCurrentDeck().add(new Card());
        player2.pickupCard();
        player2.pickupCard();

        assertEquals(2, player2.getPlayerHand().size());
        player2.playCard(1);
        assertEquals(1, player2.getPlayerHand().size());

        gameEngine.endTurn();
        player1.pickupCard();

        /*Reason it jumps from 1 card to 3 cards is because endTurn method
         * calls pickUp method. So it picks up on endTurn method and player1.pickupCard();
         * same thing applies to player2*/

        assertEquals(3, player1.getPlayerHand().size());

        gameEngine.endTurn();

        player2.pickupCard();
        assertEquals(3, player2.getPlayerHand().size());

    }

}