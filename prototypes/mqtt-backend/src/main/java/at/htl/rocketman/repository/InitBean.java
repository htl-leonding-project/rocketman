package at.htl.rocketman.repository;

import at.htl.rocketman.Datasource;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class InitBean {
    @Inject
    Logger LOG;

    void onStart(@Observes StartupEvent ev) throws IOException, SQLException {
        LOG.log(Logger.Level.INFO, "The application is starting...");
        Datasource ds = new Datasource();
        Connection c = ds.getDb();
        DatabaseMetaData dbm = c.getMetaData();
        // check if "data_set" table is there
        ResultSet tables = dbm.getTables(null, null, "data_set", null);
        if (tables.next()) {
            // Table exists
            LOG.info("table exists");
        }
        else {
            LOG.info("tables created");
            SqlRunner.createEmptyTables(c);
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOG.log(Logger.Level.INFO, "The application is stopping...");
    }
}
