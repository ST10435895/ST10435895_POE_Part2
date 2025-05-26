import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private Login validUser;
    private Login userWithBadUsername;
    private Login userWithBadPassword;
    private Login userWithBadPhone;

    @BeforeEach
    void setUp() {
        validUser = new Login("Alice", "Smith", "a_s", "P@ssword1", "+27831234567");
        userWithBadUsername = new Login("Bob", "Jones", "bob", "P@ssword1", "+27831234567");
        userWithBadPassword = new Login("Charlie", "Brown", "c_b", "password", "+27831234567");
        userWithBadPhone = new Login("Dana", "White", "d_w", "P@ssword1", "0831234567");
    }

    @Test
    void testCheckUserName_Valid() {
        assertTrue(validUser.checkUserName());
    }

    @Test
    void testCheckUserName_Invalid() {
        assertFalse(userWithBadUsername.checkUserName());
    }

    @Test
    void testCheckPasswordComplexity_Valid() {
        assertTrue(validUser.checkPasswordComplexity());
    }

    @Test
    void testCheckPasswordComplexity_Invalid() {
        assertFalse(userWithBadPassword.checkPasswordComplexity());
    }

    @Test
    void testCheckCellPhoneNumber_Valid() {
        assertTrue(validUser.checkCellPhoneNumber());
    }

    @Test
    void testCheckCellPhoneNumber_Invalid() {
        assertFalse(userWithBadPhone.checkCellPhoneNumber());
    }

    @Test
    void testRegisterUser_Success() {
        assertEquals("Registration successful.", validUser.registerUser());
    }

    @Test
    void testRegisterUser_InvalidUsername() {
        String result = userWithBadUsername.registerUser();
        assertEquals("Username is not correctly formatted. It must contain an underscore and be no more than 5 characters.", result);
    }

    @Test
    void testRegisterUser_InvalidPassword() {
        String result = userWithBadPassword.registerUser();
        assertEquals("Password is not correctly formatted. It must contain at least 8 characters, a capital letter, a number, and a special character.", result);
    }

    @Test
    void testRegisterUser_InvalidPhone() {
        String result = userWithBadPhone.registerUser();
        assertEquals("Cell phone number incorrectly formatted. It must start with +27 and have 9 digits after.", result);
    }

    @Test
    void testLoginUser_Success() {
        assertTrue(validUser.loginUser("a_s", "P@ssword1", "+27831234567"));
    }

    @Test
    void testLoginUser_Failure_WrongUsername() {
        assertFalse(validUser.loginUser("wrong_user", "P@ssword1", "+27831234567"));
    }

    @Test
    void testLoginUser_Failure_WrongPassword() {
        assertFalse(validUser.loginUser("a_s", "WrongPass", "+27831234567"));
    }

    @Test
    void testLoginUser_Failure_WrongPhone() {
        assertFalse(validUser.loginUser("a_s", "P@ssword1", "+27000000000"));
    }
}
