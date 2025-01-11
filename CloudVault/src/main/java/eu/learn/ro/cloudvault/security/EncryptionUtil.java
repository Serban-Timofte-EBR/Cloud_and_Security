package eu.learn.ro.cloudvault.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_KEY = "MI]Qf!om{3nZ-1!)R[rwc4zse0%MX`wz"; // Ensure this is 16, 24, or 32 bytes

    public static String encrypt(String data) {
        try {
            // Generate AES key from the static string
            byte[] keyBytes = AES_KEY.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 32, AES_ALGORITHM);

            // Initialize the cipher in encrypt mode
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Encrypt the data and encode as Base64
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            // Generate AES key from the static string
            byte[] keyBytes = AES_KEY.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 32, AES_ALGORITHM);

            // Initialize the cipher in decrypt mode
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decode Base64 and decrypt the data
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}