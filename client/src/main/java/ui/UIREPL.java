package ui;

import chess.*;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;

public class UIREPL implements GameHandler {
    private final int port;
    private ChessGame.TeamColor teamColor = null;
    private ChessGame upToDateGame = null;
    private final ServerFacade facade;
    private WebSocketFacade wsFacade;

    public UIREPL(int port) throws DeploymentException, IOException, URISyntaxException {
        this.facade = new ServerFacade(port);
        this.port = port;

        try {
            wsFacade = new WebSocketFacade(port, this);
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() throws URISyntaxException, IOException {
        this.preLogin();
    }

    public void updateGame(ChessGame game) {
        upToDateGame = game;
        System.out.println();
        if (this.teamColor == BLACK) {
            this.printBoardBlack(null);
        } else {
            // For both white team and observers
            this.printBoardWhite(null);
        }
        System.out.print("[GAMEPLAY] >>> ");
    }

    public void printMessage(String message) {
        System.out.println(message);
        System.out.print("[GAMEPLAY] >>> ");
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
                    if (input.length == 2) {
                        this.teamColor = null;
                    } else {
                        this.teamColor = ChessGame.TeamColor.valueOf(input[2]);
                    }
                    int gameID = Integer.parseInt(input[1]);
                    response = facade.joinGame(gameID, this.teamColor);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    } else {
                        wsFacade.connect();
                        this.wsFacade.joinPlayer(facade.getAuthToken(), gameID, this.teamColor);
                        this.gameplay(gameID, this.teamColor);
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter a game id.");
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid game id\nPlease enter a valid game id.");
                } catch (DeploymentException e) {
                    throw new RuntimeException(e);
                }
            } else if (input[0].equals("observe")) {
                try {
                    int gameID = Integer.parseInt(input[1]);
                    String response = facade.joinGame(gameID, null);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    } else {
                        wsFacade.connect();
                        this.wsFacade.joinObserver(this.facade.getAuthToken(), gameID);
                        this.gameplay(gameID, null);
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter a game id.");
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid game id\nPlease enter a valid game id.");
                } catch (DeploymentException e) {
                    throw new RuntimeException(e);
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
        while (true) {
            System.out.print("[GAMEPLAY] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var input = line.split(" ");

            if (input[0].equals("redraw")) {
                if (team == BLACK) {
                    this.printBoardBlack(null);
                } else { // WHITE or observer
                    this.printBoardWhite(null);
                }
                System.out.print("\u001b[39;49m"); // Set text back to default
            } else if (input[0].equals("leave")) {
                wsFacade.leaveGame(facade.getAuthToken(), gameID);
                this.postLogin();
                break;
            } else if (input[0].equals("move")) {
                if (input.length < 5) {
                    System.out.println("Invalid syntax; Please enter in format move <START_ROW> <START_COLUMN> <END_ROW> <END_COLUMN>");
                } else {
                    try {
                        ChessPosition startingPosition = new ChessPosition(letterToNumberConversion(input[1]), letterToNumberConversion(input[2]));
                        ChessPosition endingPosition = new ChessPosition(letterToNumberConversion(input[3]), letterToNumberConversion(input[4]));
                        ChessPiece.PieceType promotionPiece = null;
                        if (input.length > 5) {
                            String promotion = input[5];
                            promotion = promotion.toLowerCase();
                            switch (promotion) {
                                case "knight" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                                case "queen" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                                case "bishop" -> promotionPiece = ChessPiece.PieceType.BISHOP;
                                case "rook" -> promotionPiece = ChessPiece.PieceType.ROOK;
                            }
                        }

                        ChessMove move = new ChessMove(startingPosition, endingPosition, promotionPiece);
                        wsFacade.makeMove(facade.getAuthToken(), gameID, move);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter valid coordinates");
                    }
                }
            } else if (input[0].equals("resign")) {
                wsFacade.resignGame(facade.getAuthToken(), gameID);
            } else if (input[0].equals("show_moves")) {
                if (input.length < 3) {
                    System.out.println("Invalid syntax; Please enter in format show_moves <ROW> <COLUMN>");
                } else {
                    try {
                        ChessPosition position = new ChessPosition(letterToNumberConversion(input[1]), letterToNumberConversion(input[2]));
                        if (teamColor == ChessGame.TeamColor.BLACK) {
                            this.printBoardBlack(position);
                        } else {
                            this.printBoardWhite(position);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter valid coordinates");
                    }
                }
            } else { // Default case called with "help" or any other input
                System.out.println("\tredraw - Redraw the chess board");
                System.out.println("\tleave - Leave the game");
                System.out.println("\tmove <START_ROW> <START_COLUMN> <END_ROW> <END_COLUMN> <PROMOTION_PIECE> - " +
                        "Move the piece from a starting position to a different position (only include promotion " +
                        "piece if a pawn is going to be promoted)");
                System.out.println("\tresign - Forfeit and end the game");
                System.out.println("\tshow_moves <ROW> <COLUMN> - Show the legal moves for a piece at a given position");
                System.out.println("\thelp - View possible commands");
            }
        }
    }

    private int letterToNumberConversion(String input) throws NumberFormatException {
        switch (input) {
            case "a" -> {
                return 1;
            }
            case "b" -> {
                return 2;
            }
            case "c" -> {
                return 3;
            }
            case "d" -> {
                return 4;
            }
            case "e" -> {
                return 5;
            }
            case "f" -> {
                return 6;
            }
            case "g" -> {
                return 7;
            }
            case "h" -> {
                return 8;
            }
            default -> {
                return Integer.parseInt(input);
            }
        }
    }

    private void printBoardWhite(ChessPosition position) {
        ChessBoard board = this.upToDateGame.getBoard();
        Collection<ChessMove> possibleMoves = null;
        if (position != null) {
            possibleMoves = this.upToDateGame.validMoves(position);
        }

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
                        boolean moveIsValid = false;

                        if (possibleMoves != null) {
                            if (possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), null)) ||
                                    possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.QUEEN)) ||
                                    possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.BISHOP)) ||
                                    possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT)) ||
                                    possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.ROOK))) {
                                moveIsValid = true;
                            }
                        }

                        this.printPiece(piece, lightSpace, moveIsValid);
                    }
                }
                System.out.print("\u001b[30;49m\n");
            }
        }

        System.out.print("\u001b[39;49m"); // Set text back to default
    }

    private void printBoardBlack(ChessPosition position) {
        ChessBoard board = this.upToDateGame.getBoard();
        Collection<ChessMove> possibleMoves = null;
        if (position != null) {
            possibleMoves = this.upToDateGame.validMoves(position);
        }

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
                        boolean moveIsValid = false;

                        if (possibleMoves != null) {
                            if (possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), null)) ||
                                    possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.QUEEN)) ||
                                    possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.BISHOP)) ||
                                    possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT)) ||
                                    possibleMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.ROOK))) {
                                moveIsValid = true;
                            }
                        }

                        this.printPiece(piece, lightSpace, moveIsValid);
                    }
                }
                System.out.print("\u001b[30;49m\n");
            }
        }

        System.out.print("\u001b[39;49m"); // Set text back to default
    }

    private void printPiece(ChessPiece piece, boolean lightSpace, boolean moveSpace) {
        if (lightSpace) {
            if (moveSpace) {
                System.out.print("\u001b[97;102m");
            } else {
                System.out.print("\u001b[97;47m");
            }
        } else {
            if (moveSpace) {
                System.out.print("\u001b[97;42m");
            } else {
                System.out.print("\u001b[97;100m");
            }
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
