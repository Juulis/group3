package models;

import org.junit.Before;
import org.junit.Test;

import static com.sun.javaws.JnlpxArgs.verify;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.*;
import org.mockito.Mock;

public class AttackTest {

    Attack attack;
    Player player;
    Card card;
    GameEngine gameEngine;
    CreatureCard creatureCard;


      /*  @BeforeEach doesn't work!!!
      * */

    @BeforeEach
    void setUp() {
        //attack = new Attack();
        //player = new Player();

    }

    @Test
    public void playerAttackTest() {
        Player player1 = new Player();

        creatureCard = new CreatureCard(5, 5, "Beater", "Attack Player", 1, 1, 15);
        int p1 = player1.getHealth();
        attack = new Attack();
        player = new Player();

        assertEquals(20, p1);

        attack.attackPlayer(creatureCard,player1);


        assertEquals(15,player1.getHealth());

    }


}
