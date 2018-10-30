import models.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {

    private GameEngine gameEngine;

    @Mock
    Player p1Mock;
    @Mock
    Player p2Mock;

    @Mock
    ArrayList<Card> gameCardsMock;
    int currentPlayer;


    @BeforeEach
    void setUp() {

        gameEngine = new GameEngine();
        Random rand = new Random();
        currentPlayer = rand.nextInt(2) + 1;
    }

    @Test
    void endTurn() {

    }

    @RepeatedTest(100)
    void getStartingPlayer_testThatRandomIsBetween1Or2() {
        assertThat(currentPlayer).isBetween(1, 2);
    }

    @Test
    void testSetCurrentPlayerSetToPlayer1() {
        gameEngine.getStartingPlayer(1);
        assertEquals(gameEngine.getP1(), gameEngine.getCurrentPlayer());
    }

    @Test
    void testSetCurrentPlayerSetToPlayer2() {
        gameEngine.getStartingPlayer(2);
        assertEquals(gameEngine.getP2(), gameEngine.getCurrentPlayer());
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
}
