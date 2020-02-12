package bg.sofia.uni.fmi.mjt.foodanalyzer.server.cache;

import java.io.IOException;

public interface CacheFilesHandler {
    String readFoodsFileContent() throws IOException;
    String readFoodDetailsFileContent() throws IOException;
    String readPastSearchesFile() throws IOException;

    void saveToFoodsFile(String content) throws IOException;
    void saveToFoodDetailsFile(String content) throws IOException;
    void saveToPastSearchesFile(String content) throws IOException;
}
