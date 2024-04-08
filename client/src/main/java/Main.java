import chess.*;
import server.Server;
import ui.UIREPL;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(0);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        UIREPL repl = null;
        try {
            repl = new UIREPL(port);
            repl.run();
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}