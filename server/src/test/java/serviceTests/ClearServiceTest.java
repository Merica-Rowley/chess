package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import dataAccess.Exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;

public class ClearServiceTest {
    @Test
    void clear() throws DataAccessException {
        UserDAO testUserDAO = new MemoryUserDAO();
        GameDAO testGameDAO = new MemoryGameDAO();
        AuthDAO testAuthDAO = new MemoryAuthDAO();

        // populate DAOs
        testUserDAO.insertUser(new UserData("user1", "password1", "12345@email.com"));
        testUserDAO.insertUser(new UserData("user2", "password2", "67890@email.com"));
        testGameDAO.insertGame(new GameData(1, "white", "black", "gameName", new ChessGame()));
        testGameDAO.insertGame(new GameData(2, "white2", "black2", "gameName2", new ChessGame()));
        testAuthDAO.insertAuthData(new AuthData("abcde", "user3"));
        testAuthDAO.insertAuthData(new AuthData("fghij", "user4"));

        ClearService testClearService = new ClearService(testUserDAO, testGameDAO, testAuthDAO);
        testClearService.clear();

        int actual = ((MemoryUserDAO) testUserDAO).size() + ((MemoryGameDAO) testGameDAO).size() + ((MemoryAuthDAO) testAuthDAO).size();

        Assertions.assertEquals(0, actual);
    }
}
