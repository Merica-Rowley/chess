package dataAccess;

import chess.ChessGame;
import chess.ChessPiece;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    public HashMap<String, UserData> storage = new HashMap<String, UserData>();

    public void insertUser(UserData user) throws DataAccessException {
        storage.put(user.username(), user);
    }

    public UserData getUser(String username) throws DataAccessException {
        return storage.get(username);
    }

    public String getPassword(String username) throws DataAccessException {
        return storage.get(username).password();
    }

    public void deleteUser(String username) throws DataAccessException {
        storage.remove(username);
    }

    public void deleteAllUsers() throws DataAccessException {
        storage.clear();
    }
}
