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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@ApplicationScoped
public class MqttConsumer {

    @Inject
    Logger LOG;

    @Inject
    DataSetRepository dataSetRepository;

    private static String SCHEMA_FILENAME = "classes/json_schema.json";
    private final ObjectMapper mapper = new ObjectMapper();

    // Only for unittests
    public void setSchemaFilename(String schemaFilename) {
        SCHEMA_FILENAME = schemaFilename;
    }

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
        JsonSchema schema = getJsonSchemaFromStringContent(new String(Files.readAllBytes(Path.of(SCHEMA_FILENAME))));
        JsonNode node = getJsonNodeFromStringContent(message);
        Jsonb jsonb = JsonbBuilder.create();
        Set<ValidationMessage> errors = schema.validate(node);
        if(!errors.isEmpty()) {
            LOG.error("JSON Object has errors! -> Ignored");
            return;
        }
        DataSet ds = jsonb.fromJson(message, DataSet.class);
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
}
