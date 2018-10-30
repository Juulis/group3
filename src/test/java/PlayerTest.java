import models.*;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {

        player=new Player();
    }

    @Test
    void pickupCard() {

        player.getCurrentDeck().add(new Card());

        int cdSize=player.getCurrentDeck().size();
        int phSize=player.getPlayerHand().size();

        player.pickupCard();

        assertEquals(cdSize-1, player.getCurrentDeck().size());
        assertEquals(phSize+1, player.getPlayerHand().size());
    }

    @DisplayName("testing remove player health ")
    @Test
    void testRemovePlayerHealth() {
        int healthToremove=2;
        int expected=player.getHealth()-healthToremove;
        player.removeHp(2);
        assertEquals(expected,player.getHealth());

    }


}