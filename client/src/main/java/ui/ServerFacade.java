package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {
    private final int port;
    private String authToken = "";

    public ServerFacade(int port) {
        this.port = port;
    }

    public void register(String username, String password, String email) throws URISyntaxException, IOException {
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

        // Output response
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthData response = new Gson().fromJson(inputStreamReader, AuthData.class);
            this.authToken = response.authToken();
            System.out.println(response);
        }
    }

    public void login(String username, String password) {
        // post
    }

    public void logout() {
        // delete
    }

    public void createGame(String gameName) {
        // post
    }

    public void listGames() {
        // get
    }

    public void joinGame(int gameID, ChessGame.TeamColor teamColor) {
        // put
    }
}
