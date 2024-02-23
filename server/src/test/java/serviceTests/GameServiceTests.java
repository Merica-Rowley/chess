package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
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

        ChessGame chessGame1 = new ChessGame();
        ChessGame chessGame2 = new ChessGame();

        testGameDAO.insertGame(new GameData(1, "white", "black", "mygame", chessGame1));
        testGameDAO.insertGame(new GameData(2, "white", null, "myothergame", chessGame2));
        testAuthDAO.insertAuthData(new AuthData("fake-auth-token", "user123"));

        ArrayList<GameData> expected = new ArrayList<GameData>();
        expected.add(new GameData(1, "white", "black", "mygame", chessGame1));
        expected.add(new GameData(2, "white", null, "myothergame", chessGame2));

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
}
