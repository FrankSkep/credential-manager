package frank.credential_manager.DAO;

import frank.credential_manager.Database.DB_Connection;
import frank.credential_manager.Models.User;
import frank.credential_manager.Utils.Hashing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class UserDAO {

    // Método para registrar un nuevo usuario comun
    public User registerUser(String username, String password) {
        String salt = Hashing.getSalt();
        String hashedPassword = Hashing.hashPassword(password, salt);
        String query = "INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)";
        try (Connection conn = DB_Connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, salt);

            // Ejecutar el INSERT
            int affectedRows = stmt.executeUpdate();

            // Verificar si se afectó alguna fila
            if (affectedRows > 0) {
                // Obtener las claves generadas
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long id = generatedKeys.getLong(1); // Obtener el ID generado
                        return new User(id, username, hashedPassword);
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Detalles : " + e.toString(), "Ocurrio un error", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    // Método para verificar las credenciales de usuario
    public User authenticateUser(String username, String password) {
        String query = "SELECT id, password_hash, salt FROM users WHERE username = ?";
        try (Connection conn = DB_Connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String storedPassword = resultSet.getString("password_hash");
                String storedSalt = resultSet.getString("salt"); // Recupera el salt de la base de datos
                String hashedPassword = Hashing.hashPassword(password, storedSalt);

                // Compara la contraseña hasheada con la almacenada en la base de datos
                if (storedPassword.equals(hashedPassword)) {
                    return new User(id, username, hashedPassword);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Detalles : " + e.toString(), "Ocurrio un error", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    public List<String> getAllUsernames() {
        String query = "SELECT username FROM users";

        List<String> usernames = new ArrayList<>();

        try (Connection conn = DB_Connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                usernames.add(username);
            }
            return usernames;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Detalles : " + e.toString(), "Ocurrio un error", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }
}
