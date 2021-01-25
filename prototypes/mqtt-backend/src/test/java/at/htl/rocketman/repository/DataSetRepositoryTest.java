package at.htl.rocketman.repository;

import at.htl.rocketman.Datasource;
import at.htl.rocketman.entity.DataSet;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.db.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataSetRepositoryTest {

    @Inject
    Logger LOG;

    @Inject
    DataSetRepository dataSetRepository;

    @BeforeEach
    void setUp() {
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            LOG.info("Connected.");
            String deleteSqlString = "DELETE FROM data_set";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSqlString);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("SQl-Error occurred: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unknown error occurred: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(100)
    @DisplayName("tests the basic functionality of thr persist method")
    void test_persist() throws IOException {
        DataSet dataSet = new DataSet("temperature", "1200", "celsius",  LocalDateTime.parse("2021-01-11T13:11:09.34"));
        Datasource ds = new Datasource();
        Table dataset = new Table(ds.getSqliteDb(), "data_set");

        dataSetRepository.persist(dataSet);

        assertThat(dataset).hasNumberOfRows(1);
        output(dataset).toConsole();
    }

    @Test
    @Order(200)
    @DisplayName("tests the basic functionality of the getAll mathod")
    void test_getAll() throws IOException {
        DataSet dataSet1 = new DataSet("temperature", "1200", "celsius",  LocalDateTime.parse("2021-01-11T13:11:09.34"));
        DataSet dataSet2 = new DataSet("temperature", "1300", "celsius",  LocalDateTime.parse("2021-01-11T13:11:10.34"));
        DataSet dataSet3 = new DataSet("temperature", "1400", "celsius",  LocalDateTime.parse("2021-01-11T13:11:11.34"));
        Datasource ds = new Datasource();
        Table dataset = new Table(ds.getSqliteDb(), "data_set");

        dataSetRepository.persist(dataSet1);
        dataSetRepository.persist(dataSet2);
        dataSetRepository.persist(dataSet3);

        List<DataSet> dataSets = dataSetRepository.getAll();

        assertThat(dataset).hasNumberOfRows(3);
        output(dataset).toConsole();

    }
}
