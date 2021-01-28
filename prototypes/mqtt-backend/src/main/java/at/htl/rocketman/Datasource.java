package at.htl.rocketman;


import org.jboss.logging.Logger;
import org.sqlite.SQLiteDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Datasource {
    SQLiteDataSource sqliteDb;

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
        if (c == null || c.isClosed()) {
            c = sqliteDb.getConnection();
        }
        return c;
    }

    public SQLiteDataSource getSqliteDb() {
        return sqliteDb;
    }
}
