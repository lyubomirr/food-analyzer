package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.TestObjects;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetFoodReportCommandTest {
    private CommandHandler getReportCommand;
    private FoodApiClient foodApiClientMock;

    @Before
    public void setUp() {
        foodApiClientMock = mock(FoodApiClient.class);
        getReportCommand = new GetFoodReportCommand(foodApiClientMock);
    }

    @Test
    public void executeShouldReturnRightResults() throws ExecutionException, InterruptedException {
        final var foodId = "12345";
        final var gtin = "123";

        var expected = TestObjects.getFoodDetails(foodId, gtin);

        when(foodApiClientMock.getDetails(foodId))
                .thenReturn(CompletableFuture.completedFuture(expected));

        var result = getReportCommand.execute(foodId);
        assertEquals(expected.toString(), result.get());
    }

    @Test
    public void executeShouldReturnErrorIfArgumentsNull() throws ExecutionException, InterruptedException {
        var response = getReportCommand.execute(null);
        assertEquals(ServerMessages.GET_FOOD_REPORT_WRONG_FORMAT, response.get());
    }

    @Test
    public void executeShouldReturnErrorIfArgumentsInvalidCount() throws ExecutionException, InterruptedException {
        final var args = "so much args man";
        var response = getReportCommand.execute(args);
        assertEquals(ServerMessages.GET_FOOD_REPORT_WRONG_FORMAT, response.get());
    }

    @Test
    public void executeShouldReturnErrorIfFoodIdNotNumber() throws ExecutionException, InterruptedException {
        final var args = "foodId";
        var response = getReportCommand.execute(args);
        assertEquals(ServerMessages.INVALID_FOOD_ID, response.get());
    }


}