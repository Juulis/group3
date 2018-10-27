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
        card.setHp(20);
        int hpToRemove = 10;
        int expected = card.getHp() - hpToRemove;
        assertEquals(expected, 10, "test with 20 hp and 10 dmg");

        card.setHp(10);
        int expected2 = card.getHp() - hpToRemove;
        assertEquals(expected2, 0, "test with 10 hp and 10 dmg");
    }
}