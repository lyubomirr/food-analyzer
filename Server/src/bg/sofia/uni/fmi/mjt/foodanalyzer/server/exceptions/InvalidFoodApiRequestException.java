package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.ErrorResponse;

public class InvalidFoodApiRequestException extends ClientMessageException {
    private static final String DEFAULT_MESSAGE = "Your request was invalid. Response message: \"%s\".";

    public InvalidFoodApiRequestException(ErrorResponse errorResponse) {
        super(String.format(DEFAULT_MESSAGE, errorResponse.getMessage()));
    }

    public InvalidFoodApiRequestException(String message) {
        super(message);
    }
}
