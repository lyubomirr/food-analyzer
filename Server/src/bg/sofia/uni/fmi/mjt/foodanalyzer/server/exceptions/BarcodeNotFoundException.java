package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public class BarcodeNotFoundException extends RuntimeException {
    public BarcodeNotFoundException(String message) {
        super(message);
    }

    public BarcodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}