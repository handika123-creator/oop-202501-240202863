package com.upb.agripos.config;
import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {
    private static Connection connection;
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/agripos","postgres","1234");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return connection;
    }
}