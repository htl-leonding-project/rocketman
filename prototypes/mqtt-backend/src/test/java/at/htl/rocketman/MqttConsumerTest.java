package at.htl.rocketman;

import at.htl.rocketman.repository.SqlRunner;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.db.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MqttConsumerTest {
    @Inject
    Logger LOG;

    @Inject
    MqttConsumer mqttConsumer;

    protected static Collection<Path> find(String fileName, String searchDirectory) throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get(searchDirectory))) {
            return files
                    .filter(f -> f.getFileName().toString().equals(fileName))
                    .collect(Collectors.toList());

        }
    }

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
    @DisplayName("Test consumeJson with a working json with only one example object")
    void test_consumeWithGoodJson() throws IOException {
        Path goodJson = find("good.json", ".").stream().findFirst().get();
        Path jsonSchema = find("json_schema.json", ".").stream().findFirst().get();

        byte[] data = Files.readAllBytes(goodJson);
        mqttConsumer.setSchemaFilename(String.valueOf(jsonSchema));
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
        Path badjson = find("bad.json", ".").stream().findFirst().get();
        Path jsonSchema = find("json_schema.json", ".").stream().findFirst().get();

        byte[] data = Files.readAllBytes(badjson);
        mqttConsumer.setSchemaFilename(String.valueOf(jsonSchema));
        mqttConsumer.consumeJson(data);

        Datasource ds = new Datasource();

        Table dataset = new Table(ds.getSqliteDb(), "data_set");
        output(dataset).toConsole();
        assertThat(dataset).hasNumberOfRows(0);
    }
}