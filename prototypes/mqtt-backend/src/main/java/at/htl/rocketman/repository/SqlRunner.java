package at.htl.rocketman.repository;
import at.htl.rocketman.Datasource;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlRunner {
    // "src/main/resources/sql/test-script-files.properties";
    // "classes/sql/script-files.properties"
    private static final String SCRIPT_PROPERTIES_PATH = find("test-script-files.properties").stream().findFirst().get().toString();
    private static final String TARGET_SCRIPT_PROPERTIES_PATH = find("script-files.properties").stream().findFirst().get().toString();

    protected static Collection<Path> find(String fileName) {
        try (Stream<Path> files = Files.walk(Paths.get("."))) {
            return files
                    .filter(f -> f.getFileName().toString().equals(fileName))
                    .collect(Collectors.toList());

        } catch (IOException e){
            return null;
        }
    }

    public static void main(String[] args) {
        //runScript(SqlScript.INSERT);
        //runScript(SqlScript.INSERT);
    }

    public static void dropAndCreateTablesWithExampleData() {

        try {
            Properties scriptProperties = new Properties();
            loadFile(scriptProperties);

            Datasource dataSource = new Datasource();
            Connection conn = dataSource.getDb();
            System.out.println("Connection established......");
            ScriptRunner sr = new ScriptRunner(conn);
            sr.setLogWriter(null);

            for (String file : scriptProperties.stringPropertyNames()) {
                Reader reader = new BufferedReader(new FileReader(scriptProperties.getProperty(file)));
                sr.runScript(reader);
            }
            conn.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void dropTablesAndCreateEmptyTables() {

        try {
            Properties scriptProperties = new Properties();
            loadFile(scriptProperties);

            Datasource dataSource = new Datasource();
            Connection conn = dataSource.getDb();
            System.out.println("Connection established......");
            ScriptRunner sr = new ScriptRunner(conn);
            sr.setLogWriter(null);

            String dropScript = scriptProperties.getProperty(SqlScript.DROP.name().toLowerCase());
            sr.runScript(new BufferedReader(new FileReader(dropScript)));
            String createScript = scriptProperties.getProperty(SqlScript.CREATE.name().toLowerCase());
            sr.runScript(new BufferedReader(new FileReader(createScript)));
            conn.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void createEmptyTables(Connection c) throws IOException, SQLException {
        Properties scriptProperties = new Properties();
        loadFile(scriptProperties);

        ScriptRunner sr = new ScriptRunner(c);
        sr.setLogWriter(null);

        String createScript = scriptProperties.getProperty(SqlScript.CREATE.name().toLowerCase());
        sr.runScript(new BufferedReader(new FileReader(createScript)));
        c.close();
    }

    private static void loadFile(Properties scriptProperties) throws IOException {
        try {
            scriptProperties.load(new FileInputStream(SCRIPT_PROPERTIES_PATH));
        } catch(FileNotFoundException e){
            scriptProperties.load(new FileInputStream(TARGET_SCRIPT_PROPERTIES_PATH));
        }
    }

    public static void runScript(SqlScript sqlScript) {

        try {
            Properties scriptProperties = new Properties();
            scriptProperties.load(new FileInputStream(SCRIPT_PROPERTIES_PATH));

            Datasource dataSource = new Datasource();
            Connection conn = dataSource.getDb();
            System.out.println("Connection established for " + sqlScript.name() + "......");
            ScriptRunner sr = new ScriptRunner(conn);
            //sr.setLogWriter(null);

            String script = scriptProperties.getProperty(sqlScript.name().toLowerCase());
            sr.runScript(new BufferedReader(new FileReader(script)));
            conn.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}