package models;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;


import java.util.ArrayList;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.failBecauseExceptionWasNotThrown;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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
    ArrayList<Card> currentTableCardsMock, currentHandCardsMock, opponentTableCardsMock;

    @Spy
    ArrayList<Card> gameCardsSpy = spy(new ArrayList<Card>());

    @Spy
    Player players = spy(new Player());

    @BeforeEach
    void setUp() {
        gameEngine=new GameEngine();
        currentCard=new Card();
        opponentCard=new Card();
        p1=new Player();
        p2=new Player();
        gameEngine = new GameEngine();

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

        verify(deckMock, times(1)).playerDeck();
        verify(deckMock, times(1)).getPlayerOneDeck();
        verify(deckMock, times(1)).getPlayerTwoDeck();
        verify(p1Mock, times(1)).setCurrentDeck(playerOneDeckMock);
        verify(p2Mock, times(1)).setCurrentDeck(playerTwoDeckMock);
        verify(p1Mock, times(5)).pickupCard();
        verify(p2Mock, times(5)).pickupCard();
    }

    @DisplayName("If player deck is null")
    @Test
    void checkDeckSizeIsNull() {
        assertThat(gameCardsSpy.size(), is(equalTo(0)));
        assertTrue(gameCardsSpy.isEmpty());
    }

    @DisplayName("If player deck is not null")
    @Test
    void checkDeckSizeNotNull() {
        gameCardsSpy.add(new Card());
        assertThat(gameCardsSpy.size(), is(equalTo(1)));
        assertNotNull(gameCardsSpy);
    }

    @DisplayName("If player Hp is null")
    @Test
    void checkPlayerHpIsNull() {
        assertNotNull(players);
        assertThat(players.getHealth(), is(equalTo(10)));
    }

    @DisplayName("If player Hp is not null")
    @Test
    void checkPlayerHpNotNull() {
        assertThat(players.getHealth(), is(equalTo(10)));
        assertEquals(10, players.getHealth());
    }

    @Test
    void isCardKilled() {

        when(cardMock.getHp()).thenReturn(0).thenReturn(2);
        assertTrue(gameEngine.isCardKilled(cardMock));
        assertFalse(gameEngine.isCardKilled(cardMock));
    }

    @RepeatedTest(100)
    void endTurn_testThatCurrentPlayerTogglesAfterEachRound() {
        when(gameEngineSpy.makeRandom()).thenReturn(randMock);
        when(randMock.nextBoolean()).thenReturn(true); // Sets currentPlayer to P1
        gameEngineSpy.determinePlayerToStart();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP1());
        assertEquals(gameEngineSpy.getOpponentPlayer(), gameEngineSpy.getP2());

        gameEngineSpy.getCurrentPlayer().getCurrentDeck().add(new Card());
        gameEngineSpy.getOpponentPlayer().getCurrentDeck().add(new Card());
        gameEngineSpy.endTurn();

        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP2());
        assertEquals(gameEngineSpy.getOpponentPlayer(), gameEngineSpy.getP1());
    }

    @Test
    void getStartingPlayer_testThatRandomIsTrueOrFalse() {
        when(randMock.nextBoolean()).thenReturn(true).thenReturn(false);
        assertTrue(randMock.nextBoolean());
        assertFalse(randMock.nextBoolean());

    }

    @Test
    void testSetCurrentPlayer_SetToPlayer1() {
        when(gameEngineSpy.makeRandom()).thenReturn(randMock);
        when(randMock.nextBoolean()).thenReturn(true);
        gameEngineSpy.determinePlayerToStart();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP1());
    }

    @Test
    void testSetCurrentPlayer_SetToPlayer2() {
        when(gameEngineSpy.makeRandom()).thenReturn(randMock);
        when(randMock.nextBoolean()).thenReturn(false);
        gameEngineSpy.determinePlayerToStart();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP2());
    }

    @Test
    void showTable() {

        int size1 = 2;
        int size2 = 3;
        int size3 = 4;
        gameEngine.setP1(p1Mock);
        gameEngine.setP2(p2Mock);
        when(p1Mock.getTableCards()).thenReturn(currentTableCardsMock);
        when(p1Mock.getPlayerHand()).thenReturn(currentHandCardsMock);
        when(p2Mock.getTableCards()).thenReturn(opponentTableCardsMock);
        when(currentTableCardsMock.size()).thenReturn(size1);
        when(currentHandCardsMock.size()).thenReturn(size3);
        when(opponentTableCardsMock.size()).thenReturn(size2);
        when(currentTableCardsMock.get(anyInt())).thenReturn(cardMock);
        when(currentHandCardsMock.get(anyInt())).thenReturn(cardMock);
        when(opponentTableCardsMock.get(anyInt())).thenReturn(cardMock);
        gameEngine.getPlayerToStart(true);
        gameEngine.showTable();
        verify(p1Mock, times(1)).getHealth();
        verify(p2Mock, times(1)).getHealth();
        verify(p1Mock, times(1)).getTableCards();
        verify(p1Mock, times(1)).getPlayerHand();
        verify(p2Mock, times(1)).getTableCards();
        verify(currentTableCardsMock, times(size1)).get(anyInt());
        verify(currentHandCardsMock, times(size3)).get(anyInt());
        verify(opponentTableCardsMock, times(size2)).get(anyInt());
        verify(cardMock, times(size1 + size2 + size3)).getHp();

    }




    @Test
    void newTurnNewCard(){
        gameEngine.getPlayerToStart(true);
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

        gameEngine.getPlayerToStart(false);
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
        @RepeatedTest(100)
        void testplayerEnginAttack () {
            Card spyCurrentCard = Mockito.spy(currentCard);
            Card spyOpponentCard = Mockito.spy(opponentCard);
            Player spyCurrentPlayer = Mockito.spy(p1);
            Player spyOpponentPlayer = Mockito.spy(p2);
            assertNotNull(spyCurrentCard);
            assertNotNull(spyOpponentCard);

            assertNotNull(spyCurrentPlayer);
            assertNotNull(spyOpponentPlayer);

            gameEngine.setP1(spyCurrentPlayer);
            gameEngine.setP2(spyOpponentPlayer);

            verify(spyOpponentCard, atMost(100)).attack();
            verify(spyCurrentCard, atMost(100)).attack();

            int currentPlayerAttack = spyCurrentCard.attack();
            int opponentPlayerAttack = spyOpponentCard.attack();

            assertThat(currentPlayerAttack).isBetween(1, 6);
            assertThat(opponentPlayerAttack).isBetween(1, 6);

            int damage = currentPlayerAttack - opponentPlayerAttack;
            assertTrue(Math.abs(damage) >= 0);
            assertFalse(Math.abs(damage) < 0);

            verify(spyCurrentCard, atMost(100)).removeHp(damage);
            verify(spyOpponentCard, atMost(100)).removeHp(damage);
            verify(spyCurrentCard, atMost(100)).tap();
            verify(spyOpponentCard, atMost(100)).tap();
            verify(spyCurrentPlayer, atMost(100)).sendToGraveyard(spyCurrentCard);
            verify(spyOpponentPlayer, atMost(100)).sendToGraveyard(spyOpponentCard);


            gameEngine.attack(spyCurrentCard, spyOpponentCard);

    }

    @Test
    void checkIfTapped(){
        gameEngine.getPlayerToStart(true);
        Player player1 = gameEngine.getCurrentPlayer();
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.pickupCard();
        player1.pickupCard();

        assertEquals(2, player1.getPlayerHand().size());
        gameEngine.getPlayerToStart(false);
        Player player2 = gameEngine.getCurrentPlayer();
        player2.getCurrentDeck().add(new Card());
        player2.getCurrentDeck().add(new Card());
        player2.getCurrentDeck().add(new Card());
        player2.getCurrentDeck().add(new Card());

        player2.pickupCard();
        player2.pickupCard();

        assertEquals(2, player2.getPlayerHand().size());

        player1.playCard(1);
        player2.playCard(1);

        assertEquals(1, player1.getPlayerHand().size());
        assertEquals(1, player2.getPlayerHand().size());
        gameEngine.attack(player1.getTableCards().get(0),player2.getTableCards().get(0));

        gameEngine.checkIfTapped(player1.getTableCards().get(0));

        assertTrue( player1.getTableCards().get(0).getTapped());
    }

    }
