package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public interface GameDAO {
    public int size();

    // create
    public void insertGame(GameData game) throws DataAccessException;

    // read
    public String getWhiteUsername(int gameID) throws DataAccessException;
    public String getBlackUsername(int gameID) throws DataAccessException;
    public String getGameName(int gameID) throws DataAccessException;
    public ChessGame getGame(int gameID) throws DataAccessException;
    public ArrayList<GameData> listGames() throws DataAccessException;

    // update
    public void setWhiteUsername(int gameID, String whiteUsername) throws DataAccessException;
    public void setBlackUsername(int gameID, String blackUsername) throws DataAccessException;
    public void setGameName(int gameID, String gameName) throws DataAccessException;
    public void updateChessGame(int gameID, ChessGame game) throws DataAccessException;

    // delete
    public void deleteGame(int gameID) throws DataAccessException;
    public void deleteAllGames() throws DataAccessException;
}
