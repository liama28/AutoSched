package com.example.AutoSched.HttpBodies;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AccountCredentials {
    private String name;

    private String password;

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String passwordHash() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] pwHash = md.digest(this.password.getBytes(StandardCharsets.UTF_8));
        return new String(pwHash, StandardCharsets.UTF_8);
    }

}
