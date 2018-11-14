package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {
    CreatureCard currentCard;
    CreatureCard opponentCard;
    MagicCard magicCard;
    MagicCard magicCard2;
    CreatureCard currentCardDualAttack;
    CreatureCard opponentCardOne;
    CreatureCard opponentCardTwo;
    Player p1;
    Player p2;
    Attack attack;
    private GameEngine gameEngine;
    CreatureCard spyCurrentCard;
    CreatureCard spyOpponentCard;

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

    @Mock
    ArrayList<Card> currentTableCardsMock, currentHandCardsMock, opponentTableCardsMock, opponentHandCardsMock;
    @Mock
    CreatureCard creatureCardMock;

    @Spy
    ArrayList<Card> gameCardsSpy = spy(new ArrayList<Card>());

    @Spy
    Player players = spy(new Player());
    @Mock
    Attack mockAttack;
    @Spy
    Attack spyAttacks = spy(new Attack());
    CreatureCard spyCreature;

    @BeforeEach
    void setUp() throws IOException {
        attack = new Attack();
        gameEngine = new GameEngine();
        currentCard = new CreatureCard(1, 3, 2, "c4", "basic", 3, 3, 2, "");
        opponentCard = new CreatureCard(1, 2, 2, "c3", "basic", 2, 3, 1, "");
        magicCard = new MagicCard(1, 5, 2, "TROLL", "playerAttack", "");
        magicCard2 = new MagicCard(1, 3, 1, "FISK", "basic", "");
        currentCardDualAttack = new CreatureCard(1, 3, 2, "c6", "dualAttack", 6, 3, 2, "");
        opponentCardOne = new CreatureCard(1, 2, 2, "c7", "basic", 2, 3, 1, "");
        opponentCardTwo = new CreatureCard(1, 2, 2, "c8", "basic", 2, 3, 1, "");
        p1 = new Player();
        p2 = new Player();
        spyCreature = spy(opponentCard);
        spyCurrentCard = Mockito.spy(currentCard);
        spyOpponentCard = Mockito.spy(opponentCard);
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
        gameCardsSpy.add(new Card(1, 3, 2, "c4", "basic", ""));
        assertThat(gameCardsSpy.size(), is(equalTo(1)));
        assertNotNull(gameCardsSpy);
    }

    @DisplayName("If player Hp is null")
    @Test
    void checkPlayerHpIsNull() {
        assertNotNull(players);
        assertThat(players.getHealth(), is(equalTo(20)));
    }

    @DisplayName("If player Hp is not null")
    @Test
    void checkPlayerHpNotNull() {
        assertThat(players.getHealth(), is(equalTo(20)));
        assertEquals(20, players.getHealth());
    }

    @Test
    void isCardKilled() {

        when(creatureCardMock.getHp()).thenReturn(0).thenReturn(2);
        assertTrue(gameEngine.isCardKilled(creatureCardMock));
        assertFalse(gameEngine.isCardKilled(creatureCardMock));
    }

    @RepeatedTest(100)
    void endTurn_testThatCurrentPlayerTogglesAfterEachRound() throws IOException {
        when(gameEngineSpy.makeRandom()).thenReturn(randMock);
        when(randMock.nextBoolean()).thenReturn(true); // Sets currentPlayer to P1
        gameEngineSpy.determinePlayerToStart();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP1());
        assertEquals(gameEngineSpy.getOpponentPlayer(), gameEngineSpy.getP2());

        gameEngineSpy.getCurrentPlayer().getCurrentDeck().add(new Card(1, 3, 2, "c4", "basic", ""));
        gameEngineSpy.getOpponentPlayer().getCurrentDeck().add(new Card(1, 3, 1, "c5", "basic", ""));
        gameEngineSpy.endTurn();

        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP2());
        assertEquals(gameEngineSpy.getOpponentPlayer(), gameEngineSpy.getP1());

        gameEngineSpy.endTurn();
        assertEquals(gameEngineSpy.getCurrentPlayer(), gameEngineSpy.getP1());
        assertEquals(gameEngineSpy.getOpponentPlayer(), gameEngineSpy.getP2());
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
        when(currentTableCardsMock.get(anyInt())).thenReturn(creatureCardMock);
        when(currentHandCardsMock.get(anyInt())).thenReturn(creatureCardMock);
        when(opponentTableCardsMock.get(anyInt())).thenReturn(creatureCardMock);
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
        verify(creatureCardMock, times(size1 + size2 + size3)).getHp();

    }

    @DisplayName("test switch Attack method")
    @Test
    void testSwitchAttack() {
        Card card = new Card(1, 1, 1, "CArd", "Ignite", "");
        doNothing().when(spyAttacks).ignite(currentCard, opponentCard);
        doNothing().when(spyAttacks).attackPlayer(currentCard, gameEngine.getOpponentPlayer());
        doNothing().when(spyAttacks).ignite(card, spyCreature);
        doNothing().when(spyAttacks).attackPlayer(currentCard, gameEngine.getOpponentPlayer());
        doNothing().when(spyAttacks).basicAttack(currentCard, opponentCard);
        doNothing().when(spyAttacks).dualAttack(currentCard, opponentCardOne, opponentCardTwo);
        doNothing().when(spyAttacks).attackAll(currentCard, opponentTableCardsMock);
        //assertThat(attack, isA(Attack.class));


    }

    /**
     * @throws IOException After attacking opponents full tableCard list
     *                     check if all cards lost correct amount hp
     *                     <p>
     *                     repeated because of the random cards on table
     */
    @DisplayName("test attackAll method")
    @RepeatedTest(30)
    void testAttackAll() throws IOException {
        //prep game
        gameEngine.initGame();
        Player opponentPlayer = gameEngine.getOpponentPlayer();
        currentCard.setAttack(1);
        //fill tableCard list
        for (int i = 0; i < 5; i++) {
            opponentPlayer.playCard(gameEngine.getDeck().getCards().get(i), 2);
        }

        //get hp from cards before attack
        ArrayList<Card> cards = opponentPlayer.getTableCards();
        List<Integer> hp = new ArrayList<>();
        for (Card card : cards) {
            CreatureCard cCard = (CreatureCard) card;
            if (cCard.getHp() > 2) {
                hp.add(cCard.getHp());
            }
        }

        attack.attackAll(currentCard, opponentPlayer.getTableCards());

        //check hp from cards after attack
        if (!cards.isEmpty()) {
            for (int i = 0; i < cards.size(); i++) {
                CreatureCard cCard = (CreatureCard) cards.get(i);
                assertEquals(hp.get(i) - 1, cCard.getHp());
            }
        }
    }

    @DisplayName("test dualAttack method")
    @Test
    void testDualAttack() {
        assertFalse(currentCardDualAttack.isTapped());
        assertThat(currentCard.getSpecialAttack().equals("dualAttack"));

        // Test where currentCardDualAttack doesn't get killed during dualAttack()...
        attack.dualAttack(currentCardDualAttack, opponentCardOne, opponentCardTwo);

        assertTrue(gameEngine.isCardKilled(opponentCardOne));
        assertFalse(gameEngine.isCardKilled(currentCardDualAttack));
        assertTrue(gameEngine.isCardKilled(opponentCardTwo));

        // Test where currentCardDualAttack is killed during dualAttack()...
        currentCardDualAttack = new CreatureCard(1, 3, 2, "c6", "dualAttack", 2, 3, 2, "");
        opponentCardOne = new CreatureCard(1, 2, 2, "c7", "basic", 2, 3, 1, "");
        opponentCardTwo = new CreatureCard(1, 12, 2, "c8", "basic", 2, 3, 1, "");

        attack.dualAttack(currentCardDualAttack, opponentCardOne, opponentCardTwo);

        assertTrue(gameEngine.isCardKilled(opponentCardOne));
        assertTrue(gameEngine.isCardKilled(currentCardDualAttack));
        assertFalse(gameEngine.isCardKilled(opponentCardTwo));
    }

    @Test
    void name() {
        assertThat(GameEngine.AttackNames.valueOf("PLAYERATTACK"), is(notNullValue()));
        assertThat(GameEngine.AttackNames.valueOf("DUALATTACK"), is(notNullValue()));
        assertThat(GameEngine.AttackNames.valueOf("IGNITE"), is(notNullValue()));
        assertThat(GameEngine.AttackNames.valueOf("ATTACKALL"), is(notNullValue()));
        assertThat(GameEngine.AttackNames.valueOf("BASIC"), is(notNullValue()));


    }

    @Test
    void newTurnNewCard() throws IOException {
        gameEngine.getPlayerToStart(true);
        Player player1 = gameEngine.getCurrentPlayer();
        player1.getCurrentDeck().add(new CreatureCard(1, 1, 2, "c1", "basic", 2, 3, 2, ""));
        player1.getCurrentDeck().add(new CreatureCard(1, 2, 2, "c2", "basic", 1, 3, 3, ""));
        player1.getCurrentDeck().add(new CreatureCard(1, 2, 3, "c3", "basic", 3, 1, 2, ""));
        player1.getCurrentDeck().add(new CreatureCard(1, 3, 2, "c4", "basic", 2, 2, 3, ""));
        player1.pickupCard();
        player1.pickupCard();

        assertEquals(2, player1.getPlayerHand().size());
        player1.playCard(player1.getPlayerHand().get(0), 1);
        assertEquals(1, player1.getPlayerHand().size());

        gameEngine.getPlayerToStart(false);
        Player player2 = gameEngine.getCurrentPlayer();
        player2.getCurrentDeck().add(new CreatureCard(1, 1, 2, "c1", "basic", 1, 2, 1, ""));
        player2.getCurrentDeck().add(new CreatureCard(1, 2, 2, "c2", "basic", 2, 2, 2, ""));
        player2.getCurrentDeck().add(new CreatureCard(1, 2, 3, "c3", "basic", 3, 2, 1, ""));
        player2.getCurrentDeck().add(new CreatureCard(1, 3, 2, "c4", "basic", 2, 1, 3, ""));
        player2.pickupCard();
        player2.pickupCard();

        assertEquals(2, player2.getPlayerHand().size());
        player2.playCard(player2.getPlayerHand().get(0), 1);
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
    void testPlayerEngineAttack() {
        CreatureCard spyCurrentCard = Mockito.spy(currentCard);
        CreatureCard spyOpponentCard = Mockito.spy(opponentCard);

        Player spyCurrentPlayer = Mockito.spy(p1);
        Player spyOpponentPlayer = Mockito.spy(p2);
        assertNotNull(spyCurrentCard);
        assertNotNull(spyOpponentCard);

        assertNotNull(spyCurrentPlayer);
        assertNotNull(spyOpponentPlayer);

        gameEngine.setP1(spyCurrentPlayer);
        gameEngine.setP2(spyOpponentPlayer);

        verify(spyOpponentCard, atMost(100)).getAttack();
        verify(spyCurrentCard, atMost(100)).getAttack();

        int currentPlayerAttack = spyCurrentCard.getAttack();
        int opponentPlayerAttack = spyOpponentCard.getAttack();

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

        boolean expected = false;
        boolean actual = gameEngine.isCardKilled(spyCurrentCard);
        assertEquals(expected, actual);

    }

    @DisplayName("opponent player attack is null")
    @RepeatedTest(100)
    public void whenExceptionThrown_thenAssertionSucceeds() {

        assertThrows(NullPointerException.class, () -> {
            opponentCard = null;
            int opponentPlayerAttack = opponentCard.getAttack();
        });
    }

    @RepeatedTest(100)
    void testPlayerHealthAfterAttack() {
        Player spyCurrentPlayer = Mockito.spy(p1);
        Player spyOpponentPlayer = Mockito.spy(p2);
        verify(spyCurrentPlayer, atMost(100)).getHealth();
        verify(spyOpponentPlayer, atMost(100)).getHealth();
        int expected = 18;
        int actual = spyCurrentPlayer.getHealth() - 2;
        assertEquals(expected, actual);
    }

    @Test
    void checkIfTapped() {
        gameEngine.getPlayerToStart(true);
        Player player1 = gameEngine.getCurrentPlayer();
        player1.getCurrentDeck().add(new CreatureCard(1, 1, 2, "c1", "basic", 2, 3, 2, ""));
        player1.getCurrentDeck().add(new CreatureCard(1, 2, 2, "c2", "basic", 1, 3, 3, ""));
        player1.getCurrentDeck().add(new CreatureCard(1, 2, 3, "c3", "basic", 3, 1, 2 ,""));
        player1.getCurrentDeck().add(new CreatureCard(1, 3, 2, "c4", "basic", 2, 2, 3, ""));
        player1.pickupCard();
        player1.pickupCard();

        assertEquals(2, player1.getPlayerHand().size());
        gameEngine.getPlayerToStart(false);
        Player player2 = gameEngine.getCurrentPlayer();
        player2.getCurrentDeck().add(new CreatureCard(1, 1, 2, "c1", "basic", 1, 2, 1, ""));
        player2.getCurrentDeck().add(new CreatureCard(1, 2, 2, "c2", "basic", 2, 2, 2, ""));
        player2.getCurrentDeck().add(new CreatureCard(1, 2, 3, "c3", "basic", 3, 2, 1, ""));
        player2.getCurrentDeck().add(new CreatureCard(1, 3, 2, "c4", "basic", 2, 1, 3, ""));

        player2.pickupCard();
        player2.pickupCard();

        assertEquals(2, player2.getPlayerHand().size());

        player1.playCard(player1.getPlayerHand().get(1), 1);
        player2.playCard(player2.getPlayerHand().get(1), 1);

        assertEquals(1, player1.getPlayerHand().size());
        assertEquals(1, player2.getPlayerHand().size());
        attack.basicAttack(player1.getTableCards().get(0), (CreatureCard) player2.getTableCards().get(0));

        gameEngine.checkIfTapped((CreatureCard) player1.getTableCards().get(0));

        assertTrue(((CreatureCard) player1.getTableCards().get(0)).isTapped());
    }

    @Test
    void unTap() {

        int size = 2;
        gameEngine.setP1(p1Mock);
        when(p1Mock.getTableCards()).thenReturn(currentTableCardsMock);
        when(currentTableCardsMock.size()).thenReturn(size);
        when(currentTableCardsMock.get(anyInt())).thenReturn(creatureCardMock);
        when(creatureCardMock.isTapped()).thenReturn(true, false, false, true);

        gameEngine.getPlayerToStart(true);
        gameEngine.unTap();

        verify(p1Mock, times(1)).getTableCards();
        verify(currentTableCardsMock, times(size)).get(anyInt());
        verify(creatureCardMock, times(size)).isTapped();
        verify(creatureCardMock, times(1)).unTap();

        gameEngine.unTap();

        verify(creatureCardMock, times(2)).unTap();
    }

    @Test
    void checkCardsLeft() throws IOException {
        gameEngine.setP1(p1Mock);
        gameEngine.setP2(p2Mock);
        when(p1Mock.getCurrentDeck()).thenReturn(playerOneDeckMock);
        when(p2Mock.getCurrentDeck()).thenReturn(playerTwoDeckMock);
        when(p1Mock.getPlayerHand()).thenReturn(currentHandCardsMock);
        when(p2Mock.getPlayerHand()).thenReturn(opponentHandCardsMock);
        when(p1Mock.getTableCards()).thenReturn(currentTableCardsMock);
        when(p2Mock.getTableCards()).thenReturn(opponentTableCardsMock);

        gameEngine.checkCardsLeft();

        verify(p1Mock, times(2)).getCurrentDeck();
        verify(p2Mock, times(2)).getCurrentDeck();
        verify(p1Mock, times(2)).getPlayerHand();
        verify(p2Mock, times(2)).getPlayerHand();
        verify(p1Mock, times(2)).getTableCards();
        verify(p2Mock, times(2)).getTableCards();
        verify(playerOneDeckMock, times(2)).size();
        verify(playerTwoDeckMock, times(2)).size();
        verify(currentHandCardsMock, times(2)).size();
        verify(opponentHandCardsMock, times(2)).size();
        verify(currentTableCardsMock, times(2)).size();
        verify(opponentTableCardsMock, times(2)).size();

    }

    @Test
    void isCardReadyToAttack() {
        when(creatureCardMock.getPlayedOnRound()).thenReturn(0);
        when(creatureCardMock.getPower()).thenReturn(0).thenReturn(4);
        assertTrue(gameEngine.isCardReadyToAttack(creatureCardMock));
        assertFalse(gameEngine.isCardReadyToAttack(creatureCardMock));
    }

    @Test
    void testCallIgniteMethod() {

        verify(spyAttacks, atMost(2)).ignite(currentCard, opponentCard);

    }

    @Test
    void testIncreaseIgnCounter() {
        assertNotNull(spyCurrentCard);
        assertNotNull(opponentCard);
        verify(spyCreature, atMost(2)).increaseIgnRoundCounter();
        verify(spyCreature, atMost(2)).getHp();
        verify(spyCreature, atMost(2)).removeHp(2);
        verify(spyCreature, atMost(2)).getIgnRoundCounter();

    }
    @Test
    void whenMagicCardIsUsedSendToGrave() {
        assertThat(magicCard).isInstanceOfAny(MagicCard.class);
        p1.getPlayerHand().add(magicCard);
        assertEquals(1, p1.getPlayerHand().size());
        p1.playCard(p1.getPlayerHand().get(p1.getPlayerHand().indexOf(magicCard)), 4);
        p1.sendToGraveyard(magicCard);
        assertEquals(0, p1.getPlayerHand().size());
    }

    }

