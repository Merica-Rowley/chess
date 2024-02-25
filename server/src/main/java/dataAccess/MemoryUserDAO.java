package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    public HashMap<String, UserData> storage = new HashMap<String, UserData>();

    public int size() {
        return storage.size();
    }

    public void insertUser(UserData user) throws DataAccessException {
        if (storage.containsKey(user.username())) throw new UserAlreadyExistsException("Error: username already associated taken");
        if (user.username() == null || user.password() == null || user.email() == null) throw new MissingInformationException("Error: missing registration information");
        storage.put(user.username(), user);
    }

    public UserData getUser(String username) throws DataAccessException {
        if (!storage.containsKey(username)) throw new UserNotFoundException("Error: No user with username found");
        return storage.get(username);
    }

    public void deleteUser(String username) throws DataAccessException {
        if (!storage.containsKey(username)) throw new DataAccessException("Error: No user with username found");
        storage.remove(username);
    }

    public void deleteAllUsers() {
        storage.clear();
    }
}
