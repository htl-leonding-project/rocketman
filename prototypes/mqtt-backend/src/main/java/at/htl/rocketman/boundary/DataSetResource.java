package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.repository.DataSetRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/api/dataset")
public class DataSetResource {
    private static final String CSV_FILENAME = "values.csv";
    private static final String CSV_HEADER = "start_id;description;value;unit;timestamp";

    @Inject
    DataSetRepository repository;

    @Inject
    Logger LOG;

    @GET
    @Path("descriptions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDescriptions() {
        List<String> list = repository.getAllDescriptions();
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
                .header("Access-Control-Allow-Origin", "*")
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
    public Response getTimeStamps(@PathParam("description") String description) {
        List<DataSet> list = repository.findByDescription(description);
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
        return Response.ok(array.toString()).header("Access-Control-Allow-Origin", "*")
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
    public Response getTimesSinceStart(@PathParam("description") String description) {
        List<DataSet> list = repository.findByDescription(description);
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
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD").build();
    }

    @GET
    @Path("values/{description}")
    @Consumes(MediaType.APPLICATION_JSON    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getValues(@PathParam("description") String description) {
        List<DataSet> list = repository.findByDescription(description);
        StringBuilder array = new StringBuilder("[");
        for (DataSet dataSet : list) {
            array.append(dataSet.getValue())
                    .append(",");
        }
        array.append("]");
        if(list.size() != 0) {
            array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
        }
        return Response.ok(array.toString()).header("Access-Control-Allow-Origin", "*")
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
    public Response getUnit(@PathParam("description") String description) {
        String unit = repository.getUnitForDescription(description);
        if(unit != null) {
            return Response.ok("\"" + unit + "\"").header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Headers",
                            "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Methods",
                            "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .build();
        }
        return Response.noContent().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    @GET
    @Path("get-file")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFile() {
        StringBuilder result = new StringBuilder();
        result.append(CSV_HEADER)
                .append("\n");
        List<DataSet> all = repository.getAll();
        for (DataSet temp : all) {
            result.append(temp.toCSVString())
                    .append("\n");
        }
        try {
            FileWriter myWriter = new FileWriter(CSV_FILENAME);
            myWriter.write(result.toString());
            myWriter.close();
        }
        catch (IOException e) {
            LOG.error("Error while writing file " + CSV_FILENAME + " : " + e.getMessage());
        }
        return Response.ok(result.toString()).build();
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
