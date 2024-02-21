package dataAccess;

import model.AuthData;

import java.util.Map;

public interface AuthDAO {
    void insertAuthData(AuthData a) throws DataAccessException;
    String getUser() throws DataAccessException;
    String getAuthToken() throws DataAccessException;
    void deleteAuthData() throws DataAccessException;
}
