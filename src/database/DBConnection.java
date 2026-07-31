package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 *
 * Handles the JDBC connection to the MySQL database.
 * Centralizing the connection logic here means the rest of the
 * application never has to worry about connection URLs / credentials.
 *
 * IMPORTANT: Update DB_URL, DB_USER, and DB_PASSWORD below to match
 * your local MySQL Workbench setup before running the application.
 */
public class DBConnection {

    // ---- Update these values to match your MySQL setup ----
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grade_tracker";
    private static final String DB_USER = "vanita";
    private static final String DB_PASSWORD = "vinnie234";
    // ---------------------------------------------------------

    private static Connection connection = null;

    // Private constructor to prevent instantiation (utility class pattern)
    private DBConnection() {
    }

    /**
     * Returns a live connection to the database.
     * Creates a new connection if one does not already exist, or if the
     * existing one has been closed.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Explicitly loading the driver is not required in modern JDBC,
                // but is kept here for clarity and backward compatibility.
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. "
                        + "Make sure mysql-connector-j-x.x.x.jar is on the classpath.", e);
            }
        }
        return connection;
    }

    /**
     * Closes the active connection, if any. Should be called when the
     * application shuts down.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error while closing the database connection: " + e.getMessage());
            }
        }
    }
}
