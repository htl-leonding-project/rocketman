package at.htl.rocketman;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequestScoped
public class MqttPrimaryMissionConsumer {

    private static final Logger LOG = Logger.getLogger(MqttPrimaryMissionConsumer.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

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
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO temperature VALUES (?, ?)");
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
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO atmospheric_pressure VALUES (?, ?)");
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
}
