package chat.client;

public class ChatException extends Throwable {

    private String message;

    public ChatException(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

}