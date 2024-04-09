package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;

public class UIREPL implements GameHandler {
    private final int port;
    private final ServerFacade facade;

    public UIREPL(int port) throws DeploymentException, IOException, URISyntaxException {
        this.facade = new ServerFacade(port);
        this.port = port;
    }

    public void run() throws URISyntaxException, IOException {
        this.preLogin();
    }

    public void updateGame(ChessGame game) {

    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    private void preLogin() throws URISyntaxException, IOException {
        while (true) {
            System.out.print("[LOGGED OUT] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var input = line.split(" ");

            if (input[0].equals("register")) {
                try {
                    String response = facade.register(input[1], input[2], input[3]);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    } else {
                        this.postLogin();
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter your username, password, and email.");
                }
            } else if (input[0].equals("login")) {
                try {
                    String response = facade.login(input[1], input[2]);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    } else {
                        this.postLogin();
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter your username and password.");
                }
            } else if (input[0].equals("quit")) {
                System.out.println("Goodbye!");
                break;
            } else { // Default case called with "help" or any other input
                System.out.println("\tregister <USERNAME> <PASSWORD> <EMAIL> - Create an account");
                System.out.println("\tlogin <USERNAME> <PASSWORD> - Sign in");
                System.out.println("\tquit - Exit the program");
                System.out.println("\thelp - View possible commands");
            }
        }
    }

    private void postLogin() throws URISyntaxException, IOException {
        while (true) {
            System.out.print("[LOGGED IN] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var input = line.split(" ");

            if (input[0].equals("create")) {
                try {
                    String response = facade.createGame(input[1]);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter a game name.");
                }
            } else if (input[0].equals("list")) {
                System.out.println(facade.listGames());
            } else if (input[0].equals("join")) {
                try {
                    // This would be true if a game id was included but not a team (in other words, the player will be an observer)
                    String response;
                    ChessGame.TeamColor team;
                    if (input.length == 2) {
                        team = null;
                    } else {
                        team = ChessGame.TeamColor.valueOf(input[2]);
                    }
                    int gameID = Integer.parseInt(input[1]);
                    response = facade.joinGame(gameID, team);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    } else {
                        this.gameplay(gameID, team);
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter a game id.");
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid game id\nPlease enter a valid game id.");
                }
            } else if (input[0].equals("observe")) {
                try {
                    int gameID = Integer.parseInt(input[1]);
                    String response = facade.joinGame(gameID, null);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    } else {
                        this.gameplay(gameID, null);
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter a game id.");
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid game id\nPlease enter a valid game id.");
                }
            } else if (input[0].equals("logout")) {
                System.out.println(facade.logout());
                this.preLogin();
                break;
            } else if (input[0].equals("quit")) {
                System.out.println("Logging out...");
                facade.logout();
                System.out.println("Goodbye!");
                break;
            } else { // Default case called with "help" or any other input
                System.out.println("\tcreate <NAME> - Create a game");
                System.out.println("\tlist - See a list of games");
                System.out.println("\tjoin <ID> [WHITE|BLACK|<empty>] - Join a game");
                System.out.println("\tobserve <ID> - Observe a game");
                System.out.println("\tlogout - Log out");
                System.out.println("\tquit - Exit the program");
                System.out.println("\thelp - View possible commands");
            }
        }
    }

    private void gameplay(int gameID, ChessGame.TeamColor team) throws URISyntaxException, IOException {
//        if (team == BLACK) {
//            this.printBoardBlack(new ChessGame());
//        } else { // WHITE or observer
//            this.printBoardWhite(new ChessGame());
//        }
//        System.out.print("\u001b[39;49m"); // Set text back to default
        try {
            WebSocketFacade wsFacade = new WebSocketFacade(port, this);
            wsFacade.connect();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            System.out.print("[GAMEPLAY] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var input = line.split(" ");

            if (input[0].equals("redraw")) {
                if (team == BLACK) {
                    this.printBoardBlack(new ChessGame());
                } else { // WHITE or observer
                    this.printBoardWhite(new ChessGame());
                }
                System.out.print("\u001b[39;49m"); // Set text back to default
            } else if (input[0].equals("leave")) {
                System.out.println("leaving");
            } else if (input[0].equals("move")) {
                System.out.println("moving");
            } else if (input[0].equals("resign")) {
                System.out.println("resigning");
            } else if (input[0].equals("show_moves")) {
                System.out.println("showing moves");
            } else { // Default case called with "help" or any other input
                System.out.println("\tredraw - Redraw the chess board");
                System.out.println("\tleave - Leave the game");
                System.out.println("\tmove <START_ROW> <START_COLUMN> <END_ROW> <END_COLUMN> - Move the piece from a starting position to a different position");
                System.out.println("\tresign - Forfeit and end the game");
                System.out.println("\tshow_moves <ROW> <COLUMN> - Show the legal moves for a piece at a given position");
                System.out.println("\thelp - View possible commands");
            }
        }
    }

    private void printBoardWhite(ChessGame chessGame) {
        ChessBoard board = chessGame.getBoard();
        board.resetBoard(); // Sets the board to an initial set up

        // Orientation with white at the bottom
        for (int row = 9; row >= 0; row--) {
            if ((row == 0) || row == 9) {
                System.out.println("\u001b[30;104m" + EscapeSequences.EMPTY + " a  b  c  d  e  f  g  h " + EscapeSequences.EMPTY + "\u001b[30;49m");
            } else {
                for (int col = 0; col < 10; col++) {
                    if ((col == 0) || (col == 9)) {
                        System.out.printf("\u001b[30;104m %d \u001b[30;49m", (row));
                    } else {
                        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                        boolean lightSpace = ((row + col) % 2 != 0);
                        this.printPiece(piece, lightSpace);
                    }
                }
                System.out.print("\u001b[30;49m\n");
            }
        }
    }

    private void printBoardBlack(ChessGame chessGame) {
        ChessBoard board = chessGame.getBoard();
        board.resetBoard(); // Sets the board to an initial set up

        // Orientation with black at the bottom
        for (int row = 0; row < 10; row++) {
            if ((row == 0) || row == 9) {
                System.out.println("\u001b[30;104m" + EscapeSequences.EMPTY + " h  g  f  e  d  c  b  a " + EscapeSequences.EMPTY + "\u001b[30;49m");
            } else {
                for (int col = 9; col >= 0; col--) {
                    if ((col == 0) || (col == 9)) {
                        System.out.printf("\u001b[30;104m %d \u001b[30;49m", (row));
                    } else {
                        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                        boolean lightSpace = ((row + col) % 2 != 0);
                        this.printPiece(piece, lightSpace);
                    }
                }
                System.out.print("\u001b[30;49m\n");
            }
        }
    }

    private void printPiece(ChessPiece piece, boolean lightSpace) {
        if (lightSpace) {
            System.out.print("\u001b[97;47m");
        } else {
            System.out.print("\u001b[97;100m");
        }

        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                switch (piece.getPieceType()) {
                    case KING:
                        System.out.print(EscapeSequences.WHITE_KING);
                        break;
                    case QUEEN:
                        System.out.print(EscapeSequences.WHITE_QUEEN);
                        break;
                    case BISHOP:
                        System.out.print(EscapeSequences.WHITE_BISHOP);
                        break;
                    case KNIGHT:
                        System.out.print(EscapeSequences.WHITE_KNIGHT);
                        break;
                    case ROOK:
                        System.out.print(EscapeSequences.WHITE_ROOK);
                        break;
                    case PAWN:
                        System.out.print(EscapeSequences.WHITE_PAWN);
                        break;
                }
            } else {
                switch (piece.getPieceType()) {
                    case KING:
                        System.out.print(EscapeSequences.BLACK_KING);
                        break;
                    case QUEEN:
                        System.out.print(EscapeSequences.BLACK_QUEEN);
                        break;
                    case BISHOP:
                        System.out.print(EscapeSequences.BLACK_BISHOP);
                        break;
                    case KNIGHT:
                        System.out.print(EscapeSequences.BLACK_KNIGHT);
                        break;
                    case ROOK:
                        System.out.print(EscapeSequences.BLACK_ROOK);
                        break;
                    case PAWN:
                        System.out.print(EscapeSequences.BLACK_PAWN);
                        break;
                }
            }
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
    }
}
