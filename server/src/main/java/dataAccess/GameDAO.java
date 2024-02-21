package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public interface GameDAO {
    // create
    void insertGame(GameData game) throws DataAccessException;

    // read
    String getWhiteUsername(int gameID) throws DataAccessException;
    String getBlackUsername(int gameID) throws DataAccessException;
    String getGameName(int gameID) throws DataAccessException;
    ChessGame getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;

    // update
    void setWhiteUsername(int gameID, String whiteUsername) throws DataAccessException;
    void setBlackUsername(int gameID, String blackUsername) throws DataAccessException;
    void setGameName(int gameID, String gameName) throws DataAccessException;
    void updateChessGame(int gameID, ChessGame game) throws DataAccessException;

    // delete
    void deleteGame(int gameID) throws DataAccessException;
    void deleteAllGames() throws DataAccessException;
}
