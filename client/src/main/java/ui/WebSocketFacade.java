package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;

public class WebSocketFacade extends Endpoint{
    public Session session;
    GameHandler gameHandler;

    public WebSocketFacade(int port, GameHandler gameHandler) throws DeploymentException, IOException, URISyntaxException {
        this.gameHandler = gameHandler;

        URI uri = new URI(format("ws://localhost:" + port + "/connect"));
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            // Process incoming messages
            // 1. Deserialize the message
            // 2. Call gameHandler to process message
            public void onMessage(String message) {
                gameHandler.printMessage(message);
            }
        });

    }

    public void connect() {

    }

    public void disconnect() {

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
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {
        super.onError(session, thr);
    }
}
