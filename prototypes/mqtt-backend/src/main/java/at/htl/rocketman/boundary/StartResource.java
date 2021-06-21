package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.Start;
import at.htl.rocketman.repository.StartRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/start")
@Tag(name = "Start", description = "Endpoints for the Starts")
public class StartResource {
    @Inject
    StartRepository repository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Initiates a rocket launch", description = "Adds a Start to the database")
    public Response addStart(Start config) {
        if (config == null) {
            return Response.status(400).header("reason", "empty object").build();
        }
        repository.persist(config);
        return Response.ok().build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Ends a launch", description = "Sets the end date of the current Start to the current date/time")
    public Response finishStart() {
        repository.finishStarts();
        return Response.ok().build();
    }
}
