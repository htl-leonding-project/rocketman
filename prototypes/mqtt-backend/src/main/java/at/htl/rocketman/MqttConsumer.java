package at.htl.rocketman;

import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.repository.DataSetRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collection;
import java.nio.file.*;

@ApplicationScoped
public class MqttConsumer {

    @Inject
    Logger LOG;

    @Inject
    DataSetRepository dataSetRepository;

    private static final String SCHEMA_FILENAME = "json_schema.json";
    private final ObjectMapper mapper = new ObjectMapper();
//test
    /*
        {
          "description": "temperature",
          "value": "1200",
          "unit": "celsius",
          "timestamp": "2021-01-11T13:11:09.34"
        }
     */
    @Incoming("rocketman")
    public void consumeJson(byte[] raw) throws IOException {
        String message = new String(raw);
        Path schemaFilePath = find(SCHEMA_FILENAME).stream().findFirst().get();
        JsonSchema schema = getJsonSchemaFromStringContent(Files.readString(schemaFilePath));
        JsonNode node = getJsonNodeFromStringContent(message);
        Jsonb jsonb = JsonbBuilder.create();
        Set<ValidationMessage> errors = schema.validate(node);
        if(!errors.isEmpty()) {
            LOG.error("JSON Object has errors! -> Ignored");
            return;
        }
        DataSet ds;
        try {
            ds = jsonb.fromJson(message, DataSet.class);
        } catch (JsonbException e) {
            LOG.error("JSON Object can't be deserialized -> Ignored");
            return;
        }
        if (ds.getDescription().equals("")) {
            LOG.error("JSON Object has no description -> Ignored");
            return;
        }
        LOG.info("JSON Object has no errors! " + ds.getDescription() + ": " + ds.getValue());
        try {
            double valueDouble = Double.parseDouble(ds.getValue());
            valueDouble /= 100.0;
            ds.setValue(Double.toString(valueDouble));
        } catch (NumberFormatException ignored) { }
        dataSetRepository.persist(ds);
    }

    public JsonNode getJsonNodeFromStringContent(String content) throws IOException {
        return mapper.readTree(content);
    }

    protected JsonSchema getJsonSchemaFromStringContent(String schemaContent) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        return factory.getSchema(schemaContent);
    }

    protected static Collection<Path> find(String fileName) throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get("."))) {
            return files
                    .filter(f -> f.getFileName().toString().equals(fileName))
                    .collect(Collectors.toList());

        }
    }
}
