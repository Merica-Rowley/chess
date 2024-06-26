package webSocketMessages.serverMessages;

import chess.ChessGame;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.LOAD_GAME;

public class LoadGame extends ServerMessage {
    final private ChessGame game;

    public LoadGame(ChessGame game) {
        super(LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
