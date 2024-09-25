package frank.credential_manager.Utils;

import frank.credential_manager.Database.DB_Connection;
import frank.credential_manager.Models.Password;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class Tools {

    // Cambia entre paneles
    public static void changePanel(JPanel p, JPanel mainPanel) {
        p.setSize(mainPanel.getWidth(), mainPanel.getHeight());
        p.setLocation(0, 0);

        // Remover todos los componentes y sugerir recolección de basura
        mainPanel.removeAll();
        System.gc();

        mainPanel.add(p);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void setImageLabel(JLabel label, String root) {
        label.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    ImageIcon image = new ImageIcon(root);
                    Icon icon = new ImageIcon(image.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
                    label.setIcon(icon);
                    label.repaint();
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.toString());
                }
            }
        });
    }

    // Método para ajustar el ancho de las columnas de acuerdo al contenido
    public static void adjustColumnWidths(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int col = 0; col < table.getColumnCount(); col++) {
            int maxWidth = 50; // Ancho mínimo inicial
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                maxWidth = Math.max(comp.getPreferredSize().width + 10, maxWidth);
            }
            columnModel.getColumn(col).setPreferredWidth(maxWidth);
        }
    }

    // Verifica si el archivo de la base de datos existe
    public static boolean fileExists(String dbPath) {
        File dbFile = new File(dbPath);
        return dbFile.exists();
    }

    // Intenta obtener un usuario
    public static Object getUserOrNot() {
        String sql = "SELECT username FROM users";

        try (Connection connection = DB_Connection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "Ocurrio un error : " + e.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    public static void entablarContrasenias(JTable tabla, List<Password> passwords) throws Exception {
        actualizarTabla(tabla, passwords,
                pass -> new Object[]{pass.getId(), pass.getServiceName(), pass.getUsername(), pass.getPassword(), pass.getCategory()});
    }

    private static <T> void actualizarTabla(JTable tabla, List<T> elementos, java.util.function.Function<T, Object[]> mapper) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        if (!elementos.isEmpty()) {
            for (T elemento : elementos) {
                model.addRow(mapper.apply(elemento));
            }
        }
    }

    // Cargar items en combobox
    public static <T> void loadIntoCombobox(JComboBox combobox, List<T> itemList) {
        if (combobox.getItemCount() > 0) {
            combobox.removeAllItems();
        }

        if (!itemList.isEmpty()) {
            for (T item : itemList) {
                combobox.addItem(item);
            }
        }
    }

    // Valida que los campos esten llenos
    public static boolean validateFields(String[] fields) {
        for (String field : fields) {
            if (field.isBlank()) {
                return false;
            }
        }
        return true;
    }

    // Capitaliza una cadena
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // Obtener el nombre de un archivo
    public static String getFileName(String dbPath) {
        File dbFile = new File(dbPath);
        return dbFile.getName();
    }
}
