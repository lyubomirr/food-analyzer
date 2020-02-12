package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.barcode.BarcodeReader;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BarcodeNotFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.LabelNutrients;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetFoodByBarcodeCommandTest {
    private CommandHandler getByBarcodeCommand;
    private FoodApiClient foodApiClientMock;
    private BarcodeReader barcodeReaderMock;

    @Before
    public void setUp() {
        foodApiClientMock = mock(FoodApiClient.class);
        barcodeReaderMock = mock(BarcodeReader.class);
        getByBarcodeCommand = new GetFoodByBarcodeCommand(foodApiClientMock, barcodeReaderMock);
    }

    @Test
    public void executeShouldReturnRightResultWithCode() throws ExecutionException, InterruptedException {
        final var args = "--code=1234";
        final var barcode = "1234";

        var expected = new FoodDetails("3213", barcode, "desc", "ingr",
                3, "mg", LabelNutrients.nullObject());

        when(foodApiClientMock.getDetailsByBarcode(barcode))
                .thenReturn(CompletableFuture.completedFuture(expected));

        var result = getByBarcodeCommand.execute(args);
        assertEquals(expected.toString(), result.get());
    }

    @Test
    public void executeShouldReturnRightResultWithImage() throws ExecutionException,
            InterruptedException, IOException {
        final var args = "--img=somePath";
        final var barcode = "1234";
        final var imgPath = "somePath";

        var expected = new FoodDetails("3213", barcode, "desc", "ingr",
                3, "mg", LabelNutrients.nullObject());

        when(barcodeReaderMock.getBarcodeFromImage(imgPath)).thenReturn(barcode);

        when(foodApiClientMock.getDetailsByBarcode(barcode))
                .thenReturn(CompletableFuture.completedFuture(expected));

        var result = getByBarcodeCommand.execute(args);
        assertEquals(expected.toString(), result.get());
    }

    @Test
    public void executeShouldReturnErrorIfCodeNotNumeric() throws ExecutionException,
            InterruptedException, IOException {
        final var args = "--code=qweqwee";

        var result = getByBarcodeCommand.execute(args);
        assertEquals(ServerMessages.CANT_FIND_BARCODE, result.get());
    }

    @Test
    public void executeShouldReturnErrorIfArgsNull() throws ExecutionException, InterruptedException {
        var result = getByBarcodeCommand.execute(null);
        assertEquals(ServerMessages.GET_FOOD_BY_BARCODE_WRONG_FORMAT, result.get());
    }

    @Test
    public void executeShouldReturnErrorIfCannotFindFile() throws ExecutionException,
            InterruptedException, IOException {
        final var imgPath = "path";
        final var args = "--img=" + imgPath;

        when(barcodeReaderMock.getBarcodeFromImage(imgPath)).thenThrow(IOException.class);

        var result = getByBarcodeCommand.execute(args);
        assertEquals(ServerMessages.INVALID_FILE, result.get());
    }

    @Test
    public void executeShouldReturnErrorIfCannotDecodeImage() throws ExecutionException,
            InterruptedException, IOException {
        final var imgPath = "path";
        final var args = "--img=" + imgPath;

        when(barcodeReaderMock.getBarcodeFromImage(imgPath)).thenThrow(BarcodeNotFoundException.class);

        var result = getByBarcodeCommand.execute(args);
        assertEquals(ServerMessages.INVALID_BARCODE, result.get());
    }
}