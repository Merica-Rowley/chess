package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.*;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions sessions;

    @OnWebSocketConnect
    public void onConnect(Session session) {}

    @OnWebSocketClose
    public void onClose(Session session) {}

    @OnWebSocketError
    public void onError(Throwable throwable) {}

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        // 1. Determine message type
        // 2. Call the appropriate method to process the message
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> this.joinPlayer(message);
            case JOIN_OBSERVER -> this.joinObserver(message);
            case MAKE_MOVE -> this.makeMove(message);
            case LEAVE -> this.leave(message);
            case RESIGN -> this.resign(message);
        }
    }

    private void joinPlayer(String message) {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, JoinPlayer.class);
    }

    private void joinObserver(String message) {
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, JoinObserver.class);
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


    private void sendMessage(int gameID, String message, String authToken) {

    }

    private void broadcastMessage(int gameID, String message, String exceptThisAuthToken) {

    }
}
