package at.htl.rocketman.repository;

import at.htl.rocketman.Datasource;
import at.htl.rocketman.MqttPrimaryMissionConsumer;
import at.htl.rocketman.entity.DataSet;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class DataSetRepository {

    private static final Logger LOG = Logger.getLogger(MqttPrimaryMissionConsumer.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public void persist(DataSet dataSet) {
        Datasource ds = new Datasource();
        try {
            Connection conn = ds.getDb();
            LOG.info("Connected.");
            LOG.info("Description: " + dataSet.getDescription() + "; Value: " + dataSet.getValue() + "; Unit: " + dataSet.getUnit() + "; Timestamp: " + dataSet.getTimestamp() + "; " + LocalDateTime.now());
            String persistSqlString = "INSERT INTO temperature VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(persistSqlString);
            preparedStatement.setString(1, dataSet.getDescription());
            preparedStatement.setString(2, dataSet.getValue());
            preparedStatement.setString(3, dataSet.getUnit());
            preparedStatement.setString(4, dataSet.getTimestamp().format(formatter));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
    }

    public List<DataSet> getAll() {

        List<DataSet> res = new LinkedList<>();
        Datasource ds = new Datasource();
        try {
            Connection conn = ds.getDb();
            LOG.info("Connected.");
            String persistSqlString = "SELECT ds_description, ds_value, ds_unit, ds_timestamp FROM data_set";
            PreparedStatement preparedStatement = conn.prepareStatement(persistSqlString);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DataSet dataSet = new DataSet();
                dataSet.setDescription(resultSet.getString("ds_description"));
                dataSet.setValue(resultSet.getString("ds_value"));
                dataSet.setUnit(resultSet.getString("ds_unit"));
                dataSet.setTimestamp(LocalDateTime.parse(resultSet.getString("ds_timestamp"), formatter));
                res.add(dataSet);
            }
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
        return res;
    }
}
