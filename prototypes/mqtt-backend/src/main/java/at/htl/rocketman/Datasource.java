package at.htl.rocketman;


import at.htl.rocketman.repository.SqlRunner;
import org.jboss.logging.Logger;
import org.sqlite.SQLiteDataSource;

import java.io.IOException;
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
    public Connection getDb() throws SQLException, IOException {
        if (c == null || c.isClosed()) {
            c = sqliteDb.getConnection();
        }
        return c;
    }

    public SQLiteDataSource getSqliteDb() {
        return sqliteDb;
    }
}
