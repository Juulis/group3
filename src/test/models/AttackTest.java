package models;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class AttackTest {

    Attack attack;
    Player player;

    @Before

    void setUp(){
        attack = new Attack();
        player = new Player();
    }

    @Test

    void playerAttackTest(){

        player.getHealth();
        System.out.println(player.getHealth());
        assertEquals(20,player.getHealth());
        attack.attackPlayer();
        assertEquals(15,player.getHealth());

    }


}
