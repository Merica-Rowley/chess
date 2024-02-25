package dataAccess;

import chess.ChessGame;
import dataAccess.Exceptions.DataAccessException;
import model.GameData;

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

    // delete
    public void deleteAllGames() throws DataAccessException;
}
