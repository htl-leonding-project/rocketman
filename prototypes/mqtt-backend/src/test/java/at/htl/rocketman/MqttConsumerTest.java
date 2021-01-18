package at.htl.rocketman;

import at.htl.rocketman.entity.DataSet;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.db.api.Assertions.assertThat;
import static org.assertj.db.api.Assertions.bytesContentFromClassPathOf;
import static org.assertj.db.output.Outputs.output;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MqttConsumerTest {
    @Inject
    Logger LOG;

    @Inject
    MqttConsumer mqttConsumer;

    @BeforeEach
    void setUp() {
        // should be replaced with Sql runner drop and create
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
    @DisplayName("Test consumeJson with a working json with only one example object")
    void test_consumeWithGoodJson() throws IOException {
        byte[] data = Files.readAllBytes(Path.of("src/test/resources/good.json"));
        mqttConsumer.setSchemaFilename("src/main/resources/json_schema.json");
        mqttConsumer.consumeJson(data);

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(1);
    }

    @Test
    @Order(200)
    @DisplayName("Test consumeJson with a faulty json")
    void test_consumeWithBadJson() throws IOException {
        byte[] data = Files.readAllBytes(Path.of("src/test/resources/bad.json"));
        mqttConsumer.setSchemaFilename("src/main/resources/json_schema.json");
        mqttConsumer.consumeJson(data);

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(0);
    }

    @Test
    @Order(300)
    @DisplayName("Test consumeJson with a working json with multiple example objects")
    void test_consumeWithBigJson() throws IOException {
        byte[] data = Files.readAllBytes(Path.of("src/test/resources/big.json"));
        mqttConsumer.setSchemaFilename("src/main/resources/json_schema.json");
        mqttConsumer.consumeJson(data);

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(3);
    }

    @Test
    @Order(400)
    @DisplayName("Test consumeJson with a two json files")
    void test_consumeWithTwoJson() throws IOException {
        byte[] data = Files.readAllBytes(Path.of("src/test/resources/big.json"));
        mqttConsumer.setSchemaFilename("src/main/resources/json_schema.json");
        mqttConsumer.consumeJson(data);

        data = Files.readAllBytes(Path.of("src/test/resources/good.json"));
        mqttConsumer.consumeJson(data);

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(4);
    }
}