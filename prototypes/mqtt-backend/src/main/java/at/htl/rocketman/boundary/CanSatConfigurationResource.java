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
    @Path("addConf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addConfig(CanSatConfiguration config) {
        if (config == null) {
            return Response.noContent().build();
        }
        repository.writeConfig(config);
        return Response.accepted().build();
    }

    @GET
    @Path("getConf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<CanSatConfiguration> readConfig() {
        return repository.readConfig();
    }
}
