package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.repository.DataSetRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/api/dataset")
public class DataSetResource {
    @Inject
    DataSetRepository repository;

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
        return Response.ok(array.toString()).build();
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
        return Response.ok(array.toString()).build();
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
        return Response.ok(array.toString()).build();
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
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("unit/{description}")
    @Consumes(MediaType.APPLICATION_JSON    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnit(@PathParam("description") String description) {
        String unit = repository.getUnitForDescription(description);
        if(unit != null) {
            return Response.ok("\"" + unit + "\"").build();
        }
        return Response.noContent().build();
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
