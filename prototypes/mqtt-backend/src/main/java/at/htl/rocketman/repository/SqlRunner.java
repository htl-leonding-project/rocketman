//package at.htl.rocketman.repository;
//
//import org.apache.ibatis.jdbc.ScriptRunner;
//import org.apache.ibatis.metadata.Database;
//
//import javax.sql.DataSource;
//import java.io.*;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.Properties;
//
//public class SqlRunner {
//
//    private static final String SCRIPT_PROPERTIES_PATH = "sql/script-files.properties";
//
//    public static void main(String[] args) {
//        dropAndCreateTablesWithExampleData();
//        //runScript(SqlScript.INSERT);
//        //runScript(SqlScript.INSERT);
//    }
//
//    public static void dropAndCreateTablesWithExampleData() {
//
//        try {
//            Properties scriptProperties = new Properties();
//            scriptProperties.load(new FileInputStream(SCRIPT_PROPERTIES_PATH));
//
//
//            DataSource dataSource = Database.getDataSource();
//            Connection conn = dataSource.getConnection();
//            System.out.println("Connection established......");
//            ScriptRunner sr = new ScriptRunner(conn);
//            sr.setLogWriter(null);
//
//            for (String file : scriptProperties.stringPropertyNames()) {
//                Reader reader = new BufferedReader(new FileReader(scriptProperties.getProperty(file)));
//                sr.runScript(reader);
//            }
//
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void dropTablesAndCreateEmptyTables() {
//
//        try {
//            Properties scriptProperties = new Properties();
//            scriptProperties.load(new FileInputStream(SCRIPT_PROPERTIES_PATH));
//
//
//            DataSource dataSource = Database.getDataSource();
//            Connection conn = dataSource.getConnection();
//            System.out.println("Connection established......");
//            ScriptRunner sr = new ScriptRunner(conn);
//            sr.setLogWriter(null);
//
//            String dropScript = scriptProperties.getProperty(SqlScript.DROP.name().toLowerCase());
//            sr.runScript(new BufferedReader(new FileReader(dropScript)));
//            String createScript = scriptProperties.getProperty(SqlScript.CREATE.name().toLowerCase());
//            sr.runScript(new BufferedReader(new FileReader(createScript)));
//
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void runScript(SqlScript sqlScript) {
//
//        try {
//            Properties scriptProperties = new Properties();
//            scriptProperties.load(new FileInputStream(SCRIPT_PROPERTIES_PATH));
//
//            DataSource dataSource = Database.getDataSource();
//            Connection conn = dataSource.getConnection();
//            System.out.println("Connection established for " + sqlScript.name() + "......");
//            ScriptRunner sr = new ScriptRunner(conn);
//            //sr.setLogWriter(null);
//
//            String script = scriptProperties.getProperty(sqlScript.name().toLowerCase());
//            sr.runScript(new BufferedReader(new FileReader(script)));
//
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//}