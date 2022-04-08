package at.htl.rocketman;

import at.htl.rocketman.repository.SqlRunner;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import static org.assertj.db.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MqttConsumerTest {

    @Inject
    Logger LOG;

    @Inject
    MqttConsumer mqttConsumer;

    String workingTemperatureJson = "{\n" +
            "              \"description\": \"temperature\",\n" +
            "              \"value\": \"1200\",\n" +
            "              \"unit\": \"celsius\",\n" +
            "              \"timestamp\": \"2021-01-19T17:13:41.777301100\"\n" +
            "            }";

    String workingHeightJson = "\n" +
            "            {\n" +
            "              \"description\": \"hoehe\",\n" +
            "              \"value\": \"4000\",\n" +
            "              \"unit\": \"meter\",\n" +
            "              \"timestamp\": \"2022-01-19T17:13:41.777301100\"\n" +
            "            }";

    String workingAirPressureJson = "\n" +
            "            {\n" +
            "              \"description\": \"luftdruck\",\n" +
            "              \"value\": \"100000\",\n" +
            "              \"unit\": \"bar\",\n" +
            "              \"timestamp\": \"2022-02-19T17:13:41.777301100\"\n" +
            "            }";

    String noDescriptionJson = "\n" +
            "            {\n" +
            "              \"description\": \"\",\n" +
            "              \"value\": \"1200\",\n" +
            "              \"unit\": \"celsius\",\n" +
            "              \"timestamp\": \"2021-01-19T17:13:41.777301100\"\n" +
            "            }";

    String faultyTimestampJson = "\n" +
            "            {\n" +
            "              \"description\": \"\",\n" +
            "              \"value\": \"1200\",\n" +
            "              \"unit\": \"celsius\",\n" +
            "              \"timestamp\": \"01-19T17:13:41.777301100\"\n" +
            "            }";

    String valueNoStringJson = "\n" +
            "            {\n" +
            "              \"description\": \"temperature\",\n" +
            "              \"value\": 1200,\n" +
            "              \"unit\": \"celsius\",\n" +
            "              \"timestamp\": \"2021-01-19T17:13:41.777301100\"\n" +
            "            }";

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
    @DisplayName("Consume - Should save one object")
    void test_consumeSaveOneObject() throws IOException {
        mqttConsumer.consumeJson(workingTemperatureJson.getBytes(StandardCharsets.UTF_8));

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(1);
    }

    @Test
    @Order(200)
    @DisplayName("Consume - Temperature is not a string - nothing saved")
    void test_valueIsNotAString() throws IOException {
        mqttConsumer.consumeJson(valueNoStringJson.getBytes(StandardCharsets.UTF_8));

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(0);
    }

    @Test
    @Order(300)
    @DisplayName("Consume - No description provided - nothing saved")
    void test_consumeNoDescription() throws IOException {
        mqttConsumer.consumeJson(noDescriptionJson.getBytes(StandardCharsets.UTF_8));

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(0);
    }

    @Test
    @Order(350)
    @DisplayName("Consume - No timestamp- nothing saved")
    void test_consumeWrongTimestamp() throws IOException {
        mqttConsumer.consumeJson(faultyTimestampJson.getBytes(StandardCharsets.UTF_8));

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(0);
    }

    @Test
    @Order(400)
    @DisplayName("Consume - Save three object")
    void test_consumeSaveThreeObjects() throws IOException {
        mqttConsumer.consumeJson(workingTemperatureJson.getBytes(StandardCharsets.UTF_8));
        mqttConsumer.consumeJson(workingHeightJson.getBytes(StandardCharsets.UTF_8));
        mqttConsumer.consumeJson(workingAirPressureJson.getBytes(StandardCharsets.UTF_8));

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(3);
    }

    @Test
    @Order(500)
    @DisplayName("Consume - Save three objects after faulty json")
    void test_consumeThreeAfterFaultyJson() throws IOException {
        mqttConsumer.consumeJson(noDescriptionJson.getBytes(StandardCharsets.UTF_8));
        mqttConsumer.consumeJson(workingTemperatureJson.getBytes(StandardCharsets.UTF_8));
        mqttConsumer.consumeJson(workingHeightJson.getBytes(StandardCharsets.UTF_8));
        mqttConsumer.consumeJson(valueNoStringJson.getBytes(StandardCharsets.UTF_8));
        mqttConsumer.consumeJson(workingAirPressureJson.getBytes(StandardCharsets.UTF_8));
        mqttConsumer.consumeJson(valueNoStringJson.getBytes(StandardCharsets.UTF_8));

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(3);
    }
}