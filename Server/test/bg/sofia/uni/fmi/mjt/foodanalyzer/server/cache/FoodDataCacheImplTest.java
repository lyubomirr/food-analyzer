package bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.TestObjects;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FoodDataCacheImplTest {
    private CacheFilesHandler handlerMock;
    private Gson gson;

    @Before
    public void setUp() {
        gson = new Gson();
        handlerMock = mock(CacheFilesHandler.class);
    }

    @Test
    public void searchByNameShouldSearchSuccessfully() throws IOException {
        final var description = "description";
        final var food = TestObjects.getFood(description);
        final var foods = new ArrayList<Food>();
        foods.add(food);

        final var serialized = gson.toJson(foods);
        when(handlerMock.readFoodsFileContent()).thenReturn(serialized);

        var cache = new FoodDataCacheImpl(handlerMock, gson);

        var expected = new FoodSearchResult(foods);
        var actual = cache.searchByName(description);

        assertEquals(expected, actual);
    }

    @Test
    public void getFoodDetailsByFoodIdShouldReturnRightResult() throws IOException {
        final var foodId = "123123";
        final var gtin = "123123";

        var foodDetails = TestObjects.getFoodDetails(foodId, gtin);
        var details = new ArrayList<FoodDetails>();
        details.add(foodDetails);

        var serialized = gson.toJson(details);
        when(handlerMock.readFoodDetailsFileContent()).thenReturn(serialized);
        var cache = new FoodDataCacheImpl(handlerMock, gson);

        var actual = cache.getFoodDetailsByFoodId(foodId);
        assertEquals(foodDetails, actual);
    }

    @Test
    public void getFoodDetailsByGtinUpcShouldReturnRightResult() throws IOException {
        final var foodId = "123123";
        final var gtin = "123123";

        var foodDetails = TestObjects.getFoodDetails(foodId, gtin);
        var details = new ArrayList<FoodDetails>();
        details.add(foodDetails);

        var serialized = gson.toJson(details);
        when(handlerMock.readFoodDetailsFileContent()).thenReturn(serialized);
        var cache = new FoodDataCacheImpl(handlerMock, gson);

        var actual = cache.getFoodDetailsByGtinUpc(gtin);
        assertEquals(foodDetails, actual);
    }

    @Test
    public void hasBeenSearchedShouldReturnTrueIfSearched() throws IOException {
        final var pastSearch = "something";
        final var searches = new String[] { pastSearch };

        var serialized = gson.toJson(searches);
        when(handlerMock.readPastSearchesFile()).thenReturn(serialized);
        var cache = new FoodDataCacheImpl(handlerMock, gson);

        var actual = cache.hasBeenSearched(pastSearch);
        assertTrue(actual);
    }

    @Test
    public void hasBeenSearchedShouldReturnFalseIfNot() throws IOException {
        final var pastSearch = "something";
        final var searches = new String[] { };

        var serialized = gson.toJson(searches);
        when(handlerMock.readPastSearchesFile()).thenReturn(serialized);
        var cache = new FoodDataCacheImpl(handlerMock, gson);

        var actual = cache.hasBeenSearched(pastSearch);
        assertFalse(actual);
    }
}