package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache.CacheFilesHandlerImpl;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache.FoodDataCacheImpl;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.CommandInvokerImpl;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.CommandMapperImpl;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.CommandNames;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.barcode.BarcodeReaderImpl;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers.GetFoodByBarcodeCommand;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers.GetFoodCommand;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers.GetFoodReportCommand;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandMapper;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.configuration.ServerConfiguration;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.CachedFoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClientImpl;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodSearchResult;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.LabelNutrients;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.NutrientsDeserializer;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;

class Startup {

    public static void startServer() {
        try (var serverSocketChannel = ServerSocketChannel.open()) {
            var selector = Selector.open();

            prepareServerSocketChannel(serverSocketChannel, selector);
            ServerImplementation server = createServer();

            server.start(selector);

        } catch (Exception e) {
            System.out.println("An unexpected error occurred while running the server!");
            e.printStackTrace();
        }
    }

    private static void prepareServerSocketChannel(ServerSocketChannel channel, Selector selector)
            throws IOException {
        channel.bind(new InetSocketAddress(ServerConfiguration.SERVER_HOST, ServerConfiguration.SERVER_PORT));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println(String.format(ServerMessages.SERVER_STARTED, ServerConfiguration.SERVER_PORT));
    }

    private static ServerImplementation createServer() {
        var buffer = ByteBuffer.allocate(ServerConfiguration.BUFFER_SIZE);
        var commandHandler = new CommandInvokerImpl(createCommandMapper());

        return new ServerImplementation(buffer, commandHandler);
    }

    private static CommandMapper createCommandMapper()
    {
        var foodApiClient = createApiClient();

        var getFoodCommand = new GetFoodCommand(foodApiClient);
        var getFoodReportCommand = new GetFoodReportCommand(foodApiClient);
        var getFoodByBarcodeCommand = new GetFoodByBarcodeCommand(foodApiClient, new BarcodeReaderImpl());

        var commandMap = new HashMap<String, CommandHandler>();

        commandMap.put(CommandNames.GET_FOOD, getFoodCommand);
        commandMap.put(CommandNames.GET_FOOD_REPORT, getFoodReportCommand);
        commandMap.put(CommandNames.GET_FOOD_BY_BARCODE, getFoodByBarcodeCommand);

        return new CommandMapperImpl(commandMap);
    }

    private static FoodApiClient createApiClient() {
        var apiGson = createGsonForApiResponse();

        var cacheFilesHandler = new CacheFilesHandlerImpl(ServerConfiguration.FOOD_CACHE_FILE_PATH,
                ServerConfiguration.FOOD_DETAILS_CACHE_FILE_PATH, ServerConfiguration.SEARCHES_CACHE_FILE_PATH);

        var apiClient = new FoodApiClientImpl(HttpClient.newHttpClient(), apiGson);
        var foodCache = new FoodDataCacheImpl(cacheFilesHandler, new Gson());

        return new CachedFoodApiClient(apiClient, foodCache);
    }

    private static Gson createGsonForApiResponse() {
        var gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LabelNutrients.class, new NutrientsDeserializer());
        return gsonBuilder.create();
    }
}