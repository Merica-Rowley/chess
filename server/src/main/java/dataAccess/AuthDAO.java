package dataAccess;

import model.AuthData;

public interface AuthDAO {
    // create
    void insertAuthData(AuthData a) throws DataAccessException;

    // read
    String getUser(String authToken) throws DataAccessException;

    // update

    // delete
    void deleteAuthData(String authToken) throws DataAccessException;
    void deleteAllAuthData() throws DataAccessException;
}
