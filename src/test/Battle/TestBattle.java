package Battle;
import com.company.Battle;
import com.company.PostgreSQL;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.company.Deck;
import com.company.Card;

public class TestBattle {
    public final Battle battle = new Battle();
    Deck deck1 = new Deck();
    Deck deck2 = new Deck();
    @Test
    void test_readyToBattle(){
        assertFalse(battle.readyToBattle());
    }

    Card card1= new Card(11,"Fire",15);
    Card card2 = new Card(10,"Water",20);
    @Test
    void test_compare_cards(){
        assertEquals(2,battle.compareCards(card1,2,card2,3));

    }

    @Test
    void test_istaken(){
        assertFalse(battle.isTaken(3,1));
    }

    @Test
    void test_isOver(){
        assertFalse(battle.isOver());
    }
}
