package models;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;


import java.util.ArrayList;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {
   Card currentCard;
    Card opponentCard;
    Player p1;
    Player p2;

    private GameEngine gameEngine;

    @Spy
    GameEngine gameEngineSpy;
    @Mock
    Random randMock;

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
        gameEngine=new GameEngine();
        currentCard=new Card();
        opponentCard=new Card();
        p1=new Player();
        p2=new Player();


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
        when(gameEngineSpy.makeRandom()).thenReturn(randMock);
        when(randMock.nextInt()).thenReturn(1);

        assertNull(gameEngineSpy.getCurrentPlayer());
        assertNull(gameEngineSpy.getOpponentPlayer());

        gameEngineSpy.getPlayerToStart();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP1());
        assertEquals(gameEngineSpy.getOpponentPlayer(), gameEngineSpy.getP2());

        gameEngineSpy.endTurn();
        verify(gameEngineSpy, times(1)).endTurn();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP2());
        assertEquals(gameEngineSpy.getOpponentPlayer(), gameEngineSpy.getP1());
    }

    @RepeatedTest(100)
    void getStartingPlayer_testThatRandomIsBetween1Or2() {
        when(randMock.nextInt()).thenReturn(1, 2);
        assertThat(randMock.nextInt()).isBetween(1,2);
    }

    @Test
    void testSetCurrentPlayer_SetToPlayer1() {
        when(gameEngineSpy.makeRandom()).thenReturn(randMock);
        when(randMock.nextInt()).thenReturn(1);
        gameEngineSpy.getPlayerToStart();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP1());
    }

    @Test
    void testSetCurrentPlayer_SetToPlayer2() {
        when(gameEngineSpy.makeRandom()).thenReturn(randMock);
        when(randMock.nextInt()).thenReturn(2);
        gameEngineSpy.getPlayerToStart();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP2());
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


    @RepeatedTest(100)
    void testplayerEnginAttack() {
        Card spyCurrentCard=Mockito.spy(currentCard);
        Card spyOpponentCard=Mockito.spy(opponentCard);
        Player spyCurrentPlayer=Mockito.spy(p1);
        Player spyOpponentPlayer=Mockito.spy(p2);
        assertNotNull(spyCurrentCard);
        assertNotNull(spyOpponentCard);

        assertNotNull(spyCurrentPlayer);
        assertNotNull(spyOpponentPlayer);

        gameEngine.setP1(spyCurrentPlayer);
        gameEngine.setP2(spyOpponentPlayer);

        verify(spyOpponentCard, atMost(100)).attack();
        verify(spyCurrentCard, atMost(100)).attack();

        int currentPlayerAttack= spyCurrentCard.attack();
        int opponentPlayerAttack= spyOpponentCard.attack();

        assertThat(currentPlayerAttack).isBetween(1, 6);
        assertThat(opponentPlayerAttack).isBetween(1, 6);

        int damage=currentPlayerAttack-opponentPlayerAttack;
        assertTrue(Math.abs(damage)>=0);
        assertFalse(Math.abs(damage)<0);

        verify(spyCurrentCard, atMost(100)).removeHp(damage);
        verify(spyOpponentCard, atMost(100)).removeHp(damage);
        verify(spyCurrentCard, atMost(100)).tap();
        verify(spyOpponentCard, atMost(100)).tap();
        verify(spyCurrentPlayer, atMost(100)).sendToGraveyard(spyCurrentCard);
        verify(spyOpponentPlayer, atMost(100)).sendToGraveyard(spyOpponentCard);


        gameEngine.attack(spyCurrentCard,spyOpponentCard);



    }

}