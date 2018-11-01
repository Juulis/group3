package models;

import models.*;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card card = new Card();
    Random random;
    @BeforeEach
    void setUp() {
      random=new Random();
    }

    @RepeatedTest(1000)
    void randomizeHealthPoint() {
        assertThat(card.randomizeHp()).isBetween(1, 20);
    }

    @Test
    void removeHealthPoints() {
        int currentHp = card.getHp();
        int hpToRemove = 10;
        card.removeHp(hpToRemove);
        int expected = currentHp - hpToRemove;
        assertEquals(expected, card.getHp());
    }
    @DisplayName("testing attack method returning values between 1-6 ")
    @RepeatedTest(1000)
    void testAttack() {

        assertThat(card.attack()).isBetween(1, 6);

    }
}