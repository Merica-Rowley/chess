package webSocketMessages.serverMessages;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.ERROR;

public class Error extends ServerMessage {
    private final String errorMessage;

    public Error(String errorMessage) {
        super(ERROR);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
