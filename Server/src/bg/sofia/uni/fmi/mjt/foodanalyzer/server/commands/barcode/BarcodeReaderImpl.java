package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.barcode;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BarcodeNotFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.Constants;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class BarcodeReaderImpl implements BarcodeReader {
    private static final Map<DecodeHintType,Object> HINTS;

    static {
        HINTS = new EnumMap<>(DecodeHintType.class);
        HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
    }

    @Override
    public String getBarcodeFromImage(String imagePath) throws IOException {
        try(var fileStream = new FileInputStream(imagePath)) {
            return getBarcodeFromImageInternal(fileStream);
        } catch (NotFoundException e) {
            throw new BarcodeNotFoundException("Barcode was not found in image", e);
        }
    }

    private String getBarcodeFromImageInternal(InputStream fileStream) throws IOException, NotFoundException {
        var image = ImageIO.read(fileStream);

        //Convert image to bitmap.
        var luminanceSource = new BufferedImageLuminanceSource(image);
        var bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(luminanceSource));

        var reader = new MultiFormatReader();
        Result result = reader.decode(bitmap, HINTS);
        return result.getText();
    }
}
