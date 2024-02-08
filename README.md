# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## UML Server Sequence Diagram
actor Client
participant Server
participant Service
participant DataAccess
database db

group #navy Registration #white
Client -> Server: [POST] /user\n{username, password, email}
Server -> Service: register(username, password, email)
Service -> DataAccess: getUser(username)
DataAccess -> db: SELECT username from user
DataAccess --> Service: null
Service -> DataAccess: createUser(username, password)
DataAccess -> db: INSERT username, password, email INTO user
Service -> DataAccess: createAuth(username)
DataAccess -> db: INSERT username, authToken INTO auth
DataAccess --> Service: authToken
Service --> Server: authToken
Server --> Client: 200\n{authToken}
end

group #orange Login #white
Client -> Server: [POST] /session\n{username, password}
Server -> Service: login(username, password)
Service -> DataAccess: getUser(username)
DataAccess -> db: SELECT username from user
Service -> DataAccess: getPassword(password)
DataAccess -> db: SELECT password from user
Service -> DataAccess: createAuth(username)
DataAccess -> db: INSERT username, authToken INTO auth
DataAccess --> Service: authToken
Service --> Server: authToken
Server --> Client: [200]\n{authToken}
end

group #green Logout #white
Client -> Server: [DELETE] /session\nauthToken
Server -> Service: logout(authToken)
Service -> DataAccess: delete(authToken)
DataAccess -> db: DELETE authToken from auth
Server --> Client: [200]
end

group #red List Games #white
Client -> Server: [GET] /game\nauthToken
Server -> Service: listGames(authToken)
Service -> DataAccess: getAuthToken(authToken)
DataAccess -> db: SELECT authToken from auth
Service -> DataAccess: getGames(authToken)
DataAccess -> db: SELECT * from game
DataAccess --> Service: games
Service --> Server: games
Server --> Client: [200]\n{games: [{gameID, whiteUsername, blackUsername, gameName}]}
end

group #purple Create Game #white
Client -> Server: [POST] /game\nauthToken\n{gameName}
Server -> Service: createGame(authToken, gameName)
Service -> DataAccess: getAuthToken(authToken)
DataAccess -> db: SELECT authToken from auth
Service -> DataAccess: createGame(gameName)
DataAccess -> db: INSERT gameID, gameName, game INTO game
DataAccess --> Service: gameID
Service --> Server: gameID
Server --> Client: [200]\n{gameID}
end

group #yellow Join Game #black
Client -> Server: [PUT] /game\nauthToken\n{ClientColor, gameID}
Server -> Service: joinGame(authToken, ClientColor, gameID)
Service -> DataAccess: getAuthToken(authToken)
DataAccess -> db: SELECT authToken from auth
Service -> DataAccess: getGame(gameID)
DataAccess -> db: SELECT gameID from game
Service -> DataAccess: updateGame(gameID, ClientColor)
DataAccess -> db: UPDATE games \nSET teamColor to ClientColor\nWHERE gameID = gameID
Server --> Client: [200]
end

group #gray Clear Application #white
Client -> Server: [DELETE] /db
Server -> Service: deleteDatabase()
Service -> DataAccess: deleteAllUsers()
DataAccess -> db: DELETE * from user
Service -> DataAccess: deleteAllGames()
DataAccess -> db: DELETE * from game
Service -> DataAccess: deleteAllAuthData()
DataAccess -> db: DELETE * from auth
Server -> Client: [200]
end

