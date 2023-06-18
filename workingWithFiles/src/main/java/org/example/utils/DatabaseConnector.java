package org.example.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import org.example.connector.exception.DBConnectionException;

public class DatabaseConnector {

    private static Connection connection;

    public DatabaseConnector() {
        try {
            String url = FileUtils.getProperty("url");
            String password = FileUtils.getProperty("password");
            String username = FileUtils.getProperty("username");
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new DBConnectionException("Failed getting connection: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
