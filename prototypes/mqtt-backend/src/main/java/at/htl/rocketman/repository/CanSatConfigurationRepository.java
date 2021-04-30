package at.htl.rocketman.repository;

import at.htl.rocketman.entity.CanSatConfiguration;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CanSatConfigurationRepository {
    final String FILENAME = "config.csv";

    public void writeConfig(CanSatConfiguration config) {
        File file = new File(FILENAME);
        try {
            if (file.createNewFile()) {
                Files.write(Paths.get(FILENAME), "name;countdown;igniter;resistance;useJoyStick;useVideo\n".getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try  {
            StringBuilder sb = new StringBuilder();

            sb.append(config.getName()).append(";");
            sb.append(config.getCountdown()).append(";");
            sb.append(config.getIgniter()).append(";");
            sb.append(config.getResistance()).append(";");
            sb.append(config.isUseJoyStick()).append(";");
            sb.append(config.isUseJoyStick()).append("\n");

            Files.write(Paths.get(FILENAME), sb.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CanSatConfiguration> readConfig() {
        File file = new File(FILENAME);
        List<CanSatConfiguration> res = new LinkedList<CanSatConfiguration>();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lines = lines.stream().skip(1).collect(Collectors.toList());
        for (String line : lines) {
            String[] e = line.split(";");
            res.add(new CanSatConfiguration(e[0], Integer.parseInt(e[1]), Integer.parseInt(e[2]), e[3], Boolean.parseBoolean(e[4]), Boolean.parseBoolean(e[5])));
        }
        return res;
    }
}
