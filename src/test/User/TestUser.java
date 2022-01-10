package User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.company.User;

public class TestUser {
    private final User user = new User("Test");
    @Test
    void test_islogged(){
        assertFalse(user.isLogged());
    }
}
