package at.htl.rocketman.boundary;

import at.htl.rocketman.entity.CanSatConfiguration;
import at.htl.rocketman.repository.CanSatConfigurationRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/api/config")
@Tag(name = "Configurations", description = "Endpoints for the Configurations")
public class CanSatConfigurationResource {

    @Inject
    CanSatConfigurationRepository repository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add a configuration", description = "saves an additional configuration in the CSV-file")
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
    @Operation(summary = "Read all configurations", description = "Returns all configurations from the CSV-file")
    public List<CanSatConfiguration> readConfig() {
        return repository.readConfig();
    }
}
