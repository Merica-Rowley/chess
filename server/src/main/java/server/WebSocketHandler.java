package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DBGameDAO;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NoGameFoundException;
import dataAccess.Exceptions.TeamTakenException;
import dataAccess.Exceptions.UserNotFoundException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import webSocketMessages.serverMessages.Error;
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
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> this.joinPlayer(session, message);
            case JOIN_OBSERVER -> this.joinObserver(session, message);
            case MAKE_MOVE -> this.makeMove(message);
            case LEAVE -> this.leave(message);
            case RESIGN -> this.resign(message);
        }
    }

    private void joinPlayer(Session session, String message) {
        Gson gson = new Gson();
        JoinPlayer command = gson.fromJson(message, JoinPlayer.class);
        try {
            GameData currentGameData = gameDAO.getGameData(command.getGameID());

            if (currentGameData == null) {
                throw new NoGameFoundException("Error: No game found");
            }

            sessions.addSessionToGame(command.getGameID(), command.getAuthString(), session);

            String userName = this.getUsername(command.getGameID(), command.getAuthString());

            if (command.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                if (!Objects.equals(currentGameData.whiteUsername(), userName)) {
                    throw new TeamTakenException("Error: Team already taken");
                }
            } else if (command.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                if (!Objects.equals(currentGameData.blackUsername(), userName)) {
                    throw new TeamTakenException("Error: Team already taken");
                }
            }

            String sendThis = gson.toJson(new LoadGame(currentGameData.game()));
            this.sendMessage(session, command.getGameID(), sendThis, command.getAuthString());
            String broadcastThis = gson.toJson(new Notification(format(userName + " joined the game as " + command.getPlayerColor() + "\n")));
            broadcastMessage(command.getGameID(), broadcastThis, command.getAuthString());

        } catch (DataAccessException e) {
            String errorObjectString = gson.toJson(new Error(e.getMessage()));
            this.sendMessage(session, command.getGameID(), errorObjectString, command.getAuthString());
        }
    }

    private void joinObserver(Session session, String message) {
        Gson gson = new Gson();
        JoinObserver command = gson.fromJson(message, JoinObserver.class);
        try {
            GameData currentGameData = gameDAO.getGameData(command.getGameID());

            if (currentGameData == null) {
                throw new NoGameFoundException("Error: No game found");
            }

            sessions.addSessionToGame(command.getGameID(), command.getAuthString(), session);

            String userName = this.getUsername(command.getGameID(), command.getAuthString());

            String sendThis = gson.toJson(new LoadGame(currentGameData.game()));
            this.sendMessage(session, command.getGameID(), sendThis, command.getAuthString());
            String broadcastThis = gson.toJson(new Notification(format(userName + " joined the game as an observer\n")));
            broadcastMessage(command.getGameID(), broadcastThis, command.getAuthString());
        } catch (DataAccessException e) {
            String errorObjectString = gson.toJson(new Error(e.getMessage()));
            this.sendMessage(session, command.getGameID(), errorObjectString, command.getAuthString());
        }
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

    private String getUsername(int gameID, String authToken) {
        Map<AuthData, Session> sessionsList = sessions.getSessionsForGame(gameID);
        String userName = null;
        if (sessionsList != null) {
            for (var connection : sessionsList.keySet()) {
                if (Objects.equals(connection.authToken(), authToken)) {
                    userName = connection.username();
                }
            }
        }
        return userName;
    }

    private void sendMessage(Session session, int gameID, String message, String authToken) {
        try {
            assert session != null;
            session.getRemote().sendString(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcastMessage(int gameID, String message, String exceptThisAuthToken) {
        Map<AuthData, Session> sessionsList = sessions.getSessionsForGame(gameID);
        Session session = null;
        for (var connection : sessionsList.keySet()) {
            if (!Objects.equals(connection.authToken(), exceptThisAuthToken)) {
                session = sessionsList.get(connection);
                try {
                    assert session != null;
                    if (session.isOpen()) {
                        session.getRemote().sendString(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
