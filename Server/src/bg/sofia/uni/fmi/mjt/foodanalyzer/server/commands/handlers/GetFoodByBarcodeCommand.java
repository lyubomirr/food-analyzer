package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.Server;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.barcode.BarcodeReader;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BarcodeNotFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.Constants;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class GetFoodByBarcodeCommand extends BaseCommandHandler {
    private static final int MAX_ARGS = 2;
    private static final int ARG_VALUE_GROUP = 1;
    private static final Pattern CODE_PATTERN = Pattern.compile("^--code=([0-9]*)$");
    private static final Pattern IMG_PATTERN = Pattern.compile("^--img=(.*)$");

    private final BarcodeReader barcodeReader;

    public GetFoodByBarcodeCommand(FoodApiClient foodApiClient, BarcodeReader barcodeReader) {
        super(foodApiClient);
        this.barcodeReader = barcodeReader;
    }

    @Override
    public CompletableFuture<String> execute(String arguments) {
        if(arguments == null || arguments.isBlank()) {
            return CompletableFuture.completedFuture(ServerMessages.GET_FOOD_BY_BARCODE_WRONG_FORMAT);
        }

        var splittedArgs = arguments.split(Constants.WHITESPACE_SPLIT_REGEX);
        if(splittedArgs.length == 0 || splittedArgs.length > MAX_ARGS) {
            return CompletableFuture.completedFuture(ServerMessages.GET_FOOD_BY_BARCODE_WRONG_FORMAT);
        }


        try {
            var barcode = getBarcode(splittedArgs);

            if(barcode == null || barcode.isBlank()) {
                return CompletableFuture.completedFuture(ServerMessages.CANT_FIND_BARCODE);
            }

            return foodApiClient.getDetailsByBarcode(barcode)
                    .thenApply(FoodDetails::toString)
                    .exceptionally(this::exceptionHandler);

        } catch (IOException e) {
            System.out.println("Couldn't read barcode file:" + e.getMessage());
            return CompletableFuture.completedFuture(ServerMessages.INVALID_FILE);

        } catch (BarcodeNotFoundException e) {
            System.out.println("Couldn't decode barcode:" + e.getMessage());
            return CompletableFuture.completedFuture(ServerMessages.INVALID_BARCODE);

        }
    }

    private String getBarcode(String[] args) throws IOException {
        var barcode = getArgumentValue(args, CODE_PATTERN);
        if(!barcode.isBlank()) {
            return barcode;
        }

        return getBarcodeFromImage(args);
    }

    private String getArgumentValue(String[] args, Pattern argumentPattern) {
        var codeArgument = Arrays.stream(args)
                .filter(argumentPattern.asMatchPredicate())
                .findFirst().orElse(Constants.EMPTY_STRING);

        var matcher = argumentPattern.matcher(codeArgument);
        if(!matcher.matches()) {
            return Constants.EMPTY_STRING;
        }

        if(matcher.groupCount() < ARG_VALUE_GROUP) {
            return Constants.EMPTY_STRING;
        }

        return matcher.group(ARG_VALUE_GROUP);
    }

    private String getBarcodeFromImage(String[] args) throws IOException {
        var imagePath = getArgumentValue(args, IMG_PATTERN);
        if(imagePath.isBlank()) {
            return Constants.EMPTY_STRING;
        }

        return barcodeReader.getBarcodeFromImage(imagePath);
    }
}