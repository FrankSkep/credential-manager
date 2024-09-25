package frank.credential_manager.Utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encrypter {

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "A1B2C3D4E5F60789".getBytes(); // Clave secreta

    // Método para cifrar
    public static String encryptPassword(String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Método para descifrar
    public static String decryptPassword(String encryptedPassword) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
