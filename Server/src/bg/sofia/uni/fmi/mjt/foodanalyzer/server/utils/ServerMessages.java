package bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils;

public class ServerMessages {
    public static final String SERVER_STARTED= "Server listening on port %d.";
    public static final String NO_COMMAND_ENTERED = "No command entered!";
    public static final String NO_SUCH_COMMAND = "The command you've entered is not supported!";
    public static final String RESULTS_FOUND = "%d results found. \n";
    public static final String GET_FOOD_WRONG_FORMAT = "Wrong command format. " +
            "Should be \"get-food <searchInput>\".";
    public static final String GET_FOOD_REPORT_WRONG_FORMAT =
            "Wrong command format. Should be \"get-food-report <foodId>\".";
    public static final String INVALID_FOOD_ID = "Food id is invalid. Must be a valid number.";
    public static final String SERVER_EXCEPTION_MSG = "Exception occured in server!";
    public static final String GET_FOOD_BY_BARCODE_WRONG_FORMAT = "Wrong command format. " +
            "Should be \"get-food-by-barcode --code=<gtinUpc> | --img=<barcode_image_file>\".";
    public static final String INVALID_BARCODE = "Barcode is invalid!";
    public static final String CANT_FIND_BARCODE = "Please supply valid barcode argument!";
    public static final String INVALID_FILE = "Couldn't read barcode file!";
}