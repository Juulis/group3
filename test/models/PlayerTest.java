package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    @Mock
    Player p1 = new Player();

    ArrayList<Card> mockHandList;
    ArrayList<Card> mockTableList;

    @BeforeEach
    void setup(){
        mockHandList = new ArrayList<>(Arrays.asList(new Card(), new Card(), new Card(), new Card(),new Card()));
        when(p1.getPlayerHand()).thenReturn(mockHandList);
        mockTableList = new ArrayList<>();
        when(p1.getTableCards()).thenReturn(mockTableList);
    }

    @Test
    void playCard() {
        int playCardNr = 1;
        Card playCard = (Card)p1.getPlayerHand().get(playCardNr);

        p1.playCard(playCardNr);

        assertEquals(4, p1.getPlayerHand().size());
        assertEquals(1, p1.getTableCards().size());
        assertFalse(p1.getPlayerHand().contains(playCard));
        assertTrue(p1.getTableCards().contains(playCard));
    }
}