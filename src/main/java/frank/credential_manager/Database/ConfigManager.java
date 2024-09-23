package frank.credential_manager.Database;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_FILE = Paths.get(System.getProperty("user.home"), "CredentialManagerApp", "config.properties").toString();

    public static String loadDatabasePath() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            return properties.getProperty("dbPath");
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo de configuración no encontrado, se creará uno nuevo.");
            saveDatabasePath(""); // Crea un archivo nuevo si no existe
        } catch (IOException ex) {
            System.out.println("Error cargando la configuración: " + ex.getMessage());
        }
        return null;
    }

    public static void saveDatabasePath(String dbPath) {
        Properties properties = new Properties();
        properties.setProperty("dbPath", dbPath);

        try {
            new File(CONFIG_FILE).getParentFile().mkdirs();
            try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
                properties.store(output, null);
            }
        } catch (IOException ex) {
            System.out.println("Error guardando la configuración: " + ex.getMessage());
        }
    }
//    private static final String CONFIG_FILE = "config.properties";
//
//    public static String loadDatabasePath() {
//        Properties properties = new Properties();
//        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
//            properties.load(input);
//            return properties.getProperty("dbPath");
//        } catch (IOException ex) {
//            System.out.println("Error cargando la configuración: " + ex.getMessage());
//        }
//        return null;
//    }
//
//    public static void saveDatabasePath(String dbPath) {
//        Properties properties = new Properties();
//        properties.setProperty("dbPath", dbPath);
//
//        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
//            properties.store(output, null);
//        } catch (IOException ex) {
//            System.out.println("Error guardando la configuración: " + ex.getMessage());
//        }
//    }
}
