package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public int size();

    // create
    public void insertAuthData(AuthData a) throws DataAccessException;

    // read
    public AuthData getAuthData(String authToken) throws DataAccessException;

    // update

    // delete
    public void deleteAuthData(String authToken) throws DataAccessException;
    public void deleteAllAuthData() throws DataAccessException;
}
