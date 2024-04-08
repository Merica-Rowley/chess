package server;

import dataAccess.DBAuthDAO;
import dataAccess.Exceptions.DataAccessException;
import model.AuthData;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketSessions {
    Map<Integer, Map<AuthData, Session>> sessionMap = new HashMap<>();
    DBAuthDAO authDAO = new DBAuthDAO();

    public void addSessionToGame(int gameID, String authToken, Session session) throws DataAccessException {
        AuthData authData = authDAO.getAuthData(authToken);
        Map<AuthData, Session> dataMap = new HashMap<>();
        dataMap.put(authData, session);
        sessionMap.put(gameID, dataMap);
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) throws IOException {
        Map<AuthData, Session> gameGroup = sessionMap.get(gameID);
        for (var key : gameGroup.keySet()) {
            if (gameGroup.get(key) == session) {
                gameGroup.get(key).close();
                gameGroup.remove(key);
            }
        }
    }

    public void removeSession(Session session) {
        // TODO: what does this do
    }

    public Map<AuthData, Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
