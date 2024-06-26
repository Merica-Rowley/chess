package server;

import dataAccess.DBAuthDAO;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UserNotFoundException;
import model.AuthData;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {
    Map<Integer, Map<AuthData, Session>> sessionMap = new ConcurrentHashMap<>();
    private final DBAuthDAO authDAO = new DBAuthDAO();

    public void addSessionToGame(int gameID, String authToken, Session session) throws DataAccessException {
        AuthData authData = authDAO.getAuthData(authToken);

        if (authData == null) {
            throw new UserNotFoundException("Error: bad auth token");
        }

        if (sessionMap.containsKey(gameID)) {
            Map<AuthData, Session> innerMap = sessionMap.get(gameID);
            innerMap.put(authData, session);
        } else {
            Map<AuthData, Session> dataMap = new ConcurrentHashMap<>();
            dataMap.put(authData, session);
            sessionMap.put(gameID, dataMap);
        }
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) throws IOException {
        Map<AuthData, Session> gameGroup = sessionMap.get(gameID);
        for (var key : gameGroup.keySet()) {
            if (gameGroup.get(key) == session) {
                gameGroup.remove(key).close();
            }
        }
    }

    public Map<AuthData, Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
