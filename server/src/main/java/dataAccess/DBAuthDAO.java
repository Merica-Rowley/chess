package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class DBAuthDAO implements AuthDAO {
    public int size() {
        return 0;
    }

    public void insertAuthData(AuthData a) throws DataAccessException {

    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuthData(String authToken) throws DataAccessException {

    }

    public void deleteAllAuthData() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
            )
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
