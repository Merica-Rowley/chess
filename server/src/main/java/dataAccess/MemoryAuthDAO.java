package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import model.AuthData;

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

    public void deleteAllAuthData() {
        storage.clear();
    }
}
