package frank.credential_manager.Database;

import frank.credential_manager.Authentication.UserSession;
import frank.credential_manager.DAO.UserDAO;
import frank.credential_manager.Models.User;
import frank.credential_manager.Utils.Tools;
import frank.credential_manager.Views.DashboardPNL;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DB_Chooser {

    // Inicializa la base de datos local
    public static void initializeDbFile() {
        // Cargar la ruta de la base de datos desde configuración
        String dbPath = ConfigFileManager.loadDatabasePath();

        if (dbPath == null || dbPath.isEmpty() || !Tools.fileExists(dbPath)) {
            // Si no hay una base de datos guardada o si el archivo no existe, preguntar al usuario
            dbPath = promptUserForDatabase();
        }

        if (dbPath != null) {
            // Guardar la ruta seleccionada para futuros usos
            ConfigFileManager.saveDatabasePath(dbPath);

            // Inicializar la conexión con la base de datos
            DB_Connection.initializeDatabase(dbPath);
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó una base de datos. La aplicación se cerrará.");
            System.exit(0);
        }
    }

    // Método que pide al usuario que seleccione o cree una base de datos al arrancar el programa
    private static String promptUserForDatabase() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar o crear base de datos");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filtro para solo mostrar archivos .db
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de base de datos (.db)", "db");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (!selectedFile.getName().toLowerCase().endsWith(".db")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".db");
            }

            if (!selectedFile.exists()) {
                // Si el archivo no existe, preguntar si desea crearlo
                int option = JOptionPane.showConfirmDialog(null, "El archivo no existe. ¿Deseas crearlo?", "Crear Base de Datos", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        if (selectedFile.createNewFile()) {
                            JOptionPane.showMessageDialog(null, "Base de datos creada con éxito.");
                            return selectedFile.getAbsolutePath();
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error al crear la base de datos: " + e.getMessage());
                    }
                }
            } else {
                return selectedFile.getAbsolutePath();
            }
        }
        return null;
    }

    // Método para subir o crear una nueva base de datos a la app
    public static void changeDatabase() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar nueva base de datos");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de base de datos (.db)", "db");
        fileChooser.setFileFilter(filter);

        UserDAO userDAO = new UserDAO();

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (!selectedFile.getName().toLowerCase().endsWith(".db")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".db");
            }

            if (selectedFile.exists()) {
                // Cerrar la conexión actual
                DB_Connection.close();

                // Guardar la ruta de la nueva base de datos temporalmente
                String dbPath = selectedFile.getAbsolutePath();

                // Inicializar la nueva base de datos
                DB_Connection.initializeDatabase(dbPath);

                // Si el archivo ya existe, solicitar credenciales
                String[] credentials = showLoginDialog("Inicia sesion", userDAO.getAllUsernames());

                if (credentials != null) {
                    String username = credentials[0];
                    String password = credentials[1];

                    // Autenticación con la nueva base de datos
                    User user = userDAO.authenticateUser(username, password);

                    if (user != null) {

                        UserSession.getInstance().setUsuario(user); // Establecer sesion usuario activo

                        // Si la autenticación es exitosa, guardar la nueva ruta
                        ConfigFileManager.saveDatabasePath(dbPath);

                        // Recargar datos de la nueva base de datos
                        try {
                            DashboardPNL.getInstance(); // Actualiza el dashboard
                            JOptionPane.showMessageDialog(null, "Base de datos cambiada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error al inicializar el dashboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Si las credenciales son incorrectas, volver a cerrar la conexión
                        DB_Connection.close();

                        // Revertir a la base de datos anterior
                        DB_Connection.initializeDatabase(ConfigFileManager.loadDatabasePath());
                        try {
                            DashboardPNL.getInstance(); // Volver a cargar el dashboard
                        } catch (Exception e) {
                        }
                        JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Se mantiene en la base de datos actual.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // Si el archivo no existe, preguntar al usuario si desea crearlo
                int option = JOptionPane.showConfirmDialog(null, "El archivo no existe. ¿Deseas crearlo?", "Crear Base de Datos", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        if (selectedFile.createNewFile()) {
                            // Inicializar la nueva base de datos sin credenciales
                            String dbPath = selectedFile.getAbsolutePath();
                            DB_Connection.initializeDatabase(dbPath);
                            ConfigFileManager.saveDatabasePath(dbPath); // Guardar la ruta de la nueva base de datos

                            // Registrarse en la nueva base de datos
                            String[] credentials = showRegisterDialog("Registrate");
                            String username = credentials[0];
                            String password = credentials[1];

                            User newUser = userDAO.registerUser(username, password);
                            if (newUser != null) {
                                UserSession.getInstance().setUsuario(newUser);
                            }

                            // Actualizar el dashboard
                            DashboardPNL.getInstance();
                            JOptionPane.showMessageDialog(null, "Base de datos creada y cambiada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo crear la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error al crear la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    // Muestra ventana para autenticarse en la nueva base de datos
    public static String[] showLoginDialog(String msg, List<String> usuarios) {
        // Crear un ComboBox para mostrar los usuarios
        JComboBox<String> userComboBox = new JComboBox<>(usuarios.toArray(new String[0]));
        JPasswordField passwordField = new JPasswordField();

        Optional<String> resultado = usuarios.stream()
                .filter(elemento -> elemento.contains(UserSession.getInstance().getUsuario().getUsername()))
                .findFirst();  // Obtiene el primer elemento que coincida

        // Muestra seleccionada la cuenta que usa actualmente
        resultado.ifPresentOrElse(user -> userComboBox.setSelectedItem(resultado.get()),
                () -> userComboBox.setSelectedIndex(0));

        Object[] message = {
            "Usuario:", userComboBox,
            "Contraseña:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, msg, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return new String[]{(String) userComboBox.getSelectedItem(), new String(passwordField.getPassword())};
        }
        return null; // Si se cancela
    }

    public static String[] showRegisterDialog(String msg) {
        JTextField userField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
            "Usuario:", userField,
            "Contraseña:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, msg, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return new String[]{userField.getText(), new String(passwordField.getPassword())};
        }
        return null; // Si se cancela
    }
}
