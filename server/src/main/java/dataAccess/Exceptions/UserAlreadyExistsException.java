package dataAccess.Exceptions;

public class UserAlreadyExistsException extends DataAccessException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
