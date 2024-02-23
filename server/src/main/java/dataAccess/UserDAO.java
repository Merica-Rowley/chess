package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {
    public int size();

    // create
    public void insertUser(UserData user) throws DataAccessException, UserAlreadyExistsException;

    // read
    public UserData getUser(String username) throws DataAccessException;

    // update

    // delete
    public void deleteUser(String username) throws DataAccessException;
    public void deleteAllUsers() throws DataAccessException;
}
