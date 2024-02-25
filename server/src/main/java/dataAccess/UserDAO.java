package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UserAlreadyExistsException;
import model.UserData;

public interface UserDAO {
    public int size();

    // create
    public void insertUser(UserData user) throws DataAccessException, UserAlreadyExistsException;

    // read
    public UserData getUser(String username) throws DataAccessException;

    // delete
    public void deleteAllUsers() throws DataAccessException;
}
