

import models.*;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card card = new Card();

    @RepeatedTest(1000)
    void randomizeHealthPoint() {
        assertThat(card.randomizeHp()).isBetween(1, 20);
    }

    @RepeatedTest(1000)
    void removeHealthPoints() {
        int currentHp = card.getHp();
        int hpToRemove = 10;
        card.removeHp(hpToRemove);
        int expected = currentHp - hpToRemove;
        assertEquals(expected, card.getHp());
    }
}