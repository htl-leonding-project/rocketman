package org.acme.mqtt;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.acme.mqtt.entity.DataHelper;
import org.acme.mqtt.entity.DataSet;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Broadcast;


/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a MQTT topic (prices). The MQTT configuration is specified in the application configuration.
 */
@ApplicationScoped
public class RocketmanGenerator {

    private Random random = new Random();
    private DataHelper[] dh = fillDH();

    @Outgoing("my-data-stream")
    @Broadcast
    public Flowable<DataSet> generate() {

        return Flowable.interval(5, TimeUnit.SECONDS)
                .map(r -> {
                    int ran = random.nextInt(3);
                    DataSet ds = new DataSet(dh[ran].getDescription(),String.valueOf(random.nextInt(100)),dh[ran].getUnit(),LocalDateTime.now());
                    System.out.println("Sending DataSet: " + ds.toString());
                    return ds;
                });
    }

    private DataHelper[] fillDH(){
        DataHelper d1 = new DataHelper("Luftdruck","Bar");
        DataHelper d2 = new DataHelper("Höhe","m");
        DataHelper d3 = new DataHelper("Luftfeuchtigkeit","g/m3");
        DataHelper[] dh = {d1,d2,d3};
        return dh;
    }
}
