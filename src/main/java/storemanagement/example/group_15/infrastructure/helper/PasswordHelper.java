package storemanagement.example.group_15.infrastructure.helper;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHelper {
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.isEmpty() || hashedPassword == null || hashedPassword.isEmpty()) {
            throw new IllegalArgumentException("Password or hashed password cannot be null or empty");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static String generateSalt() {
        return BCrypt.gensalt(12);
    }

    public static String hashPasswordWithSalt(String plainPassword, String salt) {
        if (plainPassword == null || plainPassword.isEmpty() || salt == null || salt.isEmpty()) {
            throw new IllegalArgumentException("Password or salt cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, salt);
    }
}