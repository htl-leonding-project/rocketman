package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.entity.Start;
import at.htl.rocketman.repository.DataSetRepository;
import at.htl.rocketman.repository.StartRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/api/dataset")
@Tag(name = "DataSets", description = "Endpoints for the DataSets")
public class DataSetResource {
    private static final String DATASET_CSV_FILENAME = "values.csv";
    private static final String DATASET_CSV_HEADER = "start_id;description;value;unit;timestamp";

    private static final String START_CSV_FILENAME = "starts.csv";
    private static final String START_CSV_HEADER = "id;comment;startDate;endDate";

    @Inject
    DataSetRepository dataSetRepository;

    @Inject
    StartRepository startRepository;

    @Inject
    Logger LOG;

    @GET
    @Path("get-file")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Save data in CSV-files", description = "Saves all Starts and DataSets in their respective CSV-files and returns the datasets from the database as a JSON Object")
    public Response getFile() {
//        StringBuilder dataSets_SB = new StringBuilder();
//        StringBuilder starts_SB = new StringBuilder();
//        dataSets_SB.append(DATASET_CSV_HEADER)
//                .append("\n");
//        starts_SB.append(START_CSV_HEADER)
//                .append("\n");
//        List<DataSet> dataSets = dataSetRepository.getAll();
//        for (DataSet temp : dataSets) {
//            dataSets_SB.append(temp.toCSVString())
//                    .append("\n");
//        }
//        List<Start> starts = startRepository.getAll();
//        for (Start temp : starts) {
//            starts_SB.append(temp.toCSVString())
//                    .append("\n");
//        }
//        try {
//            FileWriter myWriter = new FileWriter(DATASET_CSV_FILENAME);
//            myWriter.write(dataSets_SB.toString());
//            myWriter.close();
//        }
//        catch (IOException e) {
//            LOG.error("Error while writing file " + DATASET_CSV_FILENAME + " : " + e.getMessage());
//        }
//        try {
//            FileWriter myWriter = new FileWriter(START_CSV_FILENAME);
//            myWriter.write(starts_SB.toString());
//            myWriter.close();
//        }
//        catch (IOException e) {
//            LOG.error("Error while writing file " + START_CSV_FILENAME + " : " + e.getMessage());
//        }
//        return Response.ok(dataSets_SB.toString()).build();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder startArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder dataSetArrayBuilder = Json.createArrayBuilder();
        for (Start start : startRepository.getAll()) {
            JsonObjectBuilder startBuilder = Json.createObjectBuilder();
            startBuilder.add("id", start.getId());
            startBuilder.add("comment", start.getComment());
            startBuilder.add("startDate", start.getStartDate().toString());
            startBuilder.add("endDate", start.getEndDate().toString());
            startArrayBuilder.add(startBuilder.build());
        }
        for (DataSet dataSet : dataSetRepository.getAll()) {
            JsonObjectBuilder dataSetBuilder = Json.createObjectBuilder();
            dataSetBuilder.add("start_id", dataSet.getStart().getId());
            dataSetBuilder.add("description", dataSet.getDescription());
            dataSetBuilder.add("value", dataSet.getValue());
            dataSetBuilder.add("unit", dataSet.getUnit());
            dataSetBuilder.add("timestamp", dataSet.getTimestamp().toString());
            dataSetArrayBuilder.add(dataSetBuilder.build());
        }
        builder.add("starts", startArrayBuilder.build());
        builder.add("dataSets", dataSetArrayBuilder.build());
        return Response.ok(builder.build()).build();
    }

    @GET
    @Path("descriptions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all descriptions", description = "returns all descriptions from the database as a JSON object")
    public Response getAllDescriptions() {
        List<String> list = dataSetRepository.getAllDescriptions();
        StringBuilder array = new StringBuilder("[");
        for (String description : list) {
            String pascalCase = description.substring(0, 1).toUpperCase() + description.substring(1).toLowerCase();
            array.append("\"")
                    .append(pascalCase)
                    .append("\"")
                    .append(",");
        }
        array.append("]");
        if(list.size() != 0) {
            array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
        }
        return Response
                .ok(array.toString())
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    @GET
    @Path("timestamps/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all timestamps for description", description = "returns all descriptions for the given description from the database as a JSON object")
    public Response getTimeStamps(@PathParam("description") String description) {
        List<DataSet> list = dataSetRepository.findByDescription(description);
        StringBuilder array = new StringBuilder("[");
        for (DataSet dataSet : list) {
            array.append("\"")
                    .append(dataSet.getTimestamp())
                    .append("\"")
                    .append(",");
        }
        array.append("]");
        if(list.size() != 0) {
            array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
        }
        return Response.ok(array.toString())
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    @GET
    @Path("timesSinceStart/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all times since start for description", description = "returns the difference between the time of start and the time of the timestamps " +
            "for all Datasets for the given description from the database as a JSON object")
    public Response getTimesSinceStart(@PathParam("description") String description) {
        List<DataSet> list = dataSetRepository.findByDescription(description);
        if (!list.isEmpty()) {
            LocalDateTime start = list.get(0).getTimestamp();
            StringBuilder array = new StringBuilder("[");
            for (DataSet dataSet : list) {
                long timeSinceStart = ChronoUnit.SECONDS.between(start, dataSet.getTimestamp());
                array.append("\"")
                        .append(timeSinceStart)
                        .append("\"")
                        .append(",");
            }
            array.append("]");
            if(list.size() != 0) {
                array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
            }
            return Response.ok(array.toString())
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Headers",
                            "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Methods",
                            "GET, POST, PUT, DELETE, OPTIONS, HEAD").build();
        }
        return Response.ok("[]").build();
    }

    @GET
    @Path("values/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all values for description", description = "returns all values for the given description from the database as a JSON object")
    public Response getValues(@PathParam("description") String description) {
        List<DataSet> list = dataSetRepository.findByDescription(description);
        StringBuilder array = new StringBuilder("[");
        for (DataSet dataSet : list) {
            array.append(dataSet.getValue())
                    .append(",");
        }
        array.append("]");
        if(list.size() != 0) {
            array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
        }
        return Response.ok(array.toString())
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    @GET
    @Path("unit/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all units for description", description = "returns all units for the given description from the database as a JSON object")
    public Response getUnit(@PathParam("description") String description) {
        String unit = dataSetRepository.getUnitForDescription(description);
        if(unit != null) {
            return Response.ok("\"" + unit + "\"")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Headers",
                            "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Methods",
                            "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .build();
        }
        return Response.noContent()
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

   /* @GET
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public String getLuftfeuchtigkeit() {
        List<DataSet> list = dataSetRepository.getAll();
        StringBuilder array = new StringBuilder("[");
        for (DataSet dataSet : list) {
            if(dataSet.getDescription().equals("Luftfeuchtigkeit"))
            array.append(",").append(dataSet);
        }
        array.append("]");
        return index.data("datasets_lf", array.toString()).render();
    }*/

    /*<ul>
    {#for dataset in datasets}
    <li>{dataset}</li>
    {/for}
    </ul>*/
}
