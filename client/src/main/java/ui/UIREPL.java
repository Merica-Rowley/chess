package ui;

import chess.ChessGame;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class UIREPL {
    private final ServerFacade facade;

    public UIREPL(int port) {
        this.facade = new ServerFacade(port);
    }

    public void run() throws URISyntaxException, IOException {
        this.preLogin();
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
                    }
                    else {
                        this.postLogin();
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter your username, password, and email.");
                }
            }
            else if (input[0].equals("login")) {
                try {
                    String response = facade.login(input[1], input[2]);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    }
                    else {
                        this.postLogin();
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter your username and password.");
                }
            }
            else if (input[0].equals("quit")) {
                System.out.println("Goodbye!");
                break;
            }
            else { // Default case called with "help" or any other input
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
                        response = facade.joinGame(Integer.parseInt(input[1]), null);
                    } else {
                        response = facade.joinGame(Integer.parseInt(input[1]), ChessGame.TeamColor.valueOf(input[2]));
                    }
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    } else {
                        this.gameplay();
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter a game id.");
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid game id\nPlease enter a valid game id.");
                }
            } else if (input[0].equals("observe")) {
                try {
                    String response = facade.joinGame(Integer.parseInt(input[1]), null);
                    System.out.println(response);
                    if (response.contains("Error")) {
                        System.out.print("Enter 'help' to see a list of commands\n");
                    } else {
                        this.gameplay();
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: missing input\nPlease enter a game id.");
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
                System.out.println("\thelp - View possible commands");            }
        }
    }

    private void gameplay() {
        System.out.println("in gameplay");
    }
}
