package Deck;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.company.Deck;

public class TestDeck {
    private final Deck deck = new Deck();

    @Test
    void test_checkDeck(){
        assertEquals(1,deck.checkDeck());
    }
}
