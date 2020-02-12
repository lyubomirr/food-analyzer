package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.ErrorResponse;

public class FoodApiServerErrorException extends ClientMessageException {
    private static final String DEFAULT_MESSAGE = "There was an error in food api server. " +
            "Response message: \"%s\".";

    public FoodApiServerErrorException(ErrorResponse errorResponse) {
        super(String.format(DEFAULT_MESSAGE, errorResponse.getMessage()));
    }
}
