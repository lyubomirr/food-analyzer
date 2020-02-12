package bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FoodDataCacheImpl implements FoodDataCache {

    private final ExecutorService executorService;
    private final CacheFilesHandler cacheFilesHandler;
    private final Gson gson;

    private Set<Food> cachedFoods;
    private Set<FoodDetails> cachedDetails;
    private Set<String> cachedSearches;

    public FoodDataCacheImpl(CacheFilesHandler cacheFilesHandler, Gson gson) {

        this.cacheFilesHandler = cacheFilesHandler;
        this.gson = gson;
        loadFileData();

        this.executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> fileWatchers());
    }

    private synchronized void fileWatchers() {
        try {
            while(true) {
                this.wait();
                loadFileData();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadFileData() {
        this.cachedFoods = getFoodsFromFile();
        this.cachedDetails = getFoodDetailsFromFile();
        this.cachedSearches = getFoodSearchesFromFile();
    }

    private Set<Food> getFoodsFromFile() {
        try {
            var foodsData = cacheFilesHandler.readFoodsFileContent();
            if(foodsData == null || foodsData.isBlank()) {
                return new HashSet<>();
            }

            var foodCollectionType = new TypeToken<HashSet<Food>>(){}.getType();
            return gson.fromJson(foodsData, foodCollectionType);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException("There was problem while reading foods cache file!", e);
        }
    }

    private Set<FoodDetails> getFoodDetailsFromFile() {
        try {
            var detailsData = cacheFilesHandler.readFoodDetailsFileContent();
            if(detailsData == null || detailsData.isBlank()) {
                return new HashSet<>();
            }

            var foodCollectionType = new TypeToken<HashSet<FoodDetails>>(){}.getType();
            return gson.fromJson(detailsData, foodCollectionType);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException("There was problem while reading food details cache file!", e);
        }
    }

    private Set<String> getFoodSearchesFromFile() {
        try {
            var pastSearches = cacheFilesHandler.readPastSearchesFile();
            if(pastSearches == null || pastSearches.isBlank()) {
                return new HashSet<>();
            }

            var foodCollectionType = new TypeToken<HashSet<String>>(){}.getType();
            return gson.fromJson(pastSearches, foodCollectionType);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException("There was problem while reading past searches file!", e);
        }
    }

    public FoodSearchResult searchByName(String foodName) {
        if(foodName == null || foodName.isBlank()) {
            return new FoodSearchResult();
        }

        var foodNameWords = StringUtils.getWords(foodName);

        var foundFoods = cachedFoods.stream()
                .filter(food -> searchFilterPredicate(food, foodNameWords))
                .collect(Collectors.toList());

        return new FoodSearchResult(foundFoods);
    }

    private boolean searchFilterPredicate(Food food, List<String> foodNameWords) {
        return StringUtils.doesStringContainsAllTokens(food.getDescription(), foodNameWords)
                || StringUtils.doesStringContainsAllTokens(food.getIngredients(), foodNameWords)
                || StringUtils.doesStringContainsAllTokens(food.getBrandOwner(), foodNameWords)
                || StringUtils.doesStringContainsAllTokens(food.getFdcId(), foodNameWords);
    }

    @Override
    public FoodDetails getFoodDetailsByFoodId(String foodId) {
        if(foodId == null || foodId.isBlank()) {
            return FoodDetails.nullObject();
        }

        var detailsResult =
                cachedDetails.stream()
                        .filter(d -> d.getFdcId().equals(foodId))
                        .findFirst()
                        .orElse(FoodDetails.nullObject());

        return detailsResult;
    }

    @Override
    public FoodDetails getFoodDetailsByGtinUpc(String gtinUpc) {
        if(gtinUpc == null || gtinUpc.isBlank()) {
            return FoodDetails.nullObject();
        }

        var detailsResult =
                cachedDetails.stream()
                        .filter(d -> d.getGtinUpc().equals(gtinUpc))
                        .findFirst()
                        .orElse(FoodDetails.nullObject());

        return detailsResult;
    }

    @Override
    public boolean hasBeenSearched(String searchInput) {
        if(searchInput == null || searchInput.isBlank()) {
            throw new IllegalArgumentException("Search input is null or empty!");
        }

        return cachedSearches.contains(searchInput);
    }

    @Override
    public synchronized FoodDetails saveFoodDetails(FoodDetails foodDetails) {
        if(foodDetails == null) {
            throw new IllegalArgumentException("FoodDetails are null!");
        }

        try {
            var savedDetails = saveFoodDetailsInternal(foodDetails);
            this.notifyAll();
            return savedDetails;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException("There was problem while writing to food details cache file!", e);
        }
    }

    private FoodDetails saveFoodDetailsInternal(FoodDetails foodDetails) throws IOException {
        if(cachedDetails.contains(foodDetails)) {
            return foodDetails;
        }

        cachedDetails.add(foodDetails);

        var serializedData = gson.toJson(cachedDetails);
        cacheFilesHandler.saveToFoodDetailsFile(serializedData);

        return foodDetails;
    }

    @Override
    public synchronized FoodSearchResult saveFoodSearchResult(FoodSearchResult foodSearchResult,
                                                               String searchInput) {
        if(foodSearchResult == null) {
            throw new IllegalArgumentException("Search result is null!");
        }

        try {
            saveSearch(searchInput);
            var savedResults =  saveFoodSearchResultInternal(foodSearchResult);
            this.notifyAll();
            return savedResults;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException("There was problem while writing to foods cache file!", e);
        }
    }

    private FoodSearchResult saveFoodSearchResultInternal(FoodSearchResult foodSearchResult) throws IOException {
        var oldCacheSize = cachedFoods.size();
        cachedFoods.addAll(foodSearchResult.getFoods());

        if(cachedFoods.size() == oldCacheSize) {
            //There was no new entries to be added in the cache.
            return foodSearchResult;
        }

        var serializedData = gson.toJson(cachedFoods);
        cacheFilesHandler.saveToFoodsFile(serializedData);
        return foodSearchResult;
    }

    private void saveSearch(String search) throws IOException {
        if(cachedSearches.contains(search)) {
            return;
        }

        cachedSearches.add(search);

        var serializedData = gson.toJson(cachedSearches);
        cacheFilesHandler.saveToPastSearchesFile(serializedData);
    }
}