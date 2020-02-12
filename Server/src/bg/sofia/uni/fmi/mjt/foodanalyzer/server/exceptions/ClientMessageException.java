package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public abstract class ClientMessageException extends RuntimeException {
    protected ClientMessageException(String message) {
        super(message);
    }

    protected ClientMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}