package at.htl.rocketman;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import javax.inject.Inject;
import javax.json.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class MqttConsumer {

    @Inject
    Logger LOG;
    private static final String SCHEMA_FILENAME = "json_schema.json";
    private static final String JSON_ARRAY_NAME = "payload";
    private static final String temperatureSqlString = "INSERT INTO temperature VALUES (?, ?)";
    private static final String pressureSqlString = "INSERT INTO atmospheric_pressure VALUES (?, ?)";
    private final ObjectMapper mapper = new ObjectMapper();

    /*
        {
          "payload": [
            {
              "description": "temperature",
              "value": "1200",
              "unit": "celsius"
            }
           ]
        }
     */
    @Incoming("rocketman")
    public void consumeJson(byte[] raw) throws IOException {
        JsonSchema schema = getJsonSchemaFromStringContent(new String(Files.readAllBytes(Path.of(SCHEMA_FILENAME))));
        JsonObject object;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(raw);
            JsonReader reader = Json.createReader(byteArrayInputStream)) {
            object = reader.readObject();
        }
        JsonArray getArray = object.getJsonArray(JSON_ARRAY_NAME);
        System.out.println(getArray);
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
        }

        /*Datasource ds = new Datasource();
        Double temp = null;
        Float pressure = null;
        try {
            temp = Double.parseDouble(rawJson.get("temperature").toString());
            pressure =  Float.parseFloat(rawJson.get("atmospheric-pressure").toString());
        } catch (Exception e) {
            LOG.error("Failed to parse value: " + e.getMessage());
        }
        if(temp != null && pressure != null) {
            try {
                Connection conn = ds.getDb();
                LOG.info("Connected.");
                LOG.info("Temperature: " + temp + "; Pressure: " + pressure + "; " + LocalDateTime.now());
                PreparedStatement preparedStatement = conn.prepareStatement(pressureSqlString);
                preparedStatement.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                preparedStatement.setDouble(2, pressure);
                preparedStatement.executeUpdate();
                preparedStatement = conn.prepareStatement(temperatureSqlString);
                preparedStatement.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                preparedStatement.setDouble(2, temp);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("SQl-Error occurred: " + e.getMessage());
            } catch (Exception e) {
                LOG.error("Unknown error occurred: " + e.getMessage());
            }
        }*/
    }

    public JsonNode getJsonNodeFromStringContent(String content) throws IOException {
        return mapper.readTree(content);
    }

    protected JsonSchema getJsonSchemaFromStringContent(String schemaContent) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        return factory.getSchema(schemaContent);
    }
}
