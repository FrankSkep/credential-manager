package frank.password_manager.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    // Método para inicializar y crear las tablas
    public static void initializeTables(Connection conn) throws SQLException {
        // SQL para crear la tabla usuarios
        String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password_hash TEXT NOT NULL, "
                + "salt TEXT NOT NULL)";

        // SQL para crear la tabla contraseñas
        String passwordsTable = "CREATE TABLE IF NOT EXISTS passwords ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "service_name TEXT NOT NULL, "
                + "username TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "category TEXT NOT NUL UNIQUE)";

        try (Statement stmt = conn.createStatement()) {
            // Ejecutar las sentencias SQL para ambas tablas
            stmt.execute(usersTable);
            stmt.execute(passwordsTable);
        }
    }
}
