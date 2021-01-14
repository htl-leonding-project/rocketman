package at.htl.rocketman;


import at.htl.rocketman.repository.SqlRunner;
import org.jboss.logging.Logger;
import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Datasource {
    SQLiteDataSource sqliteDb;
    private static final Logger LOG = Logger.getLogger(MqttConsumer.class);

    public Datasource() {
        sqliteDb = new SQLiteDataSource();
        sqliteDb.setUrl("jdbc:sqlite:rocketman.db");
    }

    private static Connection c = null;

    /**
     * @return
     * @throws SQLException
     */
    public Connection getDb() throws SQLException {
        if (c == null) {
            c = sqliteDb.getConnection();
            DatabaseMetaData dbm = c.getMetaData();
            // check if "data_set" table is there
            ResultSet tables = dbm.getTables(null, null, "data_set", null);
            if (tables.next()) {
                // Table exists
                LOG.info("table exists");
            }
            else {
                LOG.info("tables created");
                SqlRunner.createEmptyTables();
            }
        }
        return c;
    }
}
