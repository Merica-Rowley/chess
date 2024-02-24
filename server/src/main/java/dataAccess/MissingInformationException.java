package dataAccess;

public class MissingInformationException extends DataAccessException {
    public MissingInformationException(String message) {
        super(message);
    }
}
