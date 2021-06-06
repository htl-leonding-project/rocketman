package at.htl.rocketman.repository;

import at.htl.rocketman.Datasource;
import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.entity.Start;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class StartRepository {
    @Inject
    Logger LOG;

    public void persist(Start start) {
        this.finishStarts();
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            String persistSqlString = "INSERT INTO start VALUES (null, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(persistSqlString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, start.getComment().toLowerCase());
            preparedStatement.setString(2, start.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            if (start.getEndDate() == null) {
                preparedStatement.setString(3, null);
            } else {
                preparedStatement.setString(3, start.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            start.setId(rs.getLong(1));
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
    }

    public List<Start> getAll() {
        List<Start> res = new LinkedList<>();
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            LOG.info("Connected.");
            String sqlString = "SELECT st_id, st_comment, st_startDate, st_endStart FROM start";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Start start = new Start();
                start.setId(resultSet.getLong("st_id"));
                start.setComment(resultSet.getString("st_comment"));
                start.setStartDate(LocalDateTime.parse(resultSet.getString("st_startDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                if (resultSet.getString("st_endStart") != null) {
                    start.setEndDate(LocalDateTime.parse(resultSet.getString("st_endStart"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                }
                res.add(start);
            }
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
        return res;
    }

    public Start findById(Long id) {
        Start start = new Start();
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            String sqlString = "SELECT st_comment, st_startDate, st_endStart FROM start WHERE st_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            start.setId(id);
            start.setComment(resultSet.getString("st_comment"));
            start.setStartDate(LocalDateTime.parse(resultSet.getString("st_startDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            if (resultSet.getString("st_endStart") != null) {
                start.setEndDate(LocalDateTime.parse(resultSet.getString("st_endStart"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
        return start;
    }

    public Start getLastUnfinishedStart() {
        Datasource ds = new Datasource();
        Start start = new Start();
        try (Connection conn = ds.getDb()) {
            String sqlString = "select st_id, st_comment, st_startDate from start where st_endDate is null order by st_startDate asc";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            start.setId(resultSet.getLong("st_id"));
            start.setComment(resultSet.getString("st_comment"));
            start.setStartDate(LocalDateTime.parse(resultSet.getString("st_startDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
        if (start.getId() == null) {
            Start newStart = new Start("", LocalDateTime.now());
            this.persist(newStart);
            return newStart;
        }
        return start;
    }

    public void finishStarts() {
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            String sqlString = "update start set st_endDate = ? where st_endDate is null";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            preparedStatement.execute();
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
    }
}
