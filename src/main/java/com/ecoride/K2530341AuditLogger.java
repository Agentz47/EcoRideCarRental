package com.ecoride;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Audit Logger for EcoRide Car Rental System.
 * Logs all system activities for security and debugging purposes.
 * Demonstrates OOP: Singleton pattern and file I/O operations.
 */
public class K2530341AuditLogger {
    private static K2530341AuditLogger instance;
    private static final String LOG_FILE = "audit.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private K2530341AuditLogger() {
        // Initialize log file
        try {
            Path logPath = Paths.get(LOG_FILE);
            if (!Files.exists(logPath)) {
                Files.createFile(logPath);
                log("SYSTEM", "AUDIT_LOG_INITIALIZED", "Audit logging system started");
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize audit log: " + e.getMessage());
        }
    }

    /**
     * Get singleton instance of audit logger
     */
    public static synchronized K2530341AuditLogger getInstance() {
        if (instance == null) {
            instance = new K2530341AuditLogger();
        }
        return instance;
    }

    /**
     * Log a system action
     */
    public void log(String user, String action, String details) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String logEntry = String.format("[%s] USER:%s ACTION:%s DETAILS:%s",
            timestamp, user, action, details);

        writeToFile(logEntry);
    }

    /**
     * Log user authentication events
     */
    public void logAuthentication(String username, boolean success, String details) {
        String action = success ? "LOGIN_SUCCESS" : "LOGIN_FAILED";
        log(username, action, details);
    }

    /**
     * Log booking operations
     */
    public void logBookingOperation(String user, String operation, String bookingId, String details) {
        log(user, "BOOKING_" + operation.toUpperCase(),
            String.format("BookingID:%s %s", bookingId, details));
    }

    /**
     * Log vehicle operations
     */
    public void logVehicleOperation(String user, String operation, String vehicleId, String details) {
        log(user, "VEHICLE_" + operation.toUpperCase(),
            String.format("VehicleID:%s %s", vehicleId, details));
    }

    /**
     * Log customer operations
     */
    public void logCustomerOperation(String user, String operation, String customerNic, String details) {
        log(user, "CUSTOMER_" + operation.toUpperCase(),
            String.format("CustomerNIC:%s %s", customerNic, details));
    }

    /**
     * Log security violations
     */
    public void logSecurityViolation(String user, String violation, String details) {
        log(user, "SECURITY_VIOLATION", String.format("%s: %s", violation, details));
    }

    /**
     * Log system errors
     */
    public void logSystemError(String error, String details) {
        log("SYSTEM", "ERROR", String.format("%s: %s", error, details));
    }

    /**
     * Write log entry to file
     */
    private void writeToFile(String logEntry) {
        try {
            Files.write(Paths.get(LOG_FILE), (logEntry + "\n").getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to write to audit log: " + e.getMessage());
        }
    }

    /**
     * Get recent log entries
     */
    public String getRecentLogs(int lines) {
        try {
            return Files.lines(Paths.get(LOG_FILE))
                .skip(Math.max(0, Files.lines(Paths.get(LOG_FILE)).count() - lines))
                .reduce("", (a, b) -> a + "\n" + b);
        } catch (IOException e) {
            return "Error reading log file: " + e.getMessage();
        }
    }

    /**
     * Search logs for specific user actions
     */
    public String searchUserLogs(String username) {
        try {
            return Files.lines(Paths.get(LOG_FILE))
                .filter(line -> line.contains("USER:" + username))
                .reduce("", (a, b) -> a + "\n" + b);
        } catch (IOException e) {
            return "Error searching log file: " + e.getMessage();
        }
    }

    /**
     * Get logs within date range
     */
    public String getLogsByDateRange(String startDate, String endDate) {
        try {
            return Files.lines(Paths.get(LOG_FILE))
                .filter(line -> {
                    String date = line.substring(1, 11); // Extract date from [yyyy-MM-dd
                    return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
                })
                .reduce("", (a, b) -> a + "\n" + b);
        } catch (IOException e) {
            return "Error filtering log file: " + e.getMessage();
        }
    }
}
