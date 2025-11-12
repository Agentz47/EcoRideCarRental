package com.ecoride;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * System Health Monitor for EcoRide Car Rental System.
 * Monitors system performance, data integrity, and provides diagnostics.
 * Demonstrates OOP: Singleton pattern and system monitoring.
 */
public class K2530341SystemHealthMonitor {
    private static K2530341SystemHealthMonitor instance;
    private K2530341RentalSystem rentalSystem;

    private K2530341SystemHealthMonitor() {}

    public static synchronized K2530341SystemHealthMonitor getInstance() {
        if (instance == null) {
            instance = new K2530341SystemHealthMonitor();
        }
        return instance;
    }

    public void setRentalSystem(K2530341RentalSystem rentalSystem) {
        this.rentalSystem = rentalSystem;
    }

    /**
     * Perform comprehensive system health check
     */
    public SystemHealthReport performHealthCheck() {
        SystemHealthReport report = new SystemHealthReport();

        // Check data files
        report.setDataFilesStatus(checkDataFiles());

        // Check data integrity
        report.setDataIntegrityStatus(checkDataIntegrity());

        // Check system resources
        report.setSystemResourcesStatus(checkSystemResources());

        // Check business rules compliance
        report.setBusinessRulesStatus(checkBusinessRules());

        // Generate overall status
        report.setOverallStatus(determineOverallStatus(report));

        // Log the health check
        K2530341AuditLogger.getInstance().log("SYSTEM", "HEALTH_CHECK",
            String.format("Health check completed: %s", report.getOverallStatus()));

        return report;
    }

    /**
     * Check if all required data files exist and are accessible
     */
    private HealthStatus checkDataFiles() {
        String[] requiredFiles = {
            "vehicles.csv", "customers.csv", "bookings.csv",
            "users.csv", "employees.csv", "audit.log"
        };

        for (String filename : requiredFiles) {
            if (!Files.exists(Paths.get(filename))) {
                return HealthStatus.CRITICAL;
            }

            File file = new File(filename);
            if (!file.canRead()) {
                return HealthStatus.WARNING;
            }
        }

        return HealthStatus.HEALTHY;
    }

    /**
     * Check data integrity across all entities
     */
    private HealthStatus checkDataIntegrity() {
        try {
            // Check for orphaned bookings (bookings without valid customers/vehicles)
            for (K2530341Booking booking : rentalSystem.getAllBookings()) {
                if (rentalSystem.getCustomer(booking.getCustomer().getNicOrPassport()) == null) {
                    return HealthStatus.CRITICAL; // Orphaned booking
                }
                if (rentalSystem.getVehicle(booking.getVehicle().getCarId()) == null) {
                    return HealthStatus.CRITICAL; // Orphaned booking
                }
            }

            // Check for duplicate IDs
            if (hasDuplicateIds()) {
                return HealthStatus.WARNING;
            }

            // Check date consistency
            for (K2530341Booking booking : rentalSystem.getAllBookings()) {
                if (booking.getStartDate().isAfter(booking.getEndDate())) {
                    return HealthStatus.WARNING; // Invalid date range
                }
            }

        } catch (Exception e) {
            K2530341AuditLogger.getInstance().logSystemError("Data integrity check failed", e.getMessage());
            return HealthStatus.CRITICAL;
        }

        return HealthStatus.HEALTHY;
    }

    /**
     * Check system resources and performance
     */
    private HealthStatus checkSystemResources() {
        // Check available memory
        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        double memoryUsage = ((double)(totalMemory - freeMemory) / totalMemory) * 100;

        if (memoryUsage > 90) {
            return HealthStatus.WARNING;
        }

        // Check data file sizes (shouldn't be too large for CSV approach)
        try {
            for (String filename : new String[]{"vehicles.csv", "customers.csv", "bookings.csv"}) {
                File file = new File(filename);
                if (file.exists() && file.length() > 10 * 1024 * 1024) { // 10MB
                    return HealthStatus.WARNING; // File too large for CSV
                }
            }
        } catch (Exception e) {
            return HealthStatus.WARNING;
        }

        return HealthStatus.HEALTHY;
    }

    /**
     * Check compliance with business rules
     */
    private HealthStatus checkBusinessRules() {
        LocalDate today = java.time.LocalDate.now();

        for (K2530341Booking booking : rentalSystem.getAllBookings()) {
            // Check advance booking rule (at least 3 days)
            if (booking.getStartDate().isBefore(today.plusDays(3)) &&
                booking.getStartDate().isAfter(today)) {
                return HealthStatus.WARNING; // Too short notice
            }

            // Check for invalid vehicle assignments
            if (!"Reserved".equals(booking.getVehicle().getAvailabilityStatus()) &&
                booking.getStartDate().isBefore(today) && booking.getEndDate().isAfter(today)) {
                return HealthStatus.WARNING; // Active booking with wrong status
            }
        }

        return HealthStatus.HEALTHY;
    }

    /**
     * Check for duplicate IDs across entities
     */
    private boolean hasDuplicateIds() {
        // Check vehicle IDs
        java.util.Set<String> vehicleIds = new java.util.HashSet<>();
        for (K2530341Vehicle v : rentalSystem.getAllVehicles()) {
            if (!vehicleIds.add(v.getCarId())) return true;
        }

        // Check customer NICs
        java.util.Set<String> customerNics = new java.util.HashSet<>();
        for (K2530341Customer c : rentalSystem.getAllCustomers()) {
            if (!customerNics.add(c.getNicOrPassport())) return true;
        }

        // Check booking IDs
        java.util.Set<String> bookingIds = new java.util.HashSet<>();
        for (K2530341Booking b : rentalSystem.getAllBookings()) {
            if (!bookingIds.add(b.getBookingId())) return true;
        }

        return false;
    }

    /**
     * Determine overall system health status
     */
    private HealthStatus determineOverallStatus(SystemHealthReport report) {
        if (report.getDataFilesStatus() == HealthStatus.CRITICAL ||
            report.getDataIntegrityStatus() == HealthStatus.CRITICAL ||
            report.getBusinessRulesStatus() == HealthStatus.CRITICAL) {
            return HealthStatus.CRITICAL;
        }

        if (report.getDataFilesStatus() == HealthStatus.WARNING ||
            report.getDataIntegrityStatus() == HealthStatus.WARNING ||
            report.getSystemResourcesStatus() == HealthStatus.WARNING ||
            report.getBusinessRulesStatus() == HealthStatus.WARNING) {
            return HealthStatus.WARNING;
        }

        return HealthStatus.HEALTHY;
    }

    /**
     * Generate system diagnostics report
     */
    public String generateDiagnosticsReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== SYSTEM DIAGNOSTICS REPORT ===\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");

        // System information
        report.append("System Information:\n");
        report.append("- Java Version: ").append(System.getProperty("java.version")).append("\n");
        report.append("- OS: ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append("\n");
        report.append("- Available Processors: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
        report.append("- Total Memory: ").append(Runtime.getRuntime().totalMemory() / 1024 / 1024).append(" MB\n");
        report.append("- Free Memory: ").append(Runtime.getRuntime().freeMemory() / 1024 / 1024).append(" MB\n\n");

        // Data statistics
        report.append("Data Statistics:\n");
        report.append("- Vehicles: ").append(rentalSystem.getAllVehicles().size()).append("\n");
        report.append("- Customers: ").append(rentalSystem.getAllCustomers().size()).append("\n");
        report.append("- Bookings: ").append(rentalSystem.getAllBookings().size()).append("\n");
        report.append("- Users: ").append("N/A (authentication system)").append("\n\n");

        // File information
        report.append("File Information:\n");
        String[] files = {"vehicles.csv", "customers.csv", "bookings.csv", "users.csv", "audit.log"};
        for (String filename : files) {
            File file = new File(filename);
            if (file.exists()) {
                report.append(String.format("- %s: %.2f KB\n", filename, file.length() / 1024.0));
            } else {
                report.append(String.format("- %s: NOT FOUND\n", filename));
            }
        }

        return report.toString();
    }

    /**
     * Health status enumeration
     */
    public enum HealthStatus {
        HEALTHY("✓ Healthy"),
        WARNING("⚠ Warning"),
        CRITICAL("✗ Critical");

        private final String displayName;

        HealthStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * System health report class
     */
    public static class SystemHealthReport {
        private HealthStatus dataFilesStatus;
        private HealthStatus dataIntegrityStatus;
        private HealthStatus systemResourcesStatus;
        private HealthStatus businessRulesStatus;
        private HealthStatus overallStatus;

        // Getters and setters
        public HealthStatus getDataFilesStatus() { return dataFilesStatus; }
        public void setDataFilesStatus(HealthStatus dataFilesStatus) { this.dataFilesStatus = dataFilesStatus; }

        public HealthStatus getDataIntegrityStatus() { return dataIntegrityStatus; }
        public void setDataIntegrityStatus(HealthStatus dataIntegrityStatus) { this.dataIntegrityStatus = dataIntegrityStatus; }

        public HealthStatus getSystemResourcesStatus() { return systemResourcesStatus; }
        public void setSystemResourcesStatus(HealthStatus systemResourcesStatus) { this.systemResourcesStatus = systemResourcesStatus; }

        public HealthStatus getBusinessRulesStatus() { return businessRulesStatus; }
        public void setBusinessRulesStatus(HealthStatus businessRulesStatus) { this.businessRulesStatus = businessRulesStatus; }

        public HealthStatus getOverallStatus() { return overallStatus; }
        public void setOverallStatus(HealthStatus overallStatus) { this.overallStatus = overallStatus; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== SYSTEM HEALTH REPORT ===\n");
            sb.append("Overall Status: ").append(overallStatus.getDisplayName()).append("\n\n");
            sb.append("Component Status:\n");
            sb.append("- Data Files: ").append(dataFilesStatus.getDisplayName()).append("\n");
            sb.append("- Data Integrity: ").append(dataIntegrityStatus.getDisplayName()).append("\n");
            sb.append("- System Resources: ").append(systemResourcesStatus.getDisplayName()).append("\n");
            sb.append("- Business Rules: ").append(businessRulesStatus.getDisplayName()).append("\n");
            return sb.toString();
        }
    }
}
