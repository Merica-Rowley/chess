package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.Exceptions.TeamTakenException;
import model.AuthData;
import model.GameData;
import model.GameListData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.ArrayList;

public class GameServiceTests {

    @Test
    // Positive Test
    void listGames() throws DataAccessException {
        AuthDAO testAuthDAO = new MemoryAuthDAO();
        GameDAO testGameDAO = new MemoryGameDAO();

        GameService service = new GameService(testAuthDAO, testGameDAO);

        testGameDAO.insertGame(new GameData(1, "white", "black", "mygame", new ChessGame()));
        testGameDAO.insertGame(new GameData(2, "white", null, "myothergame", new ChessGame()));
        testAuthDAO.insertAuthData(new AuthData("fake-auth-token", "user123"));

        ArrayList<GameListData> expected = new ArrayList<GameListData>();
        expected.add(new GameListData(1, "white", "black", "mygame"));
        expected.add(new GameListData(2, "white", null, "myothergame"));

        Assertions.assertEquals(expected, service.listGames("fake-auth-token"));
    }

    @Test
    // Negative Test
    void listGamesWithoutAuthentication() {
        AuthDAO testAuthDAO = new MemoryAuthDAO();
        GameDAO testGameDAO = new MemoryGameDAO();

        GameService service = new GameService(testAuthDAO, testGameDAO);

        Assertions.assertThrows(NotLoggedInException.class, () -> service.listGames("another-fake-auth-token"));
    }

    @Test
    // Positive Test
    void createGame() throws DataAccessException {
        AuthDAO testAuthDAO = new MemoryAuthDAO();
        GameDAO testGameDAO = new MemoryGameDAO();

        GameService service = new GameService(testAuthDAO, testGameDAO);

        testAuthDAO.insertAuthData(new AuthData("a-made-up-auth-token", "username"));

        int generatedGameID = service.createGame("a-made-up-auth-token", "NameOfTheGame");

        Assertions.assertEquals("NameOfTheGame", testGameDAO.getGameData(generatedGameID).gameName());
    }

    @Test
    // Negative Test
    void createGameWithoutAuthentication() {
        AuthDAO testAuthDAO = new MemoryAuthDAO();
        GameDAO testGameDAO = new MemoryGameDAO();

        GameService service = new GameService(testAuthDAO, testGameDAO);

        Assertions.assertThrows(NotLoggedInException.class, () -> service.createGame("auth-token-place-holder", "MyGame"));
    }

    @Test
    // Positive Test
    void joinGame() throws DataAccessException {
        AuthDAO testAuthDAO = new MemoryAuthDAO();
        GameDAO testGameDAO = new MemoryGameDAO();

        GameService service = new GameService(testAuthDAO, testGameDAO);
        String testAuth = "test-auth-token";
        String testUser = "fakeuser";
        testAuthDAO.insertAuthData(new AuthData(testAuth, testUser));
        int gameID = service.createGame(testAuth, "gamename");

        service.joinGame(testAuth, ChessGame.TeamColor.BLACK, gameID);

        Assertions.assertEquals(testUser, testGameDAO.getGameData(gameID).blackUsername());
        Assertions.assertNull(testGameDAO.getGameData(gameID).whiteUsername());
    }

    @Test
    // Negative Test
    void joinGameTeamAlreadyTaken() throws DataAccessException {
        AuthDAO testAuthDAO = new MemoryAuthDAO();
        GameDAO testGameDAO = new MemoryGameDAO();

        GameService service = new GameService(testAuthDAO, testGameDAO);

        String testAuth = "first-test-auth-token";
        String testUser = "ghostuser";
        testAuthDAO.insertAuthData(new AuthData(testAuth, testUser));
        String testAuth2 = "another-test-auth-token";
        String testUser2 = "imaginaryuser";
        testAuthDAO.insertAuthData(new AuthData(testAuth2, testUser2));

        int gameID = service.createGame(testAuth, "othergamename");

        service.joinGame(testAuth, ChessGame.TeamColor.BLACK, gameID);

        Assertions.assertThrows(TeamTakenException.class, () -> service.joinGame(testAuth2, ChessGame.TeamColor.BLACK,gameID));
    }
}
