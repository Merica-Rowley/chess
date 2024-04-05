package webSocketMessages.serverMessages;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.NOTIFICATION;

public class Notification extends ServerMessage {
    private final String message;

    public Notification(String message) {
        super(NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
