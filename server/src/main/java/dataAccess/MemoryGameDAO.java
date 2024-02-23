package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Integer, GameData> storage = new HashMap<Integer, GameData>();

    public int size() {
        return storage.size();
    }

    public void insertGame(GameData game) throws DataAccessException {
        if (storage.containsKey(game.gameID())) throw new DataAccessException("Error: gameID already associated with a game");
        storage.put(game.gameID(), game);
    }

    public String getWhiteUsername(int gameID) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        return storage.get(gameID).whiteUsername();
    }

    public String getBlackUsername(int gameID) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        return storage.get(gameID).blackUsername();
    }

    public String getGameName(int gameID) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        return storage.get(gameID).gameName();
    }

    public ChessGame getGame(int gameID) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        return storage.get(gameID).game();
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        if (storage.isEmpty()) throw new DataAccessException("Error: No games in memory");
        return new ArrayList<GameData>(storage.values());
    }

    public void setWhiteUsername(int gameID, String whiteUsername) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        if (storage.get(gameID).whiteUsername() != null) throw new DataAccessException("Error: white is already taken");
        GameData gameToUpdate = storage.get(gameID);
        GameData updatedGame = new GameData(gameID, whiteUsername, gameToUpdate.blackUsername(), gameToUpdate.gameName(), gameToUpdate.game());
        storage.put(gameID, updatedGame);
    }

    public void setBlackUsername(int gameID, String blackUsername) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        if (storage.get(gameID).blackUsername() != null) throw new DataAccessException("Error: black is already taken");
        GameData gameToUpdate = storage.get(gameID);
        GameData updatedGame = new GameData(gameID, gameToUpdate.whiteUsername(), blackUsername, gameToUpdate.gameName(), gameToUpdate.game());
        storage.put(gameID, updatedGame);
    }

    public void setGameName(int gameID, String gameName) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        GameData gameToUpdate = storage.get(gameID);
        GameData updatedGame = new GameData(gameID, gameToUpdate.whiteUsername(), gameToUpdate.blackUsername(), gameName, gameToUpdate.game());
        storage.put(gameID, updatedGame);
    }

    public void updateChessGame(int gameID, ChessGame game) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        GameData gameToUpdate = storage.get(gameID);
        GameData updatedGame = new GameData(gameID, gameToUpdate.whiteUsername(), gameToUpdate.blackUsername(), gameToUpdate.gameName(), game);
        storage.put(gameID, updatedGame);
    }

    public void deleteGame(int gameID) throws DataAccessException {
        if (!storage.containsKey(gameID)) throw new DataAccessException("Error: No game found with gameID");
        storage.remove(gameID);
    }

    public void deleteAllGames() throws DataAccessException {
        if (storage.isEmpty()) throw new DataAccessException("Error: No games to delete");
        storage.clear();
    }
}
