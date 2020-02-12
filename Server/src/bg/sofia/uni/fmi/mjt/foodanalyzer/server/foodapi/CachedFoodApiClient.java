package bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache.FoodDataCache;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodCacheMissException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;

import java.util.concurrent.CompletableFuture;

public class CachedFoodApiClient implements FoodApiClient {
    private final FoodApiClient inner;
    private final FoodDataCache foodCache;

    public CachedFoodApiClient(FoodApiClient inner, FoodDataCache foodCache) {
        this.inner = inner;
        this.foodCache = foodCache;
    }

    @Override
    public CompletableFuture<FoodSearchResult> search(String foodName) {
        if(!foodCache.hasBeenSearched(foodName)) {
            return inner.search(foodName)
                    .thenApply(result -> foodCache.saveFoodSearchResult(result, foodName));
        }

        var cacheSearchResult = foodCache.searchByName(foodName);
        return CompletableFuture.completedFuture(cacheSearchResult);
    }

    @Override
    public CompletableFuture<FoodDetails> getDetails(String foodId) {
        var foodDetails = foodCache.getFoodDetailsByFoodId(foodId);
        if(!foodDetails.equals(FoodDetails.nullObject())) {
            return CompletableFuture.completedFuture(foodDetails);
        }

        return inner.getDetails(foodId)
                .thenApply(foodCache::saveFoodDetails);
    }

    @Override
    public CompletableFuture<FoodDetails> getDetailsByBarcode(String gtinUpc) {
        var foodDetails = foodCache.getFoodDetailsByGtinUpc(gtinUpc);
        return CompletableFuture.completedFuture(foodDetails)
                .thenApply(this::checkIfFoodDetailsNull);
    }

    //Chained in a completion stage in order exception to be caught by 'exceptionally' handler.
    private FoodDetails checkIfFoodDetailsNull(FoodDetails foodDetails) {
        if(foodDetails.equals(FoodDetails.nullObject())) {
            throw new FoodCacheMissException();
        }

        return foodDetails;
    }
}