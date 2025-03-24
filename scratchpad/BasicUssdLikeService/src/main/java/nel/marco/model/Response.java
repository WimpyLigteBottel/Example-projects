package nel.marco.model;

public class Response {

    private final String sessionId;
    private final String message;

    public Response(String sessionId, String message) {
        this.sessionId = sessionId;
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMessage() {
        return message;
    }
}
