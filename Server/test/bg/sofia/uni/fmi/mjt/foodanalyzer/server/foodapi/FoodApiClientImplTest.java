package bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.TestObjects;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FoodApiClientImplTest {
    private FoodApiClient foodApiClient;
    private HttpClient clientMock;
    private Gson gson;
    private HttpResponse responseMock;

    @Before
    public void setUp() {
        clientMock = mock(HttpClient.class);

        gson = new Gson();
        responseMock = mock(HttpResponse.class);

        final var success = 200;
        when(responseMock.statusCode()).thenReturn(success);

        foodApiClient = new FoodApiClientImpl(clientMock, gson);
    }

    @Test
    public void searchShouldReturnRightResults() throws ExecutionException, InterruptedException {
        final var searchResult = new FoodSearchResult();
        final var serializedResult = gson.toJson(searchResult);

        when(responseMock.body()).thenReturn(serializedResult);

        when(clientMock.sendAsync(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(CompletableFuture.completedFuture(responseMock));

        final var search = "smth";
        var result = foodApiClient.search(search);

        assertEquals(searchResult, result.get());
    }

    @Test
    public void getDetailsShouldReturnRightResults() throws ExecutionException, InterruptedException {
        final var foodId = "1234";
        final var gtin = "123";

        final var foodDetails = TestObjects.getFoodDetails(foodId, gtin);
        final var serializedResult = gson.toJson(foodDetails);

        when(responseMock.body()).thenReturn(serializedResult);

        when(clientMock.sendAsync(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(CompletableFuture.completedFuture(responseMock));

        var result = foodApiClient.getDetails(foodId);
        assertEquals(foodDetails, result.get());
    }
}