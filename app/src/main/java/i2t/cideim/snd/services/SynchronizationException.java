package i2t.cideim.snd.services;

/**
 * Created by Juan.
 * Represents an exception during the synchronization process against the server.
 */
public class SynchronizationException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message = null;

    public SynchronizationException(String message) {
        super(message);
        this.message = message;
    }

    public SynchronizationException(Throwable cause) {
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