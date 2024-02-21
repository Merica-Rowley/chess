package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {
    // create
    void insertUser(UserData user) throws DataAccessException;

    // read
    String getUser(String email) throws DataAccessException;
    String getPassword(String username) throws DataAccessException;
    // update

    // delete
    void deleteUser() throws DataAccessException;
}
