package frank.password_manager.DAO;

import frank.password_manager.Database.DatabaseConnection;
import frank.password_manager.Utils.Hashing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UserDAO {

    // Método para registrar un nuevo usuario comun
    public boolean registerUser(String username, String password) {
        String salt = Hashing.getSalt();
        String hashedPassword = Hashing.hashPassword(password, salt);
        String query = "INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, salt);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Detalles : " + e.toString(), "Ocurrio un error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    // Método para verificar las credenciales de usuario
    public boolean authenticateUser(String username, String password) {
        String query = "SELECT password_hash, salt FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password_hash");
                String storedSalt = resultSet.getString("salt"); // Recupera el salt de la base de datos
                String hashedPassword = Hashing.hashPassword(password, storedSalt);

                // Compara la contraseña hasheada con la almacenada en la base de datos
                return storedPassword.equals(hashedPassword);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Detalles : " + e.toString(), "Ocurrio un error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }
}
