package bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CacheFilesHandlerImpl implements CacheFilesHandler {
    private final Path foodFilePath;
    private final Path detailsFilePath;
    private final Path searchesFilePath;

    public CacheFilesHandlerImpl(String foodFilePath, String detailsFilePath, String searchesFilePath) {
        this.foodFilePath = Path.of(foodFilePath);
        this.detailsFilePath = Path.of(detailsFilePath);
        this.searchesFilePath = Path.of(searchesFilePath);
    }

    @Override
    public String readFoodsFileContent() throws IOException {
        return readFile(foodFilePath);
    }

    @Override
    public String readFoodDetailsFileContent() throws IOException {
        return readFile(detailsFilePath);
    }

    @Override
    public String readPastSearchesFile() throws IOException {
        return readFile(searchesFilePath);
    }

    private static String readFile(Path filePath) throws IOException {
        if(!Files.exists(filePath)) {
            return Constants.EMPTY_STRING;
        }

        var bytes = Files.readAllBytes(filePath);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public void saveToFoodsFile(String content) throws IOException {
        writeStringToFile(foodFilePath, content);
    }

    @Override
    public void saveToFoodDetailsFile(String content) throws IOException {
        writeStringToFile(detailsFilePath, content);
    }

    @Override
    public void saveToPastSearchesFile(String content) throws IOException {
        writeStringToFile(searchesFilePath, content);
    }

    private static void writeStringToFile(Path filePath, String content) throws IOException {
        Files.writeString(filePath, content, StandardOpenOption.CREATE);
    }
}