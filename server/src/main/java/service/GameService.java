package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.NotLoggedInException;
import model.GameData;

import java.util.ArrayList;


public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        authDAO.getAuthData(authToken); // throws NotLoggedInException if user is not logged in
        return gameDAO.listGames();
    }
}
