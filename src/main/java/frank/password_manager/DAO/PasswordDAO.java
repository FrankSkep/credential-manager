package frank.password_manager.DAO;

import frank.password_manager.Database.DatabaseConnection;
import frank.password_manager.Models.Password;
import frank.password_manager.Utils.Encrypter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.List;

public class PasswordDAO {

    public boolean savePassword(Password password) throws Exception {

        String query = "INSERT INTO passwords (service_name, username, password) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            String encryptedPassword = Encrypter.encryptPassword(password.getPassword());

            stmt.setString(1, password.getServiceName());
            stmt.setString(2, password.getUsername());
            stmt.setString(3, encryptedPassword);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public boolean updatePassword(Password newPassword, Long idPassword) throws Exception {
        String query = "UPDATE passwords SET service_name = ?, username = ?, password_hash = ? WHERE id = ?";

        // Cifrar la nueva contraseña antes de actualizarla
        String encryptedPassword = Encrypter.encryptPassword(newPassword.getPassword());

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            // Establecer los nuevos valores (nombre del servicio, usuario y contraseña)
            stmt.setString(1, newPassword.getServiceName());
            stmt.setString(2, newPassword.getUsername());
            stmt.setString(3, encryptedPassword);

            stmt.setLong(4, idPassword);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public boolean deletePassword(Long id) {
        String query = "DELETE FROM passwords WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public List<Password> getAllPasswords() throws Exception {
        String query = "SELECT id, service_name, username, password FROM passwords";

        List<Password> passwords = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();

            // Iterar sobre los resultados
            while (rs.next()) {
                Long id = rs.getLong("id");
                String service_name = rs.getString("service_name");
                String username = rs.getString("username");
                String encryptedPassword = rs.getString("password_hash");

                // Desencriptar la contraseña
                String decryptedPassword = Encrypter.decryptPassword(encryptedPassword);

                // Crear el objeto Password y agregarlo a la lista
                passwords.add(new Password(id, service_name, username, decryptedPassword));
            }
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }

        return passwords;
    }

    public List<Password> getPasswordsByService(String service_name) throws Exception {
        String query = "SELECT id, username, password FROM passwords WHERE service_name = ?";

        List<Password> passwords = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            // Establecer el parámetro del servicio
            stmt.setString(1, service_name);

            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();

            // Iterar sobre los resultados
            while (rs.next()) {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String encryptedPassword = rs.getString("password");

                // Desencriptar la contraseña
                String decryptedPassword = Encrypter.decryptPassword(encryptedPassword);

                // Crear el objeto Password y agregarlo a la lista
                passwords.add(new Password(id, service_name, username, decryptedPassword));
            }
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }

        return passwords;
    }

    // Método para obtener todas las categorías
    public List<String> getAllCategories() {
        String sql = "SELECT DISTINCT category FROM passwords";
        List<String> categories = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }

        return categories;
    }
}
