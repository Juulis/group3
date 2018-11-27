package utilities;

import models.Card;
import models.Highscore;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreHandlerTest {

    private ScoreHandler scoreHandler;

    @Mock
    Player pMock;

    @Mock
    ArrayList<Card> pDeckMock, pHandMock, pTableMock;

    @BeforeEach
    void setUp() {
        scoreHandler = new ScoreHandler();
    }

    @Test
    void isScoreAHighscore() {

        int min = scoreHandler.readHighscoresFromJSON()[scoreHandler.readHighscoresFromJSON().length-1].getScore();
        when(pMock.getScore()).thenReturn(min-1).thenReturn(min+1);
        assertFalse(scoreHandler.isScoreAHighscore(pMock));
        assertTrue(scoreHandler.isScoreAHighscore(pMock));
    }

    @Test
    void saveHighscore() {
        Highscore[] highscores = scoreHandler.readHighscoresFromJSON();
        int minScore = highscores[highscores.length-1].getScore();
        when(pMock.getName()).thenReturn("Peter");
        when(pMock.getScore()).thenReturn(minScore + 1);
        scoreHandler.saveHighscore(pMock);
        highscores = scoreHandler.readHighscoresFromJSON();
        int newMinScore = highscores[highscores.length-1].getScore();
        assertTrue( pMock.getScore() >= newMinScore);
        assertTrue(newMinScore >= minScore);
    }

    @Test
    void setPlayerScore() {
        when(pMock.getCurrentDeck()).thenReturn(pDeckMock);
        when(pMock.getPlayerHand()).thenReturn(pHandMock);
        when(pMock.getTableCards()).thenReturn(pTableMock);
        scoreHandler.setPlayerScore(pMock);
        verify(pMock,times(1)).getHealth();
        verify(pMock,times(1)).getCurrentDeck();
        verify(pDeckMock, times(1)).size();
        verify(pMock,times(1)).getPlayerHand();
        verify(pHandMock, times(1)).size();
        verify(pMock,times(1)).getTableCards();
        verify(pTableMock,times(1)).size();
        verify(pMock,times(1)).setScore(anyInt());
    }
}