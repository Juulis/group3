package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatureCardTest {

    private CreatureCard creatureCard;

    @BeforeEach
    void setUp() {

        creatureCard = new CreatureCard(5,2,3,2);
    }

    @Test
    void removeHp() {

        int hp = creatureCard.getHp();
        int dmg = 2;
        creatureCard.removeHp(dmg);
        assertEquals(hp-dmg, creatureCard.getHp());
    }
}