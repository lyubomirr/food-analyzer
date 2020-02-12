package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public class FoodCacheMissException extends ClientMessageException {
    private static final String DEFAULT_MESSAGE = "No cached data for this product!";

    public FoodCacheMissException() {
        super(DEFAULT_MESSAGE);
    }

    public FoodCacheMissException(String message) {
        super(message);
    }
}