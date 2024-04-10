package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import org.glassfish.grizzly.http.Note;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;

public class WebSocketFacade extends Endpoint{
    public Session session;
    private final int port;
    GameHandler gameHandler;

    public WebSocketFacade(int port, GameHandler gameHandler) throws DeploymentException, URISyntaxException, IOException {
        this.port = port;
        this.gameHandler = gameHandler;
    }

    public void connect() throws URISyntaxException, DeploymentException, IOException {
        URI uri = new URI(format("ws://localhost:" + this.port + "/connect"));
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            // Process incoming messages
            // 1. Deserialize the message
            // 2. Call gameHandler to process message
            @Override
            @OnMessage
            public void onMessage(String message) {
                Gson gson = new Gson();
                ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case NOTIFICATION -> {
                        Notification notification = gson.fromJson(message, Notification.class);
                        gameHandler.printMessage(notification.getMessage());
                    }
                    case ERROR -> {
                        Error error = gson.fromJson(message, Error.class);
                        gameHandler.printMessage(error.getErrorMessage());
                    }
                    case LOAD_GAME -> {
                        LoadGame loadGame = gson.fromJson(message, LoadGame.class);
                        gameHandler.updateGame(loadGame.getGame());
                    }
                }
            }
        });
    }

    public void disconnect() throws IOException {
        this.session.close();
    }

    // Outgoing messages
    // 1. Create command message
    // 2. Send message to server
    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor team) throws IOException {
        this.sendMessage(new JoinPlayer(authToken, gameID, team));
    }

    public void joinObserver(String authToken, int gameID) throws IOException {
        this.sendMessage(new JoinObserver(authToken, gameID));
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        this.sendMessage(new MakeMove(authToken, gameID, move));
    }

    public void leaveGame(String authToken, int gameID) throws IOException {
        this.sendMessage(new Leave(authToken, gameID));
    }

    public void resignGame(String authToken, int gameID) throws IOException {
        this.sendMessage(new Resign(authToken, gameID));
    }

    private void sendMessage(UserGameCommand command) throws IOException {
        String message = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(message);
    }

    @Override
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
//
//    @Override
//    @OnClose
//    public void onClose(Session session, CloseReason closeReason) {
//        super.onClose(session, closeReason);
//    }
//
//    @Override
//    @OnError
//    public void onError(Session session, Throwable thr) {
//        super.onError(session, thr);
//    }
}
