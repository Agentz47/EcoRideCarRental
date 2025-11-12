package com.ecoride;

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles authentication and user management for the EcoRide Car Rental System.
 * Demonstrates data structures: HashMap for user lookup.
 */
public class K2530341AuthSystem {
    private Map<String, K2530341User> users; // Key: username
    private List<String> validEmployeeIds;
    private K2530341User currentUser;

    public K2530341AuthSystem() {
        users = new HashMap<>();
        validEmployeeIds = new ArrayList<>();
        loadEmployeeIds();
        loadUsers();
    }

    // Load valid employee IDs from CSV
    private void loadEmployeeIds() {
        Path path = Paths.get("employees.csv");
        if (!Files.exists(path)) {
            // Create default employees
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("EMP001,John Doe\n");
                writer.write("EMP002,Jane Smith\n");
                writer.write("EMP003,Bob Johnson\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    validEmployeeIds.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load users from CSV
    private void loadUsers() {
        Path path = Paths.get("users.csv");
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    K2530341User user;
                    if (parts.length >= 5 && !parts[4].trim().isEmpty()) {
                        user = new K2530341User(parts[0], "", parts[2], parts[3], parts[4]);
                    } else {
                        user = new K2530341User(parts[0], "", parts[2], parts[3]);
                    }
                    user.setPasswordHash(parts[1]); // Set hash directly
                    users.put(user.getUsername(), user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save users to CSV
    private void saveUsers() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("users.csv"))) {
            for (K2530341User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPasswordHash() + "," + user.getRole() + "," + user.getEmployeeId() + "," + (user.getNicOrPassport() != null ? user.getNicOrPassport() : "") + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Register a new user
    public boolean registerUser(String username, String password, String role, String employeeId) {
        if (users.containsKey(username)) {
            return false; // Username exists
        }
        if ("Admin".equals(role) && !validEmployeeIds.contains(employeeId)) {
            return false; // Invalid employee ID
        }
        K2530341User user = new K2530341User(username, password, role, employeeId);
        users.put(username, user);
        saveUsers();
        return true;
    }

    // Register a customer with NIC
    public boolean registerCustomer(String username, String password, String nic, String name, String contact, String email) {
        if (users.containsKey(username)) {
            return false; // Username exists
        }
        K2530341User user = new K2530341User(username, password, "Customer", "", nic);
        users.put(username, user);
        saveUsers();
        return true;
    }

    // Login user
    public K2530341User loginUser(String username, String password) {
        K2530341User user = users.get(username);
        if (user != null && user.verifyPassword(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }

    // Logout
    public void logout() {
        currentUser = null;
    }

    // Get current user
    public K2530341User getCurrentUser() {
        return currentUser;
    }

    // Check if employee ID is valid
    public boolean isValidEmployeeId(String employeeId) {
        return validEmployeeIds.contains(employeeId);
    }
}
