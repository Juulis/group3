import models.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
    /*
    * Repeats the test the test 50 times to check if player
    * gets a new card on each round*/
    @RepeatedTest(50)
    void pickUpOnEachRoundTest(){

        player.getCurrentDeck().add(new Card());

        int playerHand = player.getPlayerHand().size();


        player.pickupCard();

        assertEquals(playerHand+1,player.getPlayerHand().size());
    }
}