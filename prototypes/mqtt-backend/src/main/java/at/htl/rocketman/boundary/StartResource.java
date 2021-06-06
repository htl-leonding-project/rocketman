package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.CanSatConfiguration;
import at.htl.rocketman.entity.DataSet;
import at.htl.rocketman.repository.StartRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

@Path("/api/start")
public class StartResource {
    @Inject
    StartRepository repository;

    @POST
    @Path("addStart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStart(Start config) {
        if (config == null) {
            return Response.status(400).header("reason", "empty object").build();
        }
        repository.persist(config);
        return Response.ok().build();
    }


    @PUT
    @Path("finish")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimeStamps() {
        repository.finishStarts();
        return Response.ok().build();
    }
}
