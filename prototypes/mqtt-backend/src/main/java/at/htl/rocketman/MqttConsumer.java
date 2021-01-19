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
import javax.json.*;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Set;

@ApplicationScoped
public class MqttConsumer {

    @Inject
    Logger LOG;

    @Inject
    DataSetRepository dataSetRepository;

    private static String SCHEMA_FILENAME = "classes/json_schema.json";
    private static final String JSON_ARRAY_NAME = "payload";
    private final ObjectMapper mapper = new ObjectMapper();

    // Only for unittests
    public void setSchemaFilename(String schemaFilename) {
        SCHEMA_FILENAME = schemaFilename;
    }

    /*
                {
                  "payload": [
                    {
                      "description": "temperature",
                      "value": "1200",
                      "unit": "celsius",
                      "timestamp": " 2021-01-11T13:11:09.34"
                    }
                   ]
                }
     */
    @Incoming("rocketman")
    public void consumeJson(byte[] raw) throws IOException {
        String message = new String(raw);
        JsonSchema schema = getJsonSchemaFromStringContent(new String(Files.readAllBytes(Path.of(SCHEMA_FILENAME))));
        JsonNode node = getJsonNodeFromStringContent(message);
        System.out.println(message);
        Jsonb jsonb = JsonbBuilder.create();
        Set<ValidationMessage> errors = schema.validate(node);
        if(!errors.isEmpty()) {
            LOG.error("JSON Object has errors! -> Ignored");
            return;
        }
        DataSet ds = jsonb.fromJson(message, DataSet.class);
        try {
            double valueDouble = Double.parseDouble(ds.getValue());
            valueDouble /= 100.0;
            ds.setValue(Double.toString(valueDouble));
        } catch (NumberFormatException e) { }
        dataSetRepository.persist(ds);

        /*JsonObject object;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(raw);
            JsonReader reader = Json.createReader(byteArrayInputStream)) {
            object = reader.readObject();
        }
        JsonArray getArray = object.getJsonArray(JSON_ARRAY_NAME);
        for(int i = 0; i < getArray.size(); i++)
        {
            JsonObject objects = getArray.getJsonObject(i);
            System.out.println(objects);
            JsonNode node = getJsonNodeFromStringContent(objects.toString());
            Set<ValidationMessage> errors = schema.validate(node);
            if(!errors.isEmpty()) {
                LOG.error("JSON Object has errors! -> Ignored");
                return;
            }
            LOG.info("no error found");

            String description = removeQuotes(objects.get("description").toString());
            String value = removeQuotes(objects.get("value").toString());
            String unit = removeQuotes(objects.get("unit").toString());
            try {
                double valueDouble = Double.parseDouble(value);
                valueDouble /= 100.0;
                value = String.valueOf(valueDouble);
            } catch (NumberFormatException e) { }
            dataSetRepository.persist(new DataSet(description, value, unit, LocalDateTime.now()));
        }
        for (DataSet x : dataSetRepository.getAll()) {
            System.out.println(x.getValue());
        }*/
    }

    public JsonNode getJsonNodeFromStringContent(String content) throws IOException {
        return mapper.readTree(content);
    }

    protected JsonSchema getJsonSchemaFromStringContent(String schemaContent) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        return factory.getSchema(schemaContent);
    }

    public String removeQuotes(String text) {
        return text.substring(1, text.length() - 1);
    }
}
