package dataAccessTests;

import chess.ChessGame;
import dataAccess.DBGameDAO;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.TeamTakenException;
import model.GameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DBGameDAOTests {
    private static DBGameDAO dao;

    static {
        dao = new DBGameDAO();
    }

    @BeforeEach
    public void clearGameTable() throws DataAccessException {
        dao.deleteAllGames();
    }

    @AfterAll
    public static void resetGameTable() throws DataAccessException {
        dao.deleteAllGames();
    }

    @Test
    void positiveInsertGame() throws DataAccessException {
        GameData testGameData = new GameData(0, null, null, "My Chess Game", new ChessGame());
        int gameID = dao.insertGame(testGameData);
        GameData testGameDataAutoIncrementID = new GameData(gameID, testGameData.whiteUsername(), testGameData.blackUsername(), testGameData.gameName(), testGameData.game());
        Assertions.assertEquals(testGameDataAutoIncrementID.gameName(), dao.getGameData(gameID).gameName());
    }

    @Test
    void negativeInsertGame() throws DataAccessException {
        GameData testGameData = new GameData(0, null, null, "My Chess Game", new ChessGame());
        int gameID = dao.insertGame(testGameData);
        // AutoIncrement should automatically set the gameID; therefore, the gameID should be greater than or equal to 0
        Assertions.assertNotEquals(0, dao.getGameData(gameID));
    }

    @Test
    void positiveGetGameData() throws DataAccessException {
        GameData testGameData = new GameData(0, null, null, "My Chess Game", new ChessGame());
        int gameID = dao.insertGame(testGameData);
        GameData retrievedGameData = dao.getGameData(gameID);
        Assertions.assertEquals(testGameData.whiteUsername(), retrievedGameData.whiteUsername());
        Assertions.assertEquals(testGameData.blackUsername(), retrievedGameData.blackUsername());
        Assertions.assertEquals(testGameData.gameName(), retrievedGameData.gameName());
    }

    @Test
    void negativeGetGameData() throws DataAccessException {
        Assertions.assertNull(dao.getGameData(-1)); // getGameData returns null when game with gameID doesn't exist in the database
    }

    @Test
    void positiveListGames() throws DataAccessException {
        dao.insertGame(new GameData(0, null, null, "My Chess Game", new ChessGame()));
        dao.insertGame(new GameData(0, null, "player21", "My Other Chess Game", new ChessGame()));

        ArrayList<GameData> gameList = new ArrayList<GameData>();
        gameList.add(new GameData(0, null, null, "My Chess Game", new ChessGame()));
        gameList.add(new GameData(0, null, "player21", "My Other Chess Game", new ChessGame()));

        ArrayList<GameData> retrievedGameList = dao.listGames();

        Assertions.assertNotEquals(gameList.get(0).gameID(), retrievedGameList.get(1).gameID());
        Assertions.assertEquals(gameList.get(0).whiteUsername(), retrievedGameList.get(0).whiteUsername());
        Assertions.assertEquals(gameList.get(1).blackUsername(), retrievedGameList.get(1).blackUsername());
        Assertions.assertNotEquals(gameList.get(0).gameName(), retrievedGameList.get(1).gameName());
        Assertions.assertEquals(gameList.get(0).game().allValidMoves(ChessGame.TeamColor.BLACK), retrievedGameList.get(0).game().allValidMoves(ChessGame.TeamColor.BLACK));
    }

    @Test
    void negativeListGames() throws DataAccessException {
        ArrayList<GameData> emptyList = new ArrayList<GameData>();
        Assertions.assertEquals(emptyList, dao.listGames());
    }

    @Test
    void positiveSetWhiteUser() throws DataAccessException {
        GameData testGameData = new GameData(0, null, null, "My Chess Game", new ChessGame());
        int gameID = dao.insertGame(testGameData);

        dao.setWhiteUsername(gameID, "whiteUser");
        Assertions.assertEquals("whiteUser", dao.getGameData(gameID).whiteUsername());
    }

    @Test
    void negativeSetWhiteUser() throws DataAccessException {
        GameData testGameData = new GameData(0, "user123", null, "My Chess Game", new ChessGame());
        int gameID = dao.insertGame(testGameData);
        Assertions.assertThrows(TeamTakenException.class, () -> dao.setWhiteUsername(gameID, "user45"));
    }

    @Test
    void positiveSetBlackUser() throws DataAccessException {
        GameData testGameData = new GameData(0, null, null, "My Chess Game", new ChessGame());
        int gameID = dao.insertGame(testGameData);

        dao.setBlackUsername(gameID, "blackUser");
        Assertions.assertEquals("blackUser", dao.getGameData(gameID).blackUsername());
    }

    @Test
    void negativeSetBlackUser() throws DataAccessException {
        GameData testGameData = new GameData(0, null, "user678", "My Chess Game", new ChessGame());
        int gameID = dao.insertGame(testGameData);
        Assertions.assertThrows(TeamTakenException.class, () -> dao.setBlackUsername(gameID, "user90"));
    }

    @Test
    void deleteAllGames() throws DataAccessException {
        // Insert some data
        int firstGameID = dao.insertGame(new GameData(0, null, null, "My Chess Game", new ChessGame()));
        int secondGameID = dao.insertGame(new GameData(0, null, "player21", "My Other Chess Game", new ChessGame()));

        dao.deleteAllGames();

        Assertions.assertNull(dao.getGameData(firstGameID));
        Assertions.assertNull(dao.getGameData(secondGameID));
    }
}
