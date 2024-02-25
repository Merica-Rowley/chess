package server;

import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.Exceptions.*;
import model.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class Handler {
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final UserDAO userDAO = new MemoryUserDAO();

    public Object clearApplication(Request request, Response response) {
        ClearService service = new ClearService(userDAO, gameDAO, authDAO);
        Gson gson = new Gson();
        try {
            service.clear();
            response.status(200);
            return "";
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }

    public Object register(Request request, Response response) throws DataAccessException {
        UserService service = new UserService(userDAO, authDAO);
        Gson gson = new Gson();

        UserData userData = gson.fromJson(request.body(), UserData.class);

        try {
            AuthData authData = service.registerUser(userData.username(), userData.password(), userData.email());
            response.status(200);
            return gson.toJson(authData);
        } catch (MissingInformationException e) {
            response.status(400);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (UserAlreadyExistsException e) {
            response.status(403);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }

    public Object login(Request request, Response response) {
        UserService service = new UserService(userDAO, authDAO);
        Gson gson = new Gson();

        LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);

        try {
            AuthData authData = service.loginUser(loginRequest.username(), loginRequest.password());
            response.status(200);
            return gson.toJson(authData);
        } catch (IncorrectPasswordException | UserNotFoundException e) {
            response.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }

    public Object logout(Request request, Response response) {
        UserService service = new UserService(userDAO, authDAO);
        Gson gson = new Gson();

        String authToken = request.headers("authorization");

        try {
            service.logoutUser(authToken);
            response.status(200);
            return "";
        } catch (NotLoggedInException e) {
            response.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }

    public Object listGames(Request request, Response response) {
        GameService service = new GameService(authDAO, gameDAO);
        Gson gson = new Gson();

        String authToken = request.headers("authorization");

        try {
            ArrayList<GameListData> games = service.listGames(authToken);
            response.status(200);
            return gson.toJson(new ResponseGameList(games));
        } catch (NotLoggedInException e) {
            response.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }

    public Object createGame(Request request, Response response) {
        GameService service = new GameService(authDAO, gameDAO);
        Gson gson = new Gson();

        String authToken = request.headers("authorization");
        CreateGameRequest gameRequest = gson.fromJson(request.body(), CreateGameRequest.class);

        try {
            int gameID = service.createGame(authToken, gameRequest.gameName());
            response.status(200);
            return gson.toJson(new ResponseMessageNumber(gameID));
        } catch (MissingInformationException e) {
            response.status(400);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (NotLoggedInException e) {
            response.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }

    public Object joinGame(Request request, Response response) {
        GameService service = new GameService(authDAO, gameDAO);
        Gson gson = new Gson();

        String authToken = request.headers("authorization");
        JoinGameRequest joinGameRequest = gson.fromJson(request.body(), JoinGameRequest.class);

        try {
            service.joinGame(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());
            response.status(200);
            return "";
        } catch (NoGameFoundException e) {
            response.status(400);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (NotLoggedInException e) {
            response.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (TeamTakenException e) {
            response.status(403);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }
}
