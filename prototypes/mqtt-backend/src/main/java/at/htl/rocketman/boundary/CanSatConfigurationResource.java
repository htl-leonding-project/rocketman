package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.CanSatConfiguration;
import at.htl.rocketman.repository.CanSatConfigurationRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api/config")
public class CanSatConfigurationResource {

    @Inject
    CanSatConfigurationRepository repository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeConfig(CanSatConfiguration config) {
        if (config == null) {
            return Response.noContent().build();
        }
        repository.writeConfig(config);
        return Response.accepted().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CanSatConfiguration readConfig() {
        return repository.readConfig();
    }
}
