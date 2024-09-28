package frank.credential_manager.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_Initializer {

    // Método para inicializar y crear las tablas
    public static void initializeTables(Connection conn) throws SQLException {
        // Habilitar claves foráneas
        conn.createStatement().execute("PRAGMA foreign_keys = ON;");

        // SQL para crear la tabla de usuarios
        String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password_hash TEXT NOT NULL, "
                + "salt TEXT NOT NULL)";

        // SQL para crear la tabla de contraseñas
        String passwordsTable = "CREATE TABLE IF NOT EXISTS passwords ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "service_name TEXT NOT NULL, "
                + "username TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "category TEXT NOT NULL, "
                + "user_id INTEGER, "
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";

        try (Statement stmt = conn.createStatement()) {
            // Ejecutar las sentencias SQL para ambas tablas
            stmt.execute(usersTable);
            stmt.execute(passwordsTable);
        }
    }
}
