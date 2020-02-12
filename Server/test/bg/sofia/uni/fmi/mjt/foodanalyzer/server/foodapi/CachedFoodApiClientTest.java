package bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.TestObjects;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache.FoodDataCache;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CachedFoodApiClientTest {
    private static final int ONE_INVOCATION = 1;
    private static final int NO_INVOCATION = 0;

    private FoodApiClient cachedFoodApiClient;
    private FoodApiClient foodApiClientMock;
    private FoodDataCache foodDataCacheMock;

    @Before
    public void setUp() {

        foodApiClientMock = mock(FoodApiClient.class);

        foodDataCacheMock = mock(FoodDataCache.class);
        when(foodDataCacheMock.saveFoodDetails(any(FoodDetails.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        when(foodDataCacheMock.saveFoodSearchResult(any(FoodSearchResult.class), anyString()))
                .then(AdditionalAnswers.returnsFirstArg());

        cachedFoodApiClient = new CachedFoodApiClient(foodApiClientMock, foodDataCacheMock);
    }


    @Test
    public void searchShouldCallInnerClientIfNotBeenSearched() throws ExecutionException, InterruptedException {
        final var search = "smth";
        var expected = new FoodSearchResult();

        when(foodDataCacheMock.hasBeenSearched(search)).thenReturn(false);
        when(foodApiClientMock.search(search)).thenReturn(CompletableFuture.completedFuture(expected));

        var actual = cachedFoodApiClient.search(search);

        verify(foodApiClientMock, times(ONE_INVOCATION)).search(search);
        assertEquals(expected, actual.get());
    }

    @Test
    public void searchShouldReturnResultIfFound() throws ExecutionException, InterruptedException {
        final var search = "smth";
        var expected = new FoodSearchResult();

        when(foodDataCacheMock.hasBeenSearched(search)).thenReturn(true);
        when(foodDataCacheMock.searchByName(search)).thenReturn(expected);

        var actual = cachedFoodApiClient.search(search);

        verify(foodApiClientMock, times(NO_INVOCATION)).search(search);
        assertEquals(expected, actual.get());
    }


    @Test
    public void searchDetailsShouldCallInnerClientIfNotFound() throws ExecutionException, InterruptedException {
        final var foodId = "1234";
        final var gtin = "1234";
        var expected = TestObjects.getFoodDetails(foodId, gtin);

        when(foodDataCacheMock.getFoodDetailsByFoodId(foodId)).thenReturn(FoodDetails.nullObject());
        when(foodApiClientMock.getDetails(foodId)).thenReturn(CompletableFuture.completedFuture(expected));

        var actual = cachedFoodApiClient.getDetails(foodId);
        assertEquals(expected, actual.get());
    }

    @Test
    public void searchDetailsShouldGetFromCacheIfFound() throws ExecutionException, InterruptedException {
        final var foodId = "1234";
        final var gtin = "1234";
        var expected = TestObjects.getFoodDetails(foodId, gtin);

        when(foodDataCacheMock.getFoodDetailsByFoodId(foodId)).thenReturn(expected);
        var actual = cachedFoodApiClient.getDetails(foodId);

        verify(foodApiClientMock, times(NO_INVOCATION)).getDetails(foodId);
        assertEquals(expected, actual.get());
    }

    @Test
    public void searchByBarcodeShouldGetFromCache() throws ExecutionException, InterruptedException {
        final var foodId = "1234";
        final var gtin = "1234";
        var expected = TestObjects.getFoodDetails(foodId, gtin);

        when(foodDataCacheMock.getFoodDetailsByGtinUpc(gtin)).thenReturn(expected);
        var actual = cachedFoodApiClient.getDetailsByBarcode(gtin);
        assertEquals(expected, actual.get());
    }
}