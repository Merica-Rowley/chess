import chess.*;
import server.Server;
import ui.UIREPL;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(0);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        UIREPL repl = new UIREPL(port);
        try {
            repl.run();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}