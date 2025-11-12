package com.ecoride;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a user in the EcoRide Car Rental System.
 * Demonstrates OOP: Encapsulation.
 */
public class K2530341User {
    private String username;
    private String passwordHash;
    private String role; // "Customer" or "Admin"
    private String employeeId; // Only for Admin role
    private String nicOrPassport; // NIC for customers

    // Constructor
    public K2530341User(String username, String password, String role, String employeeId) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.role = role;
        this.employeeId = employeeId;
    }

    // Constructor for customers with NIC
    public K2530341User(String username, String password, String role, String employeeId, String nicOrPassport) {
        this(username, password, role, employeeId);
        this.nicOrPassport = nicOrPassport;
    }

    // Hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Verify password
    public boolean verifyPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getNicOrPassport() { return nicOrPassport; }
    public void setNicOrPassport(String nicOrPassport) { this.nicOrPassport = nicOrPassport; }

    public boolean isAdmin() {
        return "Admin".equals(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }
}
