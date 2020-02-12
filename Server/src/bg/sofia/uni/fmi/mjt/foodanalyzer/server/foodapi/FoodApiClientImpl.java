package bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodApiServerErrorException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.InvalidFoodApiRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.ErrorResponse;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class FoodApiClientImpl implements FoodApiClient {
    private final Gson gson;
    private final HttpClient httpClient;

    public FoodApiClientImpl(HttpClient httpClient, Gson gson) {
        this.httpClient = httpClient;
        this.gson = gson;
    }

    @Override
    public CompletableFuture<FoodSearchResult> search(String searchInput) {
        var url = Endpoints.createSearchUrl(searchInput);
        return makeHttpGetRequest(url, FoodSearchResult.class);
    }

    @Override
    public CompletableFuture<FoodDetails> getDetails(String foodId) {
        var url = Endpoints.createDetailsUrl(foodId);
        return makeHttpGetRequest(url, FoodDetails.class);
    }

    @Override
    public CompletableFuture<FoodDetails> getDetailsByBarcode(String gtinUpc) {
        throw new UnsupportedOperationException("Cannot get food details by barcode if not cached!");
    }

    private <TResponse> CompletableFuture<TResponse> makeHttpGetRequest(URI url, Class<TResponse> responseClass) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::httpErrorHandler)
                .thenApply(HttpResponse::body)
                .thenApply(body -> gson.fromJson(body, responseClass));

    }

    private HttpResponse<String> httpErrorHandler(HttpResponse<String> response) {
        if (HttpHelperMethods.isHttpClientError(response.statusCode())) {
            var responseMessage = gson.fromJson(response.body(), ErrorResponse.class);
            throw new InvalidFoodApiRequestException(responseMessage);
        }

        if (HttpHelperMethods.isHttpServerError(response.statusCode())) {
            var responseMessage = gson.fromJson(response.body(), ErrorResponse.class);
            throw new FoodApiServerErrorException(responseMessage);
        }

        return response;
    }
}