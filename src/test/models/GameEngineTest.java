package models;
import models.Card;
import models.Deck;
import models.GameEngine;
import models.Player;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import models.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.*;
import java.util.Random;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class GameEngineTest {
    Card currentCard;
    Card opponentCard;
    Player p1;
    Player p2;

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
    ArrayList<Card> currentTableCardsMock, currentHandCardsMock, opponentTableCardsMock;

    @Spy
    ArrayList<Card> gameCardsSpy = spy(new ArrayList<Card>());

    @Spy
    Player players = spy(new Player());

    @BeforeEach
    void setUp() {
        currentCard = new Card();
        opponentCard = new Card();
        p1 = new Player();
        p2 = new Player();
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
        gameEngine.getPlayerToStart(1);
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
        verify(cardMock, times(size1 + size2)).getHp();

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
        gameEngine.getPlayerToStart(1);
        Player player1 = gameEngine.getCurrentPlayer();
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.getCurrentDeck().add(new Card());
        player1.pickupCard();
        player1.pickupCard();

        assertEquals(2, player1.getPlayerHand().size());
        gameEngine.getPlayerToStart(2);
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
