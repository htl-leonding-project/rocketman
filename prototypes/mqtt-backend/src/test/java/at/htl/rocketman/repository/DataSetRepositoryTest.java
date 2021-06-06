package at.htl.rocketman.repository;

import at.htl.rocketman.Datasource;
import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.entity.Start;
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

    @Inject
    StartRepository startRepository;

    @BeforeEach
    void setUp() {
        Datasource ds = new Datasource();
        try (Connection conn = ds.getDb()) {
            LOG.info("Connected.");
            SqlRunner.dropTablesAndCreateEmptyTables();
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
    @DisplayName("tests the basic functionality of the persist method")
    void test_persist() throws IOException {
        Start start = new Start("Er war die erste Nummer 1!", LocalDateTime.parse("2021-01-11T13:11:09.34"));
        DataSet dataSet = new DataSet("temperature", "1200", "celsius",  LocalDateTime.parse("2021-01-11T13:11:09.34"), start);
        Datasource ds = new Datasource();
        Table dataset = new Table(ds.getSqliteDb(), "data_set");

        startRepository.persist(start);
        dataSetRepository.persist(dataSet);
        startRepository.finishStarts();

        assertThat(dataset).hasNumberOfRows(1);
        output(dataset).toConsole();
    }

    @Test
    @Order(200)
    @DisplayName("tests the basic functionality of the getAll method")
    void test_getAll() throws IOException {
        Start start = new Start("Er war die erste Nummer 2!", LocalDateTime.parse("2021-01-11T13:11:09.34"));
        DataSet dataSet1 = new DataSet("temperature", "1200", "celsius",  LocalDateTime.parse("2021-01-11T13:11:09.34"), start);
        DataSet dataSet2 = new DataSet("temperature", "1300", "celsius",  LocalDateTime.parse("2021-01-11T13:11:10.34"), start);
        DataSet dataSet3 = new DataSet("temperature", "1400", "celsius",  LocalDateTime.parse("2021-01-11T13:11:11.34"));
        Datasource ds = new Datasource();
        Table dataset = new Table(ds.getSqliteDb(), "data_set");

        startRepository.persist(start);
        dataSetRepository.persist(dataSet1);
        dataSetRepository.persist(dataSet2);
        dataSetRepository.persist(dataSet3);
        startRepository.finishStarts();

        List<DataSet> dataSets = dataSetRepository.getAll();

        assertThat(dataset).hasNumberOfRows(3);
        output(dataset).toConsole();

    }
}
