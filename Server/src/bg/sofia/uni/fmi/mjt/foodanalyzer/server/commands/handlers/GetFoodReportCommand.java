package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.Constants;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;

import java.util.concurrent.CompletableFuture;

public class GetFoodReportCommand extends BaseCommandHandler {
    private final static int ARGS_COUNT = 1;
    private final static int FOOD_ID_INDEX = 0;

    public GetFoodReportCommand(FoodApiClient foodApiClient) {
        super(foodApiClient);
    }

    @Override
    public CompletableFuture<String> execute(String arguments) {
        if(arguments == null || arguments.isBlank()) {
            return CompletableFuture.completedFuture(ServerMessages.GET_FOOD_REPORT_WRONG_FORMAT);
        }

        var argumentsSplit = arguments.trim().split(Constants.WHITESPACE_SPLIT_REGEX);
        if (argumentsSplit.length != ARGS_COUNT) {
            return CompletableFuture.completedFuture(ServerMessages.GET_FOOD_REPORT_WRONG_FORMAT);
        }

        var foodId = argumentsSplit[FOOD_ID_INDEX];
        if(!isParsableToLong(foodId)) {
            return CompletableFuture.completedFuture(ServerMessages.INVALID_FOOD_ID);
        }

        return foodApiClient.getDetails(foodId)
                .thenApply(FoodDetails::toString)
                .exceptionally(this::exceptionHandler);
    }

    private static boolean isParsableToLong(String textRepresentation) {
        try {
            Long.parseLong(textRepresentation);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
