package org.acme.mqtt;

import javax.inject.Inject;
import javax.ws.rs.GET;
import org.acme.mqtt.entity.DataSet;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Publisher;

import io.smallrye.reactive.messaging.annotations.Channel;

/**
 * A simple resource retrieving the "in-memory" "my-data-stream" and sending the items to a server sent event.
 */
@Path("/rocketman")
public class RocketmanResource {

    @Inject
    @Channel("my-data-stream")
    Publisher<DataSet> dS;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<DataSet> stream() {
        return dS;
    }
}
