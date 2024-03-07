package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NoGameFoundException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.GameDAO;
import dataAccess.Exceptions.MissingInformationException;
import model.AuthData;
import model.GameData;
import model.GameListData;

import java.util.ArrayList;


public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ArrayList<GameListData> listGames(String authToken) throws DataAccessException {
        if (authDAO.getAuthData(authToken) == null) throw new NotLoggedInException("Error: authToken not found; user not logged in");
        return gameDataConversion(gameDAO.listGames());
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if (gameName == null) throw new MissingInformationException("Error: no game name");
        if (authDAO.getAuthData(authToken) == null) throw new NotLoggedInException("Error: authToken not found; user not logged in");
        return gameDAO.insertGame(new GameData(0, null, null, gameName, new ChessGame()));
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        AuthData authData = authDAO.getAuthData(authToken);
        if (authData == null) throw new NotLoggedInException("Error: authToken not found; user not logged in");
        String username = authData.username();
        if(gameDAO.getGameData(gameID) == null) throw new NoGameFoundException("Error: No game found with gameID");
        if (playerColor != null) {
            switch (playerColor) {
                case WHITE:
                    gameDAO.setWhiteUsername(gameID, username);
                    break;
                case BLACK:
                    gameDAO.setBlackUsername(gameID, username);
                    break;
            }
        }
    }

    private ArrayList<GameListData> gameDataConversion(ArrayList<GameData> games) {
        ArrayList<GameListData> editedList = new ArrayList<GameListData>();

        for (GameData game : games) {
            editedList.add(new GameListData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }

        return editedList;
    }
}
