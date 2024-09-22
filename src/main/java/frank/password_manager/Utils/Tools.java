package frank.password_manager.Utils;

import frank.password_manager.DAO.PasswordDAO;
import frank.password_manager.Database.DatabaseConnection;
import frank.password_manager.Models.Password;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Tools {

    // Cambia entre paneles
    public static void changePanel(JPanel p, JPanel mainPanel) {
        p.setSize(1060, 670);
        p.setLocation(0, 0);

        // Remover todos los componentes y sugerir recolección de basura
        mainPanel.removeAll();
        System.gc();

        mainPanel.add(p);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static boolean primerAcceso() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public static Object getUserOrNot() {
        String sql = "SELECT username FROM users";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    // Llenar tabla de clientes
    public static void entablarContrasenias(JTable tabla, List<Password> passwords) throws Exception {
        actualizarTabla(tabla, passwords,
                pass -> new Object[]{pass.getId(), pass.getServiceName(), pass.getUsername(), pass.getPassword(), pass.getCategory()},
                "No hay contraseñas.");
    }

    private static <T> void actualizarTabla(JTable tabla, List<T> elementos, java.util.function.Function<T, Object[]> mapper, String mensaje) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        if (elementos.isEmpty()) {
            JOptionPane.showInternalMessageDialog(null, mensaje);
        } else {
            for (T elemento : elementos) {
                model.addRow(mapper.apply(elemento));
            }
        }
    }

    // Método para cargar las categorías en el ComboBox
    public static void loadCategoriesIntoComboBox(JComboBox combobox, PasswordDAO passwordDAO) {
        List<String> categories = passwordDAO.getAllCategories();
        if (!categories.isEmpty()) {
            for (String category : categories) {
                combobox.addItem(category);
            }
        }
    }

    public static <T> void loadIntoCombobox(JComboBox combobox, List<T> itemList) {
        if (!itemList.isEmpty()) {
            for (T item : itemList) {
                combobox.addItem(item);
            }
        }
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
