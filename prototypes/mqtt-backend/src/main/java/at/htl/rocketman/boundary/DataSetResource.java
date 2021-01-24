package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.repository.DataSetRepository;
import org.assertj.db.internal.bytebuddy.asm.Advice;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/dataset")
public class DataSetResource {
    @Inject
    DataSetRepository dataSetRepository;

    @GET
    @Path("/descriptions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDescriptions() {
        List<String> list = dataSetRepository.getAllDescriptions();
        StringBuilder array = new StringBuilder("[");
        for (String description : list) {
            array.append("\"").append(description.toLowerCase()).append("\"").append(",");
        }
        array.append("]");
        if(list.size() != 0) {
            array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("/timestamps/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimeStamps(@PathParam("description") String description) {
        List<DataSet> list = dataSetRepository.findByDescription(description);
        StringBuilder array = new StringBuilder("[");
        for (DataSet dataSet : list) {
            array.append("\"").append(dataSet.getTimestamp()).append("\"").append(",");
        }
        array.append("]");
        if(list.size() != 0) {
            array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("/timesSinceStart/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimesSinceStart(@PathParam("description") String description) {
        List<DataSet> list = dataSetRepository.findByDescription(description);
        LocalDateTime start = list.get(0).getTimestamp();
        StringBuilder array = new StringBuilder("[");
        for (DataSet dataSet : list) {
            long timeSinceStart = ChronoUnit.SECONDS.between(start, dataSet.getTimestamp());
            array.append("\"").append(timeSinceStart).append("\"").append(",");
        }
        array.append("]");
        if(list.size() != 0) {
            array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("/values/{description}")
    @Consumes(MediaType.APPLICATION_JSON    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getValues(@PathParam("description") String description) {
        List<DataSet> list = dataSetRepository.findByDescription(description);
        StringBuilder array = new StringBuilder("[");
        for (DataSet dataSet : list) {
            array.append(dataSet.getValue()).append(",");
        }
        array.append("]");
        if(list.size() != 0) {
            array.delete(array.lastIndexOf(","), array.lastIndexOf(",") + 1);
        }
        return Response.ok(array.toString()).build();
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
