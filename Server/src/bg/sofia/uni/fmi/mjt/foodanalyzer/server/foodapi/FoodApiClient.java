package bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;

import java.util.concurrent.CompletableFuture;

public interface FoodApiClient {
    CompletableFuture<FoodSearchResult> search(String foodName);
    CompletableFuture<FoodDetails> getDetails(String foodId);
    CompletableFuture<FoodDetails> getDetailsByBarcode(String gtinUpc);
}
