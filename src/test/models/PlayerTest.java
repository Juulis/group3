package models;

import models.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    private Player player;
    Player p1 = mock(Player.class);
    ArrayList<Card> mockHandList;
    ArrayList<Card> mockHandList2;
    ArrayList<Card> mockTableList;
    GameEngine gameEngine;

    @Mock
    Card cardMock;

    @BeforeEach
    void setup() {

        player = new Player();
        mockHandList = new ArrayList<Card>(Arrays.asList(new Card(), new Card(), new Card(), new Card()));
        mockHandList2 = new ArrayList<Card>();
        mockTableList = new ArrayList<Card>();
        mockTableList.add(mockHandList.get(1));
        gameEngine = new GameEngine();
    }

    @Test
    void playCard() {
        int playCardNr = 1;

        when(p1.getPlayerHand()).thenReturn(mockHandList).thenReturn(mockHandList).thenReturn(mockHandList2);
        when(p1.getTableCards()).thenReturn(mockTableList);

        Card playCard = (Card) p1.getPlayerHand().get(playCardNr);

        p1.playCard(playCardNr);

        assertEquals(4, p1.getPlayerHand().size());
        assertEquals(1, p1.getTableCards().size());
        assertFalse(p1.getPlayerHand().contains(playCard));
        assertTrue(p1.getTableCards().contains(playCard));
    }

    @Test
    void pickupCard() {

        player.getCurrentDeck().add(new Card());

        int cdSize = player.getCurrentDeck().size();
        int phSize = player.getPlayerHand().size();

        player.pickupCard();

        assertEquals(cdSize - 1, player.getCurrentDeck().size());
        assertEquals(phSize + 1, player.getPlayerHand().size());
    }

    @Test
    void sendToGraveyard() {

        player.getTableCards().add(cardMock);
        int size = player.getTableCards().size();

        assertTrue(player.getTableCards().contains(cardMock));

        player.sendToGraveyard(cardMock);

        assertEquals(size - 1, player.getTableCards().size());
        assertFalse(player.getTableCards().contains(cardMock));
    }

    @DisplayName("testing remove player health ")
    @Test
    void testRemovePlayerHealth() {
        int healthToremove = 2;
        int expected = player.getHealth() - healthToremove;
        player.removeHp(2);
        assertEquals(expected, player.getHealth());

    }

}