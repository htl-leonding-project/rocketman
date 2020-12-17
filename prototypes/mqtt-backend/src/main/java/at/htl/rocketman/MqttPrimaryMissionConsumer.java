package at.htl.rocketman;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class MqttPrimaryMissionConsumer {

    private static final Logger LOG = Logger.getLogger(MqttPrimaryMissionConsumer.class);

    @Incoming("temperature")
    public void consumeTemperature(byte[] raw) throws SQLException {
        Datasource ds = new Datasource();
        Double temp = null;
        try {
            temp = Double.parseDouble(new String(raw));
        } catch (Exception e) {
            LOG.error("Failed to parse value");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        if(temp != null) {
            LOG.info("Temperature: " + temp + "; " + LocalDateTime.now().format(formatter));
            //ExportController.writeToCsv(new String[] {temp.toString(), LocalDateTime.now().toString()}, "temperature.csv");
            try {
                Connection conn = ds.getDb();
                LOG.info("Connected.");
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO temperature VALUES (?, ?)");
                preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().format(formatter)));
                preparedStatement.setDouble(2, temp);
                preparedStatement.executeUpdate();
                conn.close();
            } catch (SQLException e) {
                LOG.error("SQl-Error occured: " + e.getMessage());
            } catch (Exception e) {
                LOG.error("Unknown error occured: " + e.getMessage());
            }
        }
    }

    @Incoming("atmospheric-pressure")
    public void consumeAtmosphericPressure(byte[] raw) {
        Float pressure = null;
        try {
            pressure = Float.parseFloat(new String(raw));
        } catch (Exception e) {
            LOG.error("Failed to parse value");
        }
        if(pressure != null) {
            LOG.info("Pressure: " + pressure + "; " + LocalDateTime.now());
            ExportController.writeToCsv(new String[] {pressure.toString(), LocalDateTime.now().toString()}, "atmospheric-pressure.csv");
        }
    }
}
