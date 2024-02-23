package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, AuthData> storage = new HashMap<String, AuthData>();

    public int size() {
        return storage.size();
    }

    public void insertAuthData(AuthData authData) throws DataAccessException {
        if (storage.containsKey(authData.authToken())) throw new DataAccessException("Error: authToken already associated with user");
        storage.put(authData.authToken(), authData);
    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        if (!storage.containsKey(authToken)) throw new NotLoggedInException("Error: authToken not found; user not logged in");
        return storage.get(authToken);
    }

    public void deleteAuthData(String authToken) throws DataAccessException {
        if (!storage.containsKey(authToken)) throw new NotLoggedInException("Error: authToken not found; user not logged in");
        storage.remove(authToken);
    }

    public void deleteAllAuthData() throws DataAccessException {
        if (storage.isEmpty()) throw new DataAccessException("Error: No AuthData to delete");
        storage.clear();
    }
}
