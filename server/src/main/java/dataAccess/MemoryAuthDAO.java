package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, AuthData> storage = new HashMap<String, AuthData>();

    public void insertAuthData(AuthData authData) throws DataAccessException {
        if (storage.containsKey(authData.authToken())) throw new DataAccessException("Error: authToken already associated with user");
        storage.put(authData.authToken(), authData);
    }

    public String getUser(String authToken) throws DataAccessException {
        if (!storage.containsKey(authToken)) throw new DataAccessException("Error: No user found with authToken");
        return storage.get(authToken).username();
    }

    public void deleteAuthData(String authToken) throws DataAccessException {
        if (!storage.containsKey(authToken)) throw new DataAccessException("Error: No user found with authToken");
        storage.remove(authToken);
    }

    public void deleteAllAuthData() throws DataAccessException {
        if (storage.isEmpty()) throw new DataAccessException("Error: No AuthData to delete");
        storage.clear();
    }
}
