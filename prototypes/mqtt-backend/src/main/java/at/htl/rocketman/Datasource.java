package at.htl.rocketman;

import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Datasource {
    SQLiteDataSource sqliteDb;

    public Datasource() {
        sqliteDb = new SQLiteDataSource();
        sqliteDb.setUrl("jdbc:sqlite:rocketman.db");
    }

    private static Connection c = null;

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
