package models;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card card = new Card();

    @RepeatedTest(1000)
    void randomizeHealthPoint() {
        assertThat(card.randomizeHp()).isBetween(1, 20);
    }

    @RepeatedTest(100)
    void removeHealthPoints(){
        card.removeHp(10);
        assertEquals(card.getHp() - 10, card.getHp());
    }
}