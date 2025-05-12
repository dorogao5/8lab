package ru.lab.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class User {
    private final String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
