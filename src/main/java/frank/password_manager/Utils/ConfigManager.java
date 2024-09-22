package frank.password_manager.Utils;

import java.io.*;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";

    public static String loadDatabasePath() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            return properties.getProperty("dbPath");
        } catch (IOException ex) {
            System.out.println("Error cargando la configuración: " + ex.getMessage());
        }
        return null;
    }

    public static void saveDatabasePath(String dbPath) {
        Properties properties = new Properties();
        properties.setProperty("dbPath", dbPath);

        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, null);
        } catch (IOException ex) {
            System.out.println("Error guardando la configuración: " + ex.getMessage());
        }
    }
}
