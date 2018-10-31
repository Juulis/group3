package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import static org.mockito.Mockito.*;

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
    ArrayList<Card> gameCardsMock;
    @Mock
    Card cardMock;



    @BeforeEach
    void setUp() {
        gameEngine=new GameEngine();
        currentCard=new Card();
        opponentCard=new Card();
        p1=new Player();
        p2=new Player();

    }

    @Test
    void initPlayer() {

        gameEngine.setP1(p1Mock);
        gameEngine.setP2(p2Mock);
        assertNotNull(gameEngine.getP1());
        assertNotNull(gameEngine.getP2());
        gameEngine.setGameCards(gameCardsMock);
        gameEngine.initPlayer();
        verify(p1Mock, times(1)).setCurrentDeck(gameCardsMock);
        verify(p2Mock, times(1)).setCurrentDeck(gameCardsMock);
        verify(p1Mock, times(5)).pickupCard();
        verify(p2Mock, times(5)).pickupCard();
    }

    @Test
    void isCardKilled() {

        when(cardMock.getHp()).thenReturn(0).thenReturn(2);
        assertTrue(gameEngine.isCardKilled(cardMock));
        assertFalse(gameEngine.isCardKilled(cardMock));
    }


    @RepeatedTest(100)
    void testplayerEnginAttack() {
        Card spyCurrentCard=Mockito.spy(currentCard);
        Card spyOpponentCard=Mockito.spy(opponentCard);
        Player spyCurrentPlayer=Mockito.spy(p1);
        Player spyOpponentPlayer=Mockito.spy(p2);
        assertNotNull(spyCurrentCard);
        assertNotNull(spyOpponentCard);

        assertNotNull(spyOpponentPlayer);
        assertNotNull(spyOpponentPlayer);

        gameEngine.setP1(spyCurrentPlayer);
        gameEngine.setP2(spyCurrentPlayer);

        verify(spyCurrentCard, atMost(100)).attack();
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


        gameEngine.gameEnginAttack(spyCurrentCard,spyOpponentCard);



    }

}