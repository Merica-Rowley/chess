package dataAccess;

import chess.ChessGame;
import dataAccess.Exceptions.DataAccessException;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    // create
    public int insertGame(GameData game) throws DataAccessException;

    // read
    public GameData getGameData(int gameID) throws DataAccessException;
    public ArrayList<GameData> listGames() throws DataAccessException;

    // update
    public void setWhiteUsername(int gameID, String whiteUsername) throws DataAccessException;
    public void setBlackUsername(int gameID, String blackUsername) throws DataAccessException;
    public void updateGame(int gameID, ChessGame game) throws DataAccessException;

    // delete
    public void deleteAllGames() throws DataAccessException;
}
