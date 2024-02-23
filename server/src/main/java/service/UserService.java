package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData registerUser(String username, String password, String email) throws DataAccessException {
        userDAO.insertUser(new UserData(username, password, email));
        AuthData authData = this.createAuth(username);
        authDAO.insertAuthData(authData);
        return authData;
    }

    public AuthData loginUser(String username, String password) throws DataAccessException, IncorrectPasswordException {
        UserData userObject = userDAO.getUser(username);
        if (!Objects.equals(userObject.password(), password)) throw new IncorrectPasswordException("Error: incorrect password");
        AuthData authData = this.createAuth(username);
        authDAO.insertAuthData(authData);
        return authData;
    }

    public void logoutUser(String authToken) throws DataAccessException{
        authDAO.deleteAuthData(authToken);
    }
    
    private AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }

}
