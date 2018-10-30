import models.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    private Player player;

    @Mock
    Card cardMock;

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

    @Test
    void sendToGraveyard() {

        player.getTableCards().add(cardMock);
        int size=player.getTableCards().size();

        assertTrue(player.getTableCards().contains(cardMock));

        player.sendToGraveyard(cardMock);

        assertEquals(size-1, player.getTableCards().size());
        assertFalse(player.getTableCards().contains(cardMock));
    }
}