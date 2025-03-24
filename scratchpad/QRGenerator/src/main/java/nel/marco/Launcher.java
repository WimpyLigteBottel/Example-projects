package nel.marco;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Launcher {

  public static void main(String[] args) {
    SpringApplication.run(Launcher.class, args);
  }
}

@RestController
class RestEndpoint {

  private static final String PATH = "C:\\temp\\QR.png";

  @GetMapping("/generate")
  public static void generateQRCodeImage(
      @RequestParam String text,
      @RequestParam(defaultValue = "600") int width,
      @RequestParam(defaultValue = "800") int height,
      @RequestParam(defaultValue = PATH) String filePath)
      throws WriterException, IOException {

    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

    Path path = FileSystems.getDefault().getPath(filePath);
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
  }

  @GetMapping("/qr/generate")
  public static byte[] getQRCodeImage(
      @RequestParam String text,
      @RequestParam(defaultValue = "600") int width,
      @RequestParam(defaultValue = "800") int height) {
    byte[] image = new byte[0];

    try {
      image = createQRbyteArray(text, width, height);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return image;
  }

  @GetMapping("/qr/decode")
  public static String decodeImage(@RequestParam(defaultValue = PATH) String path) {
    String qrcode = null;
    try {

      BinaryBitmap binaryBitmap =
          new BinaryBitmap(
              new HybridBinarizer(
                  new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
      Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, getMap());
      return qrCodeResult.getText();

      // generateQRCodeImage(text, width, height, PATH);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Convert Byte Array into Base64 Encode String

    return qrcode;
  }

  public static Map getMap() {
    Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    return hintMap;
  }

  private static byte[] createQRbyteArray(String text, int width, int height)
      throws WriterException, IOException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    MatrixToImageConfig con = new MatrixToImageConfig(0xFF000002, 0xFFFFC041);

    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, con);
    byte[] pngData = pngOutputStream.toByteArray();
    return pngData;
  }
}
