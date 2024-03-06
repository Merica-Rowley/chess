package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class DBGameDAO implements GameDAO {
    public int getNextID() {
        return 0;
    }

    public int size() {
        return 0;
    }

    public void insertGame(GameData game) throws DataAccessException {
        configureDatabase();
    }

    public GameData getGameData(int gameID) throws DataAccessException {
        configureDatabase();
        return null;
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        configureDatabase();
        return null;
    }

    public void setWhiteUsername(int gameID, String whiteUsername) throws DataAccessException {
        configureDatabase();
    }

    public void setBlackUsername(int gameID, String blackUsername) throws DataAccessException {
        configureDatabase();
    }

    public void deleteAllGames() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
              `gameID` INT NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
