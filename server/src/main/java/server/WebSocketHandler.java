package server;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import javax.websocket.*;

public class WebSocketHandler {
    @OnWebSocketConnect
    public void onConnect(Session session) {

    }

    @OnWebSocketClose
    public void onClose(Session session) {

    }

    @OnWebSocketError
    public void onError(Throwable throwable) {

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String string) {
        // 1. Determine message type
        // 2. Call the appropriate method to process the message

    }

    private void sendMessage(int gameID, String message, String authToken) {

    }

    private void broadcastMessage(int gameID, String message, String exceptThisAuthToken) {

    }
}
