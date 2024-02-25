package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MissingInformationException;
import model.AuthData;
import model.GameData;
import model.GameListData;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ArrayList<GameListData> listGames(String authToken) throws DataAccessException {
        authDAO.getAuthData(authToken); // throws NotLoggedInException if user is not logged in
        return gameDataConversion(gameDAO.listGames());
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if (gameName == null) throw new MissingInformationException("Error: no game name");
        authDAO.getAuthData(authToken); // throws NotLoggedInException if user is not logged in
        int gameID = gameDAO.getNextID();
        gameDAO.insertGame(new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        AuthData authData = authDAO.getAuthData(authToken); // throws NotLoggedInException if user is not logged in
        String username = authData.username();
        gameDAO.getGameData(gameID); // throws NoGameFoundException if gameID doesn't correspond to an existing game
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
