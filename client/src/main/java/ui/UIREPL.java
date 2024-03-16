package ui;

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
                System.out.println("\tquit - Exit program");
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

            } else if (input[0].equals("list")) {

            } else if (input[0].equals("join")) {

            } else if (input[0].equals("observe")) {

            } else if (input[0].equals("logout")) {

            } else if (input[0].equals("quit")) {
                System.out.println("Logging out...");
                facade.logout();
                System.out.println("Goodbye!");
                break;
            } else { // Default case called with "help" or any other input

            }
        }
    }

    private void gameplay() {

    }
}
