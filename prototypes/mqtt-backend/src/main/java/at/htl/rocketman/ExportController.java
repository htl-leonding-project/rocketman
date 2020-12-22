package at.htl.rocketman;

import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class ExportController {
    private static final Logger LOG = Logger.getLogger(MqttConsumer.class);

    public static void writeToCsv(String[] data, String fileName) {
        if  (data.length > 0) {
            try {
                Path path = Paths.get(fileName);
                String contentToAppend = "Spain\r\n";
                File file = path.toFile();
                if (!file.exists()) {
                    file.createNewFile();
                    LOG.info("File " + fileName + " has been created.");
                }
                Files.write(
                        path,
                        (Arrays.stream(data).skip(1).reduce(data[0], (s1, s2) -> s1 + "; " + s2) + "\n").getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
