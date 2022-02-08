package org.acme.mqtt;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.smallrye.mutiny.Multi;
import org.acme.mqtt.entity.DataHelper;
import org.acme.mqtt.entity.DataSet;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Broadcast;


/**
 * A bean producing random values every 5 seconds.
 *
 */
@ApplicationScoped
public class RocketmanGenerator {


    @Inject
    Logger log;

    private Random random = new Random();
    private DataHelper[] dh = fillDH();
    @Outgoing("rocketman")
    @Broadcast
    public Multi<DataSet> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
                .onOverflow().drop()
                .map(r -> {
                    int ran = random.nextInt(3);
                    DataSet ds = new DataSet(
                            dh[ran].getDescription(),
                            String.valueOf(random.nextInt(100)*100),
                            dh[ran].getUnit(),
                            LocalDateTime.now()
                    );
                    log.info("Sending DataSet: " + ds.toString());
                    return  ds;
                });
    }

    private DataHelper[] fillDH(){
        DataHelper d1 = new DataHelper("Luftdruck","Bar");
        DataHelper d2 = new DataHelper("Hoehe","m");
        DataHelper d3 = new DataHelper("Luftfeuchtigkeit","g/m3");
        DataHelper[] dh = {d1,d2,d3};
        return dh;
    }
}
