package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;

import java.util.concurrent.CompletableFuture;

public class GetFoodCommand extends BaseCommandHandler {
    public GetFoodCommand(FoodApiClient foodApiClient) {
        super(foodApiClient);
    }

    @Override
    public CompletableFuture<String> execute(String arguments) {
        if (arguments == null || arguments.isBlank()) {
            return CompletableFuture.completedFuture(ServerMessages.GET_FOOD_WRONG_FORMAT);
        }

        return foodApiClient.search(arguments)
                .thenApply(FoodSearchResult::toString)
                .exceptionally(this::exceptionHandler);
    }
}