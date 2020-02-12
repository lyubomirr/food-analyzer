package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.TestObjects;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetFoodCommandTest {
    private CommandHandler getFoodCommand;
    private FoodApiClient foodApiClientMock;

    @Before
    public void setUp() {
        foodApiClientMock = mock(FoodApiClient.class);
        getFoodCommand = new GetFoodCommand(foodApiClientMock);
    }


    @Test
    public void executeShouldReturnRightResults() throws ExecutionException, InterruptedException {
        final var searchInput = "some food";
        final var descrption = "description";

        var foods = new ArrayList<Food>();
        foods.add(TestObjects.getFood(descrption));
        var expected = new FoodSearchResult(foods);

        when(foodApiClientMock.search(searchInput))
                .thenReturn(CompletableFuture.completedFuture(expected));

        var result = getFoodCommand.execute(searchInput);
        assertEquals(expected.toString(), result.get());
    }

    @Test
    public void executeShouldReturnErrorIfArgumentsNull() throws ExecutionException, InterruptedException {
        var response = getFoodCommand.execute(null);
        assertEquals(ServerMessages.GET_FOOD_WRONG_FORMAT, response.get());
    }
}