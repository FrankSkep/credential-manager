package frank.password_manager.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    public static void initializeDatabase() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:src/main/resources/passManager.db");
        config.setMaximumPoolSize(3);
        config.setPoolName("HikariSQLitePool");

        dataSource = new HikariDataSource(config);

        // Crear las tablas después de inicializar la conexión
        try (Connection conn = dataSource.getConnection()) {
            DatabaseInitializer.initializeTables(conn);
        } catch (SQLException e) {
            System.out.println("Error inicializando las tablas: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDatabase();
        }
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
