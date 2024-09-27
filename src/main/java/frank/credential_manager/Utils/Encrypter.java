package frank.credential_manager.Utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encrypter {

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue;

    static {
        // Cargar las variables de entorno desde el archivo .env
        Dotenv dotenv = Dotenv.load();
        String secretKey = dotenv.get("SECRET_KEY");  // Obtener la clave secreta desde el archivo .env
        keyValue = secretKey.getBytes();  // Convertir la clave secreta en bytes
    }

    public static String encryptPassword(String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptPassword(String encryptedPassword) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
