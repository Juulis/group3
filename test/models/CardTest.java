package models;

import org.junit.jupiter.api.*;

import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardTest {

    Card card = mock(Card.class);
    Random rand = new Random();

    @RepeatedTest(1000)
    void randomizeHealthPoint() {
        int min = 1;
        int max = 20;
        when(card.randomizeHp()).thenReturn(rand.nextInt(max) + min);
        assertThat(card.randomizeHp()).isBetween(min, max);
    }

    @Test
    void removeHealthPoints(){
        int hpToRemove = 10;
        int expected = card.getHp() - hpToRemove;
        when(card.removeHp(hpToRemove)).thenReturn(expected);
        assertEquals(expected, card.removeHp(hpToRemove));
    }
}