package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NoGameFoundException;
import dataAccess.Exceptions.TeamTakenException;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DBGameDAO implements GameDAO {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
              `gameID` INT NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    DAOHelper helper = new DAOHelper(createStatements);

    public int insertGame(GameData game) throws DataAccessException {
        helper.configureDatabase();
        var statement = "INSERT INTO GameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        String gameName = game.gameName();
        game.game().getBoard().resetBoard(); // Sets newly created game to fresh board
        String gameJson = new Gson().toJson(game.game());
        return helper.executeUpdate(statement, whiteUsername, blackUsername, gameName, gameJson);
    }

    public GameData getGameData(int gameID) throws DataAccessException {
        helper.configureDatabase();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM GameData WHERE gameID=?";
            var ps = conn.prepareStatement(statement);
            ps.setInt(1, gameID);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return readGame(rs);
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }

        return null;
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        helper.configureDatabase();
        var gameList = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM GameData";
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            while (rs.next()) {
                gameList.add(readGame(rs));
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return gameList;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int newGameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
        return new GameData(newGameID, whiteUsername, blackUsername, gameName, game);
    }

    public void setWhiteUsername(int gameID, String whiteUsername) throws DataAccessException {
        helper.configureDatabase();
        if (this.getGameData(gameID) == null) throw new NoGameFoundException("Error: No game found with gameID");
        if (whiteUsername != null && this.getGameData(gameID).whiteUsername() != null) throw new TeamTakenException("Error: white is already taken");
        var statement = "UPDATE GameData SET whiteUsername=? WHERE gameID=?";
        helper.executeUpdate(statement, whiteUsername, gameID);
    }

    public void setBlackUsername(int gameID, String blackUsername) throws DataAccessException {
        helper.configureDatabase();
        if (this.getGameData(gameID) == null) throw new NoGameFoundException("Error: No game found with gameID");
        if (blackUsername != null && this.getGameData(gameID).blackUsername() != null) throw new TeamTakenException("Error: black is already taken");
        var statement = "UPDATE GameData SET blackUsername=? WHERE gameID=?";
        helper.executeUpdate(statement, blackUsername, gameID);
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        helper.configureDatabase();
        if (this.getGameData(gameID) == null) throw new NoGameFoundException("Error: No game found with gameID");
        String gameJson = new Gson().toJson(game);
        var statement = "UPDATE GameData SET game=? WHERE gameID=?";
        helper.executeUpdate(statement, gameJson, gameID);
    }

    public void deleteAllGames() throws DataAccessException {
        helper.configureDatabase();
        var statement = "TRUNCATE GameData";
        helper.executeUpdate(statement);
    }
}
