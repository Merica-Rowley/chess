package dataAccess;

public class NotLoggedInException extends DataAccessException {
    public NotLoggedInException(String message) {
        super(message);
    }
}
