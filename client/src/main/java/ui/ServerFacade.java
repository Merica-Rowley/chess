package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static java.lang.String.format;

public class ServerFacade {
    private final int port;
    private String authToken = "";

    public ServerFacade(int port) {
        this.port = port;
    }

    public String register(String username, String password, String email) throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:" + port + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);
        // Header that specifies that body will be of type json
        http.addRequestProperty("Content-Type", "application/json");

        // Write body
        UserData body = new UserData(username, password, email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        http.connect();

        int responseCode = http.getResponseCode();

        switch (responseCode) {
            case 200:
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    AuthData response = new Gson().fromJson(inputStreamReader, AuthData.class);
                    this.authToken = response.authToken();
                    return format("Success! Registered with username: %s", response.username());
                }
            default: // Catches all errors and displays the error message
                try (InputStream errorBody = http.getErrorStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(errorBody);
                    var errorMessage = new Gson().fromJson(inputStreamReader, ResponseMessage.class);
                    return errorMessage.message();
                }
        }
    }

    public String login(String username, String password) throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);
        // Header that specifies that body will be of type json
        http.addRequestProperty("Content-Type", "application/json");

        // Write body
        LoginRequest body = new LoginRequest(username, password);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        http.connect();

        int responseCode = http.getResponseCode();

        switch (responseCode) {
            case 200:
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    AuthData response = new Gson().fromJson(inputStreamReader, AuthData.class);
                    this.authToken = response.authToken();
                    return format("Success! Logged in as: %s", response.username());
                }
            default: // Catches all errors and displays the error message
                try (InputStream errorBody = http.getErrorStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(errorBody);
                    var errorMessage = new Gson().fromJson(inputStreamReader, ResponseMessage.class);
                    return errorMessage.message();
                }
        }
    }

    public String logout() throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        http.setDoOutput(true);
        // Header that specifies that body will be of type json
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", authToken);

        // Make request
        http.connect();

        int responseCode = http.getResponseCode();

        switch (responseCode) {
            case 200:
                return "Success! Logged out";
            default: // Catches all errors and displays the error message
                try (InputStream errorBody = http.getErrorStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(errorBody);
                    var errorMessage = new Gson().fromJson(inputStreamReader, ResponseMessage.class);
                    return errorMessage.message();
                }
        }
    }

    public String createGame(String gameName) throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);
        // Header that specifies that body will be of type json
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", authToken);

        // Write body
        CreateGameRequest body = new CreateGameRequest(gameName);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        http.connect();

        int responseCode = http.getResponseCode();

        switch (responseCode) {
            case 200:
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    ResponseMessageNumber response = new Gson().fromJson(inputStreamReader, ResponseMessageNumber.class);
                    int gameID = response.gameID();
                    return format("Success! Created game with id: %d", gameID);
                }
            default: // Catches all errors and displays the error message
                try (InputStream errorBody = http.getErrorStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(errorBody);
                    var errorMessage = new Gson().fromJson(inputStreamReader, ResponseMessage.class);
                    return errorMessage.message();
                }
        }
    }

    public String listGames() throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        http.setDoOutput(true);
        // Header that specifies that body will be of type json
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", authToken);

        // Make request
        http.connect();

        int responseCode = http.getResponseCode();

        switch (responseCode) {
            case 200:
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    ArrayList<GameListData> response = new Gson().fromJson(inputStreamReader, ResponseGameList.class).games();
                    String gameList = "";
                    String separator = "";

                    for (GameListData game : response) {
                        gameList += separator;
                        gameList += game.gameID() + " " + game.gameName() + "\n";
                        String whiteUser = (game.whiteUsername() == null ? "No Player" : game.whiteUsername());
                        gameList += "White Team: " + whiteUser + "\n";
                        String blackUser = (game.blackUsername() == null ? "No Player" : game.blackUsername());
                        gameList += "Black Team: " + blackUser + "\n";
                        separator = "\n";
                    }

                    return format(gameList);
                }
            default: // Catches all errors and displays the error message
                try (InputStream errorBody = http.getErrorStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(errorBody);
                    var errorMessage = new Gson().fromJson(inputStreamReader, ResponseMessage.class);
                    return errorMessage.message();
                }
        }
    }

    public String joinGame(int gameID, ChessGame.TeamColor teamColor) throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");

        http.setDoOutput(true);
        // Header that specifies that body will be of type json
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", authToken);

        // Write body
        JoinGameRequest body = new JoinGameRequest(teamColor, gameID);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        http.connect();

        int responseCode = http.getResponseCode();

        switch (responseCode) {
            case 200:
                return "Success! Joined game";
            default: // Catches all errors and displays the error message
                try (InputStream errorBody = http.getErrorStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(errorBody);
                    var errorMessage = new Gson().fromJson(inputStreamReader, ResponseMessage.class);
                    return errorMessage.message();
                }
        }
    }

    public String getAuthToken() {
        return authToken;
    }
}
