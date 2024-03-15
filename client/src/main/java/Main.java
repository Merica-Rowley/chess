import chess.*;
import server.Server;
import ui.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(0);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade facade = new ServerFacade(port);
        try {
            System.out.println(facade.register("mme", "pass", "email@mail.com"));
            System.out.println(facade.login("mme", "pass"));
            System.out.println(facade.createGame("game3"));
            System.out.println(facade.createGame("game4"));
            System.out.println(facade.listGames());
        } catch (IOException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }
    }
}