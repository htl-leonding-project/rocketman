package at.htl.rocketman.repository;

import at.htl.rocketman.entity.CanSatConfiguration;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;

@ApplicationScoped
public class CanSatConfigurationRepository {
    final String FILENAME = "config.csv";

    public void writeConfig(CanSatConfiguration config) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            StringBuilder sb = new StringBuilder();

            sb.append(config.getName()).append(";");
            sb.append(config.getCountdown()).append(";");
            sb.append(config.getIgniter()).append(";");
            sb.append(config.getResistance()).append(";");
            sb.append(config.isUseJoyStick()).append(";");
            sb.append(config.isUseJoyStick());

            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CanSatConfiguration readConfig() {
        CanSatConfiguration res = new CanSatConfiguration();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String[] e = reader.readLine().split(";");
            res = new CanSatConfiguration(e[0], Integer.parseInt(e[1]), Integer.parseInt(e[2]), e[3], Boolean.parseBoolean(e[4]), Boolean.parseBoolean(e[5]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
