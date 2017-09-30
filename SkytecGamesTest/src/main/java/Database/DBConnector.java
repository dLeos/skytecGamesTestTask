package Database;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnector {

    private final String MYSQL_CONFIG_FILE = "src\\main\\resources\\MySQLServerConfig.txt";

    private Connection connection;
    private Map<String, String> configuration;

    public DBConnector() {
        try {
            readConfig();
            connect();

        } catch (IOException e) {
            System.out.println("Config data cannot be read");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Connection to the database is failed");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Closing connection is failed");
            e.printStackTrace();
        }
    }

    private void connect()
        throws SQLException {

        Driver driver = new FabricMySQLDriver();
        DriverManager.registerDriver(driver);

        connection = DriverManager.getConnection(
                configuration.get("URL"),
                configuration.get("User"),
                configuration.get("Password"));

    }

    private void readConfig()
        throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(MYSQL_CONFIG_FILE));
        configuration = new HashMap<>();

        for (String line : lines) {
            int separator = line.indexOf(':');
            configuration.put(
                    line.substring(0, separator),
                    line.substring(separator + 1).trim());
        }
    }
}
