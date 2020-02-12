package bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;

public interface FoodDataCache {
    FoodSearchResult searchByName(String foodName);
    FoodDetails getFoodDetailsByFoodId(String foodId);
    FoodDetails getFoodDetailsByGtinUpc(String gtinUpc);
    boolean hasBeenSearched(String searchInput);

    FoodSearchResult saveFoodSearchResult(FoodSearchResult foodSearchResult, String searchInput);
    FoodDetails saveFoodDetails(FoodDetails foodDetails);
}
