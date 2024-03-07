package dataAccessTests;

import dataAccess.DBUserDAO;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.MissingInformationException;
import dataAccess.Exceptions.UserNotFoundException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DBUserDAOTests {
    private static DBUserDAO dao;

    static {
        dao = new DBUserDAO();
    }

    @BeforeEach
    public void clearAuthTable() throws DataAccessException {
        dao.deleteAllUsers();
    }

    @AfterAll
    public static void resetAuthTable() throws DataAccessException {
        dao.deleteAllUsers();
    }

    @Test
    void positiveInsertUser() throws DataAccessException {
        UserData testUser = new UserData("user43", "password21", "mail@email.com");
        dao.insertUser(testUser);
        Assertions.assertEquals(testUser, dao.getUser(testUser.username()));
    }

    @Test
    void negativeInsertUser() throws DataAccessException {
        UserData missingPasswordUser = new UserData("someUser", null, "hi@mail.com");
        Assertions.assertThrows(MissingInformationException.class, () -> dao.insertUser(missingPasswordUser));
    }

    @Test
    void positiveGetUser() throws DataAccessException {
        UserData testUser = new UserData("user43", "password21", "mail@email.com");
        dao.insertUser(testUser);
        UserData retrievedUser = dao.getUser(testUser.username());
        Assertions.assertEquals(testUser, retrievedUser);
    }

    @Test
    void negativeGetUser() throws DataAccessException {
        Assertions.assertNull(dao.getUser("randomUsername")); // getUser returns null when username is not in the database
    }

    @Test
    void deleteAllAuthData() throws DataAccessException {
        // Insert some data
        UserData testUser = new UserData("user43", "password21", "mail@email.com");
        UserData secondTestUser = new UserData("user87", "password65", "hola@email.com");
        dao.insertUser(testUser);
        dao.insertUser(secondTestUser);

        dao.deleteAllUsers();

        Assertions.assertNull(dao.getUser(testUser.username()));
        Assertions.assertNull(dao.getUser(secondTestUser.username()));
    }
}
