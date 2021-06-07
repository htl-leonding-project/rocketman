package at.htl.rocketman.repository;

import at.htl.rocketman.Datasource;
import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.entity.Start;
import io.vertx.ext.web.common.template.test;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class DataSetRepository {

    @Inject
    Logger LOG;

    @Inject
    StartRepository startRepository;

    public void persist(DataSet dataSet) {
        Start lastStart = startRepository.getLastUnfinishedStart();
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            String persistSqlString = "INSERT INTO data_set VALUES (null, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(persistSqlString);
            preparedStatement.setString(1, dataSet.getDescription().toLowerCase());
            preparedStatement.setString(2, dataSet.getValue());
            preparedStatement.setString(3, dataSet.getUnit());
            preparedStatement.setString(4, dataSet.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            if (dataSet.getStart() == null) {
                preparedStatement.setLong(5, lastStart.getId());
            } else {
                preparedStatement.setLong(5, dataSet.getStart().getId());
            }
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
        try (Connection conn = ds.getDb()) {
            LOG.info("Connected.");
            String getAllSqlString = "SELECT ds_description, ds_value, ds_unit, ds_timestamp, ds_st_id FROM data_set";
            PreparedStatement preparedStatement = conn.prepareStatement(getAllSqlString);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DataSet dataSet = new DataSet();
                dataSet.setDescription(resultSet.getString("ds_description"));
                dataSet.setValue(resultSet.getString("ds_value"));
                dataSet.setUnit(resultSet.getString("ds_unit"));
                dataSet.setTimestamp(LocalDateTime.parse(resultSet.getString("ds_timestamp"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                dataSet.setStart(new Start());
                dataSet.getStart().setId(resultSet.getLong("ds_st_id"));
                res.add(dataSet);
            }
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
        res.forEach(d -> {
            d.setStart(startRepository.findById(d.getStart().getId()));
        });
        return res;
    }

    public List<DataSet> findByDescription(String description) {
        List<DataSet> res = new LinkedList<>();
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            String sqlString = "SELECT ds_description, ds_value, ds_unit, ds_timestamp FROM data_set WHERE ds_description = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setString(1, description.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DataSet dataSet = new DataSet();
                dataSet.setDescription(resultSet.getString("ds_description"));
                dataSet.setValue(resultSet.getString("ds_value"));
                dataSet.setUnit(resultSet.getString("ds_unit"));
                dataSet.setTimestamp(LocalDateTime.parse(resultSet.getString("ds_timestamp"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                res.add(dataSet);
            }
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
        return res;
    }

    public List<String> getAllDescriptions() {
        List<String> res = new LinkedList<>();
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            String sqlString = "SELECT DISTINCT ds_description FROM data_set";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                res.add(resultSet.getString("ds_description"));
            }
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
        return res;
    }

    public String getUnitForDescription(String description) {
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            String sqlString = "SELECT DISTINCT ds_unit FROM data_set WHERE ds_description = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setString(1, description.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ds_unit");
            }
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
        return null;
    }
}
