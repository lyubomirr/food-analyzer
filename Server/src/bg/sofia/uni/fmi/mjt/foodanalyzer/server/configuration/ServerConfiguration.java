package bg.sofia.uni.fmi.mjt.foodanalyzer.server.configuration;

public class ServerConfiguration {
    public static final int BUFFER_SIZE = 1024*1024;
    public static final int SERVER_PORT = 7777;
    public static final String SERVER_HOST = "localhost";
    public static final String FOOD_API_KEY = "your-api-key";
    public static final String FOOD_CACHE_FILE_PATH = "foods.json";
    public static final String FOOD_DETAILS_CACHE_FILE_PATH = "foodDetails.json";
    public static final String SEARCHES_CACHE_FILE_PATH = "searches.json";
}