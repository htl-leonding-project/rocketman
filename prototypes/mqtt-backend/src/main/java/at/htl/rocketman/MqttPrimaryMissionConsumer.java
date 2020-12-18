package at.htl.rocketman;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequestScoped
public class MqttPrimaryMissionConsumer {

    private static final Logger LOG = Logger.getLogger(MqttPrimaryMissionConsumer.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    private static final String temperatureSqlString = "INSERT INTO temperature VALUES (?, ?)";
    private static final String pressureSqlString = "INSERT INTO atmospheric_pressure VALUES (?, ?)";

    @Incoming("temperature")
    public void consumeTemperature(byte[] raw) {
        Datasource ds = new Datasource();
        Double temp = null;
        try {
            temp = Double.parseDouble(new String(raw));
        } catch (Exception e) {
            LOG.error("Failed to parse value");
        }
        if(temp != null) {
            LOG.info("Temperature: " + temp + "; " + LocalDateTime.now().format(formatter));
            //ExportController.writeToCsv(new String[] {temp.toString(), LocalDateTime.now().toString()}, "temperature.csv");
            try {
                Connection conn = ds.getDb();
                LOG.info("Connected.");
                PreparedStatement preparedStatement = conn.prepareStatement(temperatureSqlString);
                preparedStatement.setString(1, LocalDateTime.now().format(formatter));
                preparedStatement.setDouble(2, temp);
                preparedStatement.executeUpdate();
                //conn.close();
            } catch (SQLException e) {
                LOG.error("SQl-Error occurred: " + e.getMessage());
            } catch (Exception e) {
                LOG.error("Unknown error occurred: " + e.getMessage());
            }
        }
    }

    @Incoming("atmospheric-pressure")
    public void consumeAtmosphericPressure(byte[] raw) {
        Datasource ds = new Datasource();
        Float pressure = null;
        try {
            pressure = Float.parseFloat(new String(raw));
        } catch (Exception e) {
            LOG.error("Failed to parse value");
        }
        if(pressure != null) {
            LOG.info("Pressure: " + pressure + "; " + LocalDateTime.now());
            try {
                Connection conn = ds.getDb();
                LOG.info("Connected.");
                PreparedStatement preparedStatement = conn.prepareStatement(pressureSqlString);
                preparedStatement.setString(1, LocalDateTime.now().format(formatter));
                preparedStatement.setDouble(2, pressure);
                preparedStatement.executeUpdate();
                //conn.close();
            } catch (SQLException e) {
                LOG.error("SQl-Error occurred: " + e.getMessage());
            } catch (Exception e) {
                LOG.error("Unknown error occurred: " + e.getMessage());
            }
        }
    }

    /*
        {
          "temperature": 12,
          "atmospheric-pressure": 1245
        }
     */
    @Incoming("primary-json")
    public void consumePrimaryMissionJson(byte[] raw) {
        ByteArrayInputStream bais = new ByteArrayInputStream(raw);
        JsonReader jsonReader = Json.createReader(bais);
        JsonObject rawJson = jsonReader.readObject();
        Datasource ds = new Datasource();
        Double temp = null;
        Float pressure = null;
        try {
            temp = Double.parseDouble(rawJson.get("temperature").toString());
            pressure =  Float.parseFloat(rawJson.get("atmospheric-pressure").toString());
        } catch (Exception e) {
            LOG.error("Failed to parse value: " + e.getMessage());
        }
        if(temp != null && pressure != null) {
            try {
                Connection conn = ds.getDb();
                LOG.info("Connected.");
                LOG.info("Temperature: " + temp + "; Pressure: " + pressure + "; " + LocalDateTime.now());
                PreparedStatement preparedStatement = conn.prepareStatement(pressureSqlString);
                preparedStatement.setString(1, LocalDateTime.now().format(formatter));
                preparedStatement.setDouble(2, pressure);
                preparedStatement.executeUpdate();
                preparedStatement = conn.prepareStatement(temperatureSqlString);
                preparedStatement.setString(1, LocalDateTime.now().format(formatter));
                preparedStatement.setDouble(2, temp);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("SQl-Error occurred: " + e.getMessage());
            } catch (Exception e) {
                LOG.error("Unknown error occurred: " + e.getMessage());
            }
        }
    }
}
