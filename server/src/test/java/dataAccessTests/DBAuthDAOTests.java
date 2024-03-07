package dataAccessTests;

import dataAccess.*;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import model.AuthData;
import org.junit.jupiter.api.*;

public class DBAuthDAOTests {
    private static DBAuthDAO dao;

    static {
        dao = new DBAuthDAO();
    }

    @BeforeEach
    public void clearAuthTable() throws DataAccessException {
        dao.deleteAllAuthData();
    }

    @AfterAll
    public static void resetAuthTable() throws DataAccessException {
        dao.deleteAllAuthData();
    }

    @Test
    void positiveInsertAuthData() throws DataAccessException {
        AuthData testAuthData = new AuthData("test-auth-token", "username");
        dao.insertAuthData(testAuthData);
        Assertions.assertEquals(testAuthData, dao.getAuthData(testAuthData.authToken()));
    }

    @Test
    void negativeInsertAuthData() throws DataAccessException {
        AuthData testAuthData = new AuthData("test-auth-token", "username");
        dao.insertAuthData(testAuthData);
        Assertions.assertThrows(DataAccessException.class, () -> dao.insertAuthData(testAuthData));
    }

    @Test
    void positiveGetAuthData() throws DataAccessException {
        AuthData testAuthData = new AuthData("test-auth-token", "username");
        dao.insertAuthData(testAuthData);
        Assertions.assertEquals(testAuthData.username(), dao.getAuthData(testAuthData.authToken()).username());
    }

    @Test
    void negativeGetAuthData() throws DataAccessException {
        Assertions.assertNull(dao.getAuthData("some-auth-token")); // If getAuthData returns null, then the authToken is not in the table
    }

    @Test
    void positiveDeleteAuthData() throws DataAccessException {
        // Insert some data
        AuthData testAuthData = new AuthData("test-auth-token", "username");
        dao.insertAuthData(testAuthData);

        // Remove the data
        dao.deleteAuthData(testAuthData.authToken());

        Assertions.assertNull(dao.getAuthData("test-auth-token"));
    }

    @Test
    void negativeDeleteAuthData() throws DataAccessException {
        Assertions.assertThrows(NotLoggedInException.class, () -> dao.deleteAuthData("a-fake-auth-token"));
    }

    @Test
    void deleteAllAuthData() throws DataAccessException {
        // Insert some data
        AuthData testAuthData = new AuthData("test-auth-token", "username");
        AuthData anotherTestAuthData = new AuthData("other-auth-token", "username2");
        dao.insertAuthData(testAuthData);
        dao.insertAuthData(anotherTestAuthData);

        Assertions.assertEquals(testAuthData, dao.getAuthData("test-auth-token"));
        Assertions.assertEquals(anotherTestAuthData, dao.getAuthData("other-auth-token"));

        dao.deleteAllAuthData();

        Assertions.assertNull(dao.getAuthData("test-auth-token"));
        Assertions.assertNull(dao.getAuthData("other-auth-token"));
    }
}
