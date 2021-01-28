package at.htl.rocketman.repository;
import at.htl.rocketman.Datasource;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class SqlRunner {

    private static final String SCRIPT_PROPERTIES_PATH = "src/main/resources/sql/test-script-files.properties";
    private static final String TARGET_SCRIPT_PROPERTIES_PATH = "classes/sql/script-files.properties";



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

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void createEmptyTables(Connection c) throws IOException {
        Properties scriptProperties = new Properties();
        loadFile(scriptProperties);

        ScriptRunner sr = new ScriptRunner(c);
        sr.setLogWriter(null);

        String createScript = scriptProperties.getProperty(SqlScript.CREATE.name().toLowerCase());
        sr.runScript(new BufferedReader(new FileReader(createScript)));
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

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}