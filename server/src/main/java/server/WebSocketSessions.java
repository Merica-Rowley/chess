package server;

import model.AuthData;

import javax.websocket.Session;
import java.util.Map;

public class WebSocketSessions {
    Map<Integer, Map<AuthData, Session>> sessionMap;

    public void addSessionToGame(int gameID, String authToken, Session session) {

    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) {

    }

    public void removeSession(Session session) {

    }

    public Map<AuthData, Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
