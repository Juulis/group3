package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {

    private GameEngine gameEngine;

    @Mock
    Player p1Mock, p2Mock;
    @Mock
    ArrayList<Card> gameCardsMock;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine();
    }

    @Test
    void firstRound() {
        assertTrue(gameEngine.firstRoundCheck());
        gameEngine.startGame();
        assertTrue(gameEngine.firstRoundCheck());
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