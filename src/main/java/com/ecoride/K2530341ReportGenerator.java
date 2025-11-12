package com.ecoride;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Simple report generator for the rental system
// Handles different types of reports like revenue, utilization, etc.
public class K2530341ReportGenerator {

    // Creates a revenue report for bookings within a date range
    public static String generateRevenueReport(K2530341RentalSystem rentalSystem, LocalDate startDate, LocalDate endDate) {
        List<K2530341Booking> allBookings = rentalSystem.getAllBookings();
        Map<String, Double> revenueByCategory = new HashMap<>();
        Map<String, Integer> bookingsByCategory = new HashMap<>();
        double totalRevenue = 0.0;
        int totalBookings = 0;

        // Go through each booking and check if it's in our date range
        for (K2530341Booking booking : allBookings) {
            LocalDate bookingDate = booking.getStartDate();
            if ((bookingDate.isEqual(startDate) || bookingDate.isAfter(startDate)) &&
                (bookingDate.isEqual(endDate) || bookingDate.isBefore(endDate))) {

                String category = booking.getVehicle().getCategory();
                double revenue = K2530341FeeCalculator.calculateEstimatedFee(booking);

                // Add up revenue and booking count for each category
                revenueByCategory.put(category, revenueByCategory.getOrDefault(category, 0.0) + revenue);
                bookingsByCategory.put(category, bookingsByCategory.getOrDefault(category, 0) + 1);

                totalRevenue += revenue;
                totalBookings++;
            }
        }

        // Build the report text
        StringBuilder report = new StringBuilder();
        report.append("=== REVENUE REPORT ===\n");
        report.append(String.format("Period: %s to %s\n\n",
            startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        report.append("Revenue by Category:\n");
        for (Map.Entry<String, Double> entry : revenueByCategory.entrySet()) {
            String category = entry.getKey();
            double revenue = entry.getValue();
            int bookings = bookingsByCategory.get(category);
            double avgRevenue = bookings > 0 ? revenue / bookings : 0;

            report.append(String.format("- %s: LKR %.2f (%d bookings, Avg: LKR %.2f)\n",
                category, revenue, bookings, avgRevenue));
        }

        report.append(String.format("\nTotal Revenue: LKR %.2f\n", totalRevenue));
        report.append(String.format("Total Bookings: %d\n", totalBookings));
        report.append(String.format("Average Revenue per Booking: LKR %.2f\n",
            totalBookings > 0 ? totalRevenue / totalBookings : 0));

        return report.toString();
    }

    // Shows how many vehicles are available, reserved, etc. by category
    public static String generateUtilizationReport(K2530341RentalSystem rentalSystem) {
        List<K2530341Vehicle> vehicles = rentalSystem.getAllVehicles();
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Integer> availableByCategory = new HashMap<>();
        Map<String, Integer> reservedByCategory = new HashMap<>();
        Map<String, Integer> maintenanceByCategory = new HashMap<>();

        // Count vehicles by category and status
        for (K2530341Vehicle vehicle : vehicles) {
            String category = vehicle.getCategory();
            String status = vehicle.getAvailabilityStatus();

            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);

            // Figure out which status bucket this goes in
            switch (status.toLowerCase()) {
                case "available":
                    availableByCategory.put(category, availableByCategory.getOrDefault(category, 0) + 1);
                    break;
                case "reserved":
                    reservedByCategory.put(category, reservedByCategory.getOrDefault(category, 0) + 1);
                    break;
                case "under maintenance":
                    maintenanceByCategory.put(category, maintenanceByCategory.getOrDefault(category, 0) + 1);
                    break;
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("=== VEHICLE UTILIZATION REPORT ===\n\n");

        // Table header
        report.append(String.format("%-15s %-8s %-10s %-10s %-12s %-10s\n",
            "Category", "Total", "Available", "Reserved", "Maintenance", "Utilization"));
        report.append("-".repeat(75) + "\n");

        // Print each category's stats
        for (String category : categoryCount.keySet()) {
            int total = categoryCount.get(category);
            int available = availableByCategory.getOrDefault(category, 0);
            int reserved = reservedByCategory.getOrDefault(category, 0);
            int maintenance = maintenanceByCategory.getOrDefault(category, 0);
            double utilization = total > 0 ? ((double)(reserved + maintenance) / total) * 100 : 0;

            report.append(String.format("%-15s %-8d %-10d %-10d %-12d %-9.1f%%\n",
                category, total, available, reserved, maintenance, utilization));
        }

        return report.toString();
    }

    // Shows all bookings for a specific customer
    public static String generateCustomerReport(K2530341RentalSystem rentalSystem, String customerNic) {
        List<K2530341Booking> customerBookings = rentalSystem.getBookingsByCustomerNic(customerNic);
        K2530341Customer customer = rentalSystem.getCustomer(customerNic);

        if (customer == null) {
            return "Customer not found with NIC: " + customerNic;
        }

        StringBuilder report = new StringBuilder();
        report.append("=== CUSTOMER BOOKING REPORT ===\n");
        report.append(String.format("Customer: %s\n", customer.getName()));
        report.append(String.format("NIC/Passport: %s\n", customer.getNicOrPassport()));
        report.append(String.format("Contact: %s\n", customer.getContactNumber()));
        report.append(String.format("Email: %s\n\n", customer.getEmail()));

        if (customerBookings.isEmpty()) {
            report.append("No booking history found.\n");
            return report.toString();
        }

        report.append(String.format("Total Bookings: %d\n\n", customerBookings.size()));

        // Table header for bookings
        report.append(String.format("%-12s %-15s %-12s %-12s %-8s %-10s\n",
            "Booking ID", "Vehicle", "Start Date", "End Date", "Days", "Est. Cost"));
        report.append("-".repeat(85) + "\n");

        double totalSpent = 0.0;
        for (K2530341Booking booking : customerBookings) {
            double cost = K2530341FeeCalculator.calculateEstimatedFee(booking);
            totalSpent += cost;

            report.append(String.format("%-12s %-15s %-12s %-12s %-8d LKR %-8.2f\n",
                booking.getBookingId(),
                booking.getVehicle().getModel(),
                booking.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                booking.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                booking.getNumberOfDays(),
                cost));
        }

        report.append(String.format("\nTotal Amount Spent: LKR %.2f\n", totalSpent));
        report.append(String.format("Average Booking Cost: LKR %.2f\n",
            customerBookings.size() > 0 ? totalSpent / customerBookings.size() : 0));

        return report.toString();
    }

    // Quick overview of the whole system
    public static String generateSystemSummary(K2530341RentalSystem rentalSystem) {
        List<K2530341Vehicle> vehicles = rentalSystem.getAllVehicles();
        List<K2530341Customer> customers = rentalSystem.getAllCustomers();
        List<K2530341Booking> bookings = rentalSystem.getAllBookings();

        StringBuilder report = new StringBuilder();
        report.append("=== SYSTEM SUMMARY REPORT ===\n\n");

        report.append(String.format("Total Vehicles: %d\n", vehicles.size()));
        report.append(String.format("Total Customers: %d\n", customers.size()));
        report.append(String.format("Total Bookings: %d\n", bookings.size()));

        // Figure out how many vehicles are available vs reserved
        int availableCount = 0;
        int reservedCount = 0;
        Map<String, Integer> vehiclesByCategory = new HashMap<>();

        for (K2530341Vehicle vehicle : vehicles) {
            vehiclesByCategory.put(vehicle.getCategory(),
                vehiclesByCategory.getOrDefault(vehicle.getCategory(), 0) + 1);

            if ("Available".equals(vehicle.getAvailabilityStatus())) {
                availableCount++;
            } else if ("Reserved".equals(vehicle.getAvailabilityStatus())) {
                reservedCount++;
            }
        }

        report.append(String.format("Available Vehicles: %d\n", availableCount));
        report.append(String.format("Reserved Vehicles: %d\n", reservedCount));
        report.append(String.format("Current Utilization: %.1f%%\n\n",
            vehicles.size() > 0 ? ((double)reservedCount / vehicles.size()) * 100 : 0));

        report.append("Vehicles by Category:\n");
        for (Map.Entry<String, Integer> entry : vehiclesByCategory.entrySet()) {
            report.append(String.format("- %s: %d vehicles\n", entry.getKey(), entry.getValue()));
        }

        return report.toString();
    }
}
