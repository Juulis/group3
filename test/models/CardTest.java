package models;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card card = new Card();

    @RepeatedTest(1000)
    void randomizeHealthPoint() {
        int min = 1;
        int max = 20;
        assertThat(card.randomizeHp()).isBetween(min, max);
    }

    @Test
    void removeHealthPoints(){
        int hpToRemove = 10;
        int expected = card.getHp() - hpToRemove;
        assertEquals(expected, card.removeHp(hpToRemove));
    }
}