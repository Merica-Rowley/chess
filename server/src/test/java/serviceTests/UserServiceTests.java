package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

public class UserServiceTests {

    @Test
    void register() throws DataAccessException {
        UserDAO testUserDAO = new MemoryUserDAO();
        AuthDAO testAuthDAO = new MemoryAuthDAO();

        UserService service = new UserService(testUserDAO, testAuthDAO);
        AuthData authData = service.registerUser("user1", "pass123", "qwerty@mail.com");

        // Tests insertion in UserDAO
        String expectedPassword = "pass123";
        String actualPassword = testUserDAO.getUser("user1").password();

        // Tests insertion in AuthDAO
        String expectedUser = "user1";
        String actualUser = testAuthDAO.getAuthData(authData.authToken()).username();

        Assertions.assertEquals(expectedPassword, actualPassword);
        Assertions.assertEquals(expectedUser,actualUser);
;    }

    @Test
    void registerUserAlreadyExists() throws DataAccessException {
        UserDAO testUserDAO = new MemoryUserDAO();
        AuthDAO testAuthDAO = new MemoryAuthDAO();

        UserService service = new UserService(testUserDAO, testAuthDAO);

        try {
            service.registerUser("user1", "pass123", "qwerty@mail.com");
            Assertions.assertThrows(UserAlreadyExistsException.class, () -> service.registerUser("user1", "pass456", "yuiop@mail.com"));
        } catch (UserAlreadyExistsException ignored) {
        }
    }

    @Test
    void login() throws DataAccessException {
        UserDAO testUserDAO = new MemoryUserDAO();
        AuthDAO testAuthDAO = new MemoryAuthDAO();

        testUserDAO.insertUser(new UserData("testuser", "greatpassword", "someemail@mail.com"));
        UserService service = new UserService(testUserDAO, testAuthDAO);

        try {
            AuthData authData = service.loginUser("testuser", "greatpassword");
            Assertions.assertEquals("testuser", testAuthDAO.getAuthData(authData.authToken()).username());
        } catch(IncorrectPasswordException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    void loginIncorrectPassword() throws DataAccessException {
        UserDAO testUserDAO = new MemoryUserDAO();
        AuthDAO testAuthDAO = new MemoryAuthDAO();

        testUserDAO.insertUser(new UserData("user21", "password", "myemail@mail.com"));
        UserService service = new UserService(testUserDAO, testAuthDAO);

        Assertions.assertThrows(IncorrectPasswordException.class, () -> service.loginUser("user21", "notthepassword"));
    }
}
