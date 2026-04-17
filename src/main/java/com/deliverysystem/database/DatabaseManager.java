package com.deliverysystem.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseManager handles database connections for the application.
 * Uses singleton pattern for efficient connection management.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static Connection connection;
    
    private String url;
    private String username;
    private String password;
    private String driverClass;

    /**
     * Private constructor for singleton pattern
     */
    private DatabaseManager() {
        loadDatabaseConfig();
    }

    /**
     * Get singleton instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Load database configuration from properties file
     */
    private void loadDatabaseConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                // Use default values if properties file not found
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/delivery_system");
                props.setProperty("db.username", "root");
                props.setProperty("db.password", "");
                props.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            }
        } catch (IOException e) {
            System.err.println("Error loading database config, using defaults: " + e.getMessage());
        }

        url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/delivery_system");
        username = props.getProperty("db.username", "root");
        password = props.getProperty("db.password", "");
        driverClass = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }

    /**
     * Get database connection
     */
    public Connection getConnection() throws SQLException {
        try {
            // Check if connection is valid
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            
            // Load driver
            Class.forName(driverClass);
            
            // Create new connection
            connection = DriverManager.getConnection(url, username, password);
            return connection;
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Test database connection
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get database URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get database username
     */
    public String getUsername() {
        return username;
    }
}