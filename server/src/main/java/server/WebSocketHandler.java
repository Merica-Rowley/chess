package server;

import com.google.gson.Gson;
import dataAccess.DBGameDAO;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

@WebSocket
public class WebSocketHandler {
    public WebSocketSessions sessions;
    private final GameDAO gameDAO = new DBGameDAO();

    WebSocketHandler() {
        this.sessions = new WebSocketSessions();
    }

//    @OnWebSocketConnect
//    public void onConnect(Session session) {}
//
//    @OnWebSocketClose
//    public void onClose(Session session) {}
//
//    @OnWebSocketError
//    public void onError(Throwable throwable) {}

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        // 1. Determine message type
        // 2. Call the appropriate method to process the message
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        try {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> this.joinPlayer(session, message);
                case JOIN_OBSERVER -> this.joinObserver(session, message);
                case MAKE_MOVE -> this.makeMove(message);
                case LEAVE -> this.leave(message);
                case RESIGN -> this.resign(message);
            }
        } catch (DataAccessException e) {
            // TODO: send error message back
        }
    }

    private void joinPlayer(Session session, String message) throws DataAccessException {
        Gson gson = new Gson();
        JoinPlayer command = gson.fromJson(message, JoinPlayer.class);
        sessions.addSessionToGame(command.getGameID(), command.getAuthString(), session);
        String sendThis = gson.toJson(new LoadGame(gameDAO.getGameData(command.getGameID()).game()));
        String userName = this.sendMessage(command.getGameID(), sendThis, command.getAuthString());
        String broadcastThis = gson.toJson(new Notification(format(userName + " joined the game as " + command.getPlayerColor() + "\n")));
        broadcastMessage(command.getGameID(), broadcastThis, command.getAuthString());
    }

    private void joinObserver(Session session, String message) throws DataAccessException {
        Gson gson = new Gson();
        JoinObserver command = gson.fromJson(message, JoinObserver.class);
        sessions.addSessionToGame(command.getGameID(), command.getAuthString(), session);
        String sendThis = gson.toJson(new LoadGame(gameDAO.getGameData(command.getGameID()).game()));
        String userName = this.sendMessage(command.getGameID(), sendThis, command.getAuthString());
        String broadcastThis = gson.toJson(new Notification(format(userName + " joined the game as an observer\n")));
        broadcastMessage(command.getGameID(), broadcastThis, command.getAuthString());
    }

    private void makeMove(String message) {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, MakeMove.class);
    }

    private void leave(String message) {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, Leave.class);
    }

    private void resign(String message) {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, Resign.class);
    }

    private String sendMessage(int gameID, String message, String authToken) {
        Map<AuthData, Session> sessionsList = sessions.getSessionsForGame(gameID);
        Session session = null;
        String userName = null;
        for (var connection : sessionsList.keySet()) {
            if (Objects.equals(connection.authToken(), authToken)) {
                session = sessionsList.get(connection);
                userName = connection.username();
            }
        }
        try {
            assert session != null;
            session.getRemote().sendString(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userName;
    }

    private void broadcastMessage(int gameID, String message, String exceptThisAuthToken) {
        Map<AuthData, Session> sessionsList = sessions.getSessionsForGame(gameID);
        Session session = null;
        for (var connection : sessionsList.keySet()) {
            if (!Objects.equals(connection.authToken(), exceptThisAuthToken)) {
                session = sessionsList.get(connection);
                try {
                    assert session != null;
                    session.getRemote().sendString(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
