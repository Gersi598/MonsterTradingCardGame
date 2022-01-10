package PostgreSQL;
import com.company.PostgreSQL;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.company.User;

public class TestPostgreSQL {
    private final PostgreSQL postgre = new PostgreSQL();
    private final User user = new User("Gersi");
    @Test
    void test_isLogged(){
        assertTrue(postgre.isLogged("gersi"));
    }

    @Test
    void test_registerUser(){
        assertEquals(0, postgre.registerUser(user));
    }

    @Test
    void test_loginUser(){
        assertEquals(1,postgre.logInUser(user));
    }

    @Test
    void test_decreaseCoins(){
        assertEquals(1,postgre.decreaseCoinsFromUser(user.getUsername()));
    }


}
