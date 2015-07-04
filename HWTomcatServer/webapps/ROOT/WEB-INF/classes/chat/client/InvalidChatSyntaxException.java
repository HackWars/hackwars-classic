package chat.client;

public class InvalidChatSyntaxException extends Throwable {

    private String message;

    public InvalidChatSyntaxException(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

}