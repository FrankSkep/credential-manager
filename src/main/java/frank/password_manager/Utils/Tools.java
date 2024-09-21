package frank.password_manager.Utils;

import frank.password_manager.Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Tools {

    // Cambia entre paneles
    public static void changePanel(JPanel p, JPanel mainPanel) {
        p.setSize(1060, 670);
        p.setLocation(0, 0);

        // Remover todos los componentes y sugerir recolección de basura
        mainPanel.removeAll();
        System.gc();

//        mainPanel.add(p, BorderLayout.CENTER);
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
}
