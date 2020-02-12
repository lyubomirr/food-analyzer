package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.barcode;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface BarcodeReader {
    String getBarcodeFromImage(String imagePath) throws IOException;
}
