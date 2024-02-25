package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    public int size();

    // create
    public void insertAuthData(AuthData a) throws DataAccessException;

    // read
    public AuthData getAuthData(String authToken) throws DataAccessException;

    // delete
    public void deleteAuthData(String authToken) throws DataAccessException;
    public void deleteAllAuthData() throws DataAccessException;
}
