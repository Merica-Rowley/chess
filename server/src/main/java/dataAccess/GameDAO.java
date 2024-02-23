package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public interface GameDAO {
    public int getNextID();
    public int size();

    // create
    public void insertGame(GameData game) throws DataAccessException;

    // read
    public GameData getGameData(int gameID) throws DataAccessException;
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
