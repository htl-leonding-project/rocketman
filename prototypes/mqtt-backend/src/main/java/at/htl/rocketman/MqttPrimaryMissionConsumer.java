package at.htl.rocketman;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@ApplicationScoped
public class MqttPrimaryMissionConsumer {

    private static final Logger LOG = Logger.getLogger(MqttPrimaryMissionConsumer.class);

    @Incoming("temperature")
    public void consumeTemperature(byte[] raw) throws SQLException {
        Datasource ds = new Datasource();
        Connection connection = ds.getDb();
        LOG.error(connection);
        Double temp = null;
        try {
            LOG.error("parse value");
            temp = Double.parseDouble(new String(raw));
        } catch (Exception e) {
            LOG.error("Failed to parse value");
        }
        if(temp != null) {
            LOG.error("prepare statement");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO temperature VALUES (?, ?)");
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().toString()));
            preparedStatement.setDouble(2, temp);
            preparedStatement.executeUpdate();
            //LOG.info("Temperature: " + temp + "; " + LocalDateTime.now());
            //ExportController.writeToCsv(new String[] {temp.toString(), LocalDateTime.now().toString()}, "temperature.csv");
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
