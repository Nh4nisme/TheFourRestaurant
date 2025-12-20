package com.thefourrestaurant.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordHasher {
    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 32; // 256 bits
    private static final int PBKDF2_ITERATIONS = 65536;

    public static String hash(String password) {
        try {
            byte[] salt = new byte[SALT_BYTES];
            SecureRandom.getInstanceStrong().nextBytes(salt);
            byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTES);
            String saltB64 = Base64.getEncoder().encodeToString(salt);
            String hashB64 = Base64.getEncoder().encodeToString(hash);
            return saltB64 + "$" + hashB64;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    public static void main(String[] args) {
        String[][] samples = new String[][]{
                {"TK000001", "admin123", "Admin@123"},
                {"TK000002", "thungan01", "TNpass01"},
        };

        System.out.println("-- Hashed passwords (format salt$hash, Base64)");
        for (String[] s : samples) {
            String maTK = s[0];
            String username = s[1];
            String plain = s[2];
            String hashed = hash(plain);
            if (maTK != null) {
                System.out.printf("-- %s / %s -> %s%n", maTK, username, hashed);
            } else {
                System.out.printf("-- %s -> %s%n", plain, hashed);
            }
        }

        System.out.println();
        for (String[] s : samples) {
            String maTK = s[0];
            String username = s[1];
            String plain = s[2];
            String hashed = hash(plain);
            if (maTK != null) {
                System.out.printf("INSERT INTO TaiKhoan (maTK, tenDangNhap, matKhau, maVT) VALUES ('%s', '%s', '%s', '%s');%n",
                        maTK, username, hashed, maTK.equals("TK000001") ? "VT000001" : "VT000002");
            } else {
                System.out.printf("-- password '%s' -> '%s'%n", plain, hashed);
            }
        }
    }
}
