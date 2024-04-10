package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.MissingInformationException;
import dataAccess.Exceptions.UserAlreadyExistsException;
import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DBUserDAO implements UserDAO {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS UserData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    DAOHelper helper = new DAOHelper(this.createStatements);

    public void insertUser(UserData user) throws DataAccessException {
        helper.configureDatabase();

        if (user.username() == null || user.password() == null || user.email() == null) throw new MissingInformationException("Error: missing registration information");
        if(this.getUser(user.username()) != null) throw new UserAlreadyExistsException("Error: username already taken");

        var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";
        String username = user.username();
        String password = user.password();
        String email = user.email();
        helper.executeUpdate(statement, username, password, email);
    }

    public UserData getUser(String username) throws DataAccessException {
        helper.configureDatabase();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password, email FROM UserData WHERE username=?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String password = rs.getString("password");
                String email = rs.getString("email");
                return new UserData(username, password, email);
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }

        return null;
    }

    public void deleteAllUsers() throws DataAccessException {
        helper.configureDatabase();
        var statement = "TRUNCATE UserData";
        helper.executeUpdate(statement);
    }
}
