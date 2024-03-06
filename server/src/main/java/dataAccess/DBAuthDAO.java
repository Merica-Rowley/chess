package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DBAuthDAO implements AuthDAO {
    public void insertAuthData(AuthData a) throws DataAccessException {
        configureDatabase();
    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        configureDatabase();
        return null;
    }

    public void deleteAuthData(String authToken) throws DataAccessException {
        configureDatabase();
    }

    public void deleteAllAuthData() throws DataAccessException {
        configureDatabase();
        var statement = "TRUNCATE AuthData";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
//                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
//            """
//            CREATE TABLE IF NOT EXISTS AuthData (
//              `authToken` varchar(256) NOT NULL,
//              `username` varchar(256) NOT NULL,
//              PRIMARY KEY (`authToken`),
//            )
//            """
            """
            CREATE TABLE IF NOT EXISTS AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
