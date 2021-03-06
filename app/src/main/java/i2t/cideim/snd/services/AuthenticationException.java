package i2t.cideim.snd.services;

/**
 * Created by Juan.
 * Represents an exception during the authentication process against the server.
 */
public class AuthenticationException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message = null;

    public AuthenticationException(String message) {
        super(message);
        this.message = message;
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}