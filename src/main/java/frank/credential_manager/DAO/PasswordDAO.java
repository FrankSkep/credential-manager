package frank.credential_manager.DAO;

import frank.credential_manager.Database.DB_Connection;
import frank.credential_manager.Models.Password;
import frank.credential_manager.Utils.Encrypter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.List;

public class PasswordDAO {

    // Instancia estatica privada
    private static PasswordDAO instance;

    // Constructor privado para evitar instanciación directa
    private PasswordDAO() {
    }

    // Método estático para obtener la única instancia
    public static synchronized PasswordDAO getInstance() {
        if (instance == null) {
            instance = new PasswordDAO();
        }
        return instance;
    }

    public boolean savePassword(Password password, Long userId) throws Exception {

        String query = "INSERT INTO passwords (service_name, username, password, category, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DB_Connection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            String encryptedPassword = Encrypter.encryptPassword(password.getPassword());

            stmt.setString(1, password.getServiceName());
            stmt.setString(2, password.getUsername());
            stmt.setString(3, encryptedPassword);
            stmt.setString(4, password.getCategory());
            stmt.setLong(5, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public boolean updatePassword(Password newPassword, Long userId) throws Exception {
        String query = "UPDATE passwords SET service_name = ?, username = ?, password = ?, category = ? WHERE id = ? AND user_id = ?";

        // Cifrar la nueva contraseña antes de actualizarla
        String encryptedPassword = Encrypter.encryptPassword(newPassword.getPassword());

        try (Connection connection = DB_Connection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            // Establecer los nuevos valores
            stmt.setString(1, newPassword.getServiceName());
            stmt.setString(2, newPassword.getUsername());
            stmt.setString(3, encryptedPassword);
            stmt.setString(4, newPassword.getCategory());
            stmt.setLong(5, newPassword.getId());
            stmt.setLong(6, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public boolean deletePassword(Long id, Long userId) {
        String query = "DELETE FROM passwords WHERE id = ? AND user_id = ?";

        try (Connection connection = DB_Connection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, id);
            stmt.setLong(2, userId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public List<Password> getAllPasswords(Long userId) throws Exception {
        String query = "SELECT id, service_name, username, password, category FROM passwords WHERE user_id = ?";

        List<Password> passwords = new ArrayList<>();

        try (Connection connection = DB_Connection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, userId);

            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();

            // Iterar sobre los resultados
            while (rs.next()) {
                Long id = rs.getLong("id");
                String service_name = rs.getString("service_name");
                String username = rs.getString("username");
                String encryptedPassword = rs.getString("password");
                String category = rs.getString("category");

                // Desencriptar la contraseña
                String decryptedPassword = Encrypter.decryptPassword(encryptedPassword);

                // Crear el objeto Password y agregarlo a la lista
                passwords.add(new Password(id, service_name, username, decryptedPassword, category));
            }
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }

        return passwords;
    }

    public List<String> getAllServices() {
        String query = "SELECT DISTINCT service_name FROM passwords";
        List<String> services = new ArrayList<>();

        try (Connection connection = DB_Connection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                services.add(rs.getString("service_name"));
            }

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }

        return services;
    }

    // Método para obtener todas las categorías
    public List<String> getAllCategories() {
        String query = "SELECT DISTINCT category FROM passwords";
        List<String> categories = new ArrayList<>();

        try (Connection connection = DB_Connection.getConnection(); PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }

        return categories;
    }
}
