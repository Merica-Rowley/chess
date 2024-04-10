package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import model.AuthData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DBAuthDAO implements AuthDAO {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    DAOHelper helper = new DAOHelper(createStatements);

    public void insertAuthData(AuthData a) throws DataAccessException {
        helper.configureDatabase();
        if (this.getAuthData(a.authToken()) != null) throw new DataAccessException("Error: authToken already associated with user");
        var statement = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        String authToken = a.authToken();
        String username = a.username();
        helper.executeUpdate(statement, authToken, username);
    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        helper.configureDatabase();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM AuthData WHERE authToken=?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);
            var rs = ps.executeQuery();
                if (rs.next()) {
                    String username = rs.getString("username");
                    return new AuthData(authToken, username);
                }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }

        return null;
    }

    public void deleteAuthData(String authToken) throws DataAccessException {
        helper.configureDatabase();
        if (this.getAuthData(authToken) == null) throw new NotLoggedInException("Error: authToken not found; user not logged in");
        var statement = "DELETE FROM AuthData WHERE authToken=?";
        helper.executeUpdate(statement, authToken);
    }

    public void deleteAllAuthData() throws DataAccessException {
        helper.configureDatabase();
        var statement = "TRUNCATE AuthData";
        helper.executeUpdate(statement);
    }
}
