package nel.marco;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileHandlerUtil {


    public static void writeFile(String fileToBeDecrypted, String text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToBeDecrypted))) {
            bw.write(text.replaceAll("newLine", "\r\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static StringBuilder readFile(String fileToBeRead) {
        StringBuilder sb = new StringBuilder();

        try {


            List<String> strings = Files.readAllLines(Paths.get(fileToBeRead));

            strings.forEach(s -> sb.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb;
    }
}
