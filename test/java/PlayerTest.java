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
}