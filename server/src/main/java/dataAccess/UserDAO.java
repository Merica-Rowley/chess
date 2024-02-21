package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {
    // create
    void insertUser(UserData user) throws DataAccessException;

    // read
    UserData getUser(String username) throws DataAccessException;
    String getPassword(String username) throws DataAccessException;

    // update

    // delete
    void deleteUser(String username) throws DataAccessException;
    void deleteAllUsers() throws DataAccessException;
}
