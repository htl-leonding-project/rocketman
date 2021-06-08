package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.CanSatConfiguration;
import at.htl.rocketman.repository.CanSatConfigurationRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/api/config")
public class CanSatConfigurationResource {

    @Inject
    CanSatConfigurationRepository repository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addConfig(CanSatConfiguration config) {
        if (config == null) {
            return Response.status(400).header("reason", "empty object").build();
        }
        repository.writeConfig(config);
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<CanSatConfiguration> readConfig() {
        return repository.readConfig();
    }
}
