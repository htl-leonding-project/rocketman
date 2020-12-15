package at.htl.rocketman;


import javax.inject.Inject;
import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Datasource {
    SQLiteDataSource sqliteDb;

    public Datasource() {
        sqliteDb = new SQLiteDataSource();
    }

    private static Connection c = null;

    /**
     * @return
     * @throws SQLException
     */
    public Connection getDb() throws SQLException {
        if (c == null) {
            sqliteDb.setDatabaseName("rocketman.db");
            sqliteDb.setUrl("jdbc:sqlite:rocketman.db");
            c = sqliteDb.getConnection();
        }
        return c;
    }
}
