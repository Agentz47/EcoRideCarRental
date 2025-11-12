package com.ecoride;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Search functionality for EcoRide Car Rental System.
 * Provides sophisticated filtering and search capabilities.
 * Demonstrates OOP: Utility class with static methods and algorithms.
 */
public class K2530341AdvancedSearch {

    /**
     * Search vehicles with multiple criteria
     */
    public static List<K2530341Vehicle> searchVehicles(K2530341RentalSystem rentalSystem,
            String category, double maxPrice, LocalDate startDate, LocalDate endDate,
            String availabilityStatus, String modelKeyword) {

        List<K2530341Vehicle> allVehicles = rentalSystem.getAllVehicles();
        List<K2530341Vehicle> results = new ArrayList<>();

        for (K2530341Vehicle vehicle : allVehicles) {
            // Category filter
            if (category != null && !category.isEmpty() &&
                !vehicle.getCategory().toLowerCase().contains(category.toLowerCase())) {
                continue;
            }

            // Price filter
            if (maxPrice > 0 && vehicle.getDailyRentalPrice() > maxPrice) {
                continue;
            }

            // Availability status filter
            if (availabilityStatus != null && !availabilityStatus.isEmpty() &&
                !vehicle.getAvailabilityStatus().equalsIgnoreCase(availabilityStatus)) {
                continue;
            }

            // Model keyword filter
            if (modelKeyword != null && !modelKeyword.isEmpty() &&
                !vehicle.getModel().toLowerCase().contains(modelKeyword.toLowerCase())) {
                continue;
            }

            // Date availability check (if dates provided)
            if (startDate != null && endDate != null) {
                if (!isVehicleAvailableForDates(rentalSystem, vehicle, startDate, endDate)) {
                    continue;
                }
            }

            results.add(vehicle);
        }

        return results;
    }

    /**
     * Search bookings with multiple criteria
     */
    public static List<K2530341Booking> searchBookings(K2530341RentalSystem rentalSystem,
            String customerName, String vehicleModel, LocalDate startDate, LocalDate endDate,
            String bookingId, String status) {

        List<K2530341Booking> allBookings = rentalSystem.getAllBookings();
        List<K2530341Booking> results = new ArrayList<>();

        for (K2530341Booking booking : allBookings) {
            // Customer name filter
            if (customerName != null && !customerName.isEmpty() &&
                !booking.getCustomer().getName().toLowerCase().contains(customerName.toLowerCase())) {
                continue;
            }

            // Vehicle model filter
            if (vehicleModel != null && !vehicleModel.isEmpty() &&
                !booking.getVehicle().getModel().toLowerCase().contains(vehicleModel.toLowerCase())) {
                continue;
            }

            // Date range filter
            if (startDate != null && booking.getStartDate().isBefore(startDate)) {
                continue;
            }
            if (endDate != null && booking.getEndDate().isAfter(endDate)) {
                continue;
            }

            // Booking ID filter
            if (bookingId != null && !bookingId.isEmpty() &&
                !booking.getBookingId().toLowerCase().contains(bookingId.toLowerCase())) {
                continue;
            }

            // Status filter (based on dates relative to today)
            if (status != null && !status.isEmpty()) {
                LocalDate today = LocalDate.now();
                boolean matchesStatus = false;

                switch (status.toLowerCase()) {
                    case "upcoming":
                        matchesStatus = booking.getStartDate().isAfter(today);
                        break;
                    case "active":
                        matchesStatus = !booking.getStartDate().isAfter(today) &&
                                       !booking.getEndDate().isBefore(today);
                        break;
                    case "completed":
                        matchesStatus = booking.getEndDate().isBefore(today);
                        break;
                    case "cancelable":
                        matchesStatus = booking.canCancel();
                        break;
                }

                if (!matchesStatus) continue;
            }

            results.add(booking);
        }

        return results;
    }

    /**
     * Find best available vehicles for a date range and budget
     */
    public static List<K2530341Vehicle> findBestMatches(K2530341RentalSystem rentalSystem,
            LocalDate startDate, LocalDate endDate, double maxBudget, String preferredCategory) {

        List<K2530341Vehicle> availableVehicles = new ArrayList<>();
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Find all available vehicles for the date range
        for (K2530341Vehicle vehicle : rentalSystem.getAllVehicles()) {
            if (isVehicleAvailableForDates(rentalSystem, vehicle, startDate, endDate)) {
                availableVehicles.add(vehicle);
            }
        }

        // Sort by preference: category match, then price
        availableVehicles.sort((v1, v2) -> {
            // First priority: preferred category
            boolean v1Preferred = preferredCategory != null &&
                v1.getCategory().equalsIgnoreCase(preferredCategory);
            boolean v2Preferred = preferredCategory != null &&
                v2.getCategory().equalsIgnoreCase(preferredCategory);

            if (v1Preferred && !v2Preferred) return -1;
            if (!v1Preferred && v2Preferred) return 1;

            // Second priority: price (lower first)
            double v1Total = v1.getDailyRentalPrice() * days;
            double v2Total = v2.getDailyRentalPrice() * days;

            // Apply discount for 7+ days
            if (days >= 7) {
                v1Total *= 0.9;
                v2Total *= 0.9;
            }

            return Double.compare(v1Total, v2Total);
        });

        // Filter by budget
        List<K2530341Vehicle> budgetMatches = new ArrayList<>();
        for (K2530341Vehicle vehicle : availableVehicles) {
            double estimatedCost = K2530341FeeCalculator.calculateEstimatedFee(
                new K2530341Booking("TEMP", null, vehicle, startDate, endDate, 0));

            if (estimatedCost <= maxBudget) {
                budgetMatches.add(vehicle);
            }
        }

        return budgetMatches;
    }

    /**
     * Check if a vehicle is available for specific dates
     */
    private static boolean isVehicleAvailableForDates(K2530341RentalSystem rentalSystem,
            K2530341Vehicle vehicle, LocalDate startDate, LocalDate endDate) {

        // Check if vehicle status is available
        if (!"Available".equals(vehicle.getAvailabilityStatus())) {
            return false;
        }

        // Check for conflicting bookings
        List<K2530341Booking> bookings = rentalSystem.getAllBookings();
        for (K2530341Booking booking : bookings) {
            if (booking.getVehicle().getCarId().equals(vehicle.getCarId())) {
                // Check for date overlap
                if (!(endDate.isBefore(booking.getStartDate()) ||
                      startDate.isAfter(booking.getEndDate()))) {
                    return false; // Overlap found
                }
            }
        }

        return true;
    }

    /**
     * Get vehicle recommendations based on customer history
     */
    public static List<K2530341Vehicle> getRecommendations(K2530341RentalSystem rentalSystem,
            String customerNic) {

        List<K2530341Booking> customerBookings = rentalSystem.getBookingsByCustomerNic(customerNic);
        List<K2530341Vehicle> recommendations = new ArrayList<>();

        if (customerBookings.isEmpty()) {
            // New customer - recommend popular categories
            return getPopularVehicles(rentalSystem);
        }

        // Analyze customer's preferences
        String favoriteCategory = findFavoriteCategory(customerBookings);
        double avgBudget = calculateAverageBudget(customerBookings);

        // Find similar vehicles in preferred category within budget
        for (K2530341Vehicle vehicle : rentalSystem.getAllVehicles()) {
            if (vehicle.getCategory().equals(favoriteCategory) &&
                vehicle.getDailyRentalPrice() <= avgBudget &&
                "Available".equals(vehicle.getAvailabilityStatus())) {
                recommendations.add(vehicle);
            }
        }

        // If not enough recommendations, add from other categories
        if (recommendations.size() < 3) {
            for (K2530341Vehicle vehicle : rentalSystem.getAllVehicles()) {
                if (!vehicle.getCategory().equals(favoriteCategory) &&
                    vehicle.getDailyRentalPrice() <= avgBudget * 1.2 && // 20% buffer
                    "Available".equals(vehicle.getAvailabilityStatus()) &&
                    !recommendations.contains(vehicle)) {
                    recommendations.add(vehicle);
                    if (recommendations.size() >= 5) break;
                }
            }
        }

        return recommendations;
    }

    /**
     * Find customer's favorite vehicle category
     */
    private static String findFavoriteCategory(List<K2530341Booking> bookings) {
        java.util.Map<String, Integer> categoryCount = new java.util.HashMap<>();

        for (K2530341Booking booking : bookings) {
            String category = booking.getVehicle().getCategory();
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }

        return categoryCount.entrySet().stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(java.util.Map.Entry::getKey)
            .orElse("Hybrid"); // Default fallback
    }

    /**
     * Calculate customer's average daily budget
     */
    private static double calculateAverageBudget(List<K2530341Booking> bookings) {
        double totalSpent = 0;
        int totalDays = 0;

        for (K2530341Booking booking : bookings) {
            double cost = K2530341FeeCalculator.calculateEstimatedFee(booking);
            long days = booking.getNumberOfDays();
            totalSpent += cost;
            totalDays += days;
        }

        return totalDays > 0 ? totalSpent / totalDays : 7500.0; // Default daily budget
    }

    /**
     * Get most popular vehicles (by booking count)
     */
    private static List<K2530341Vehicle> getPopularVehicles(K2530341RentalSystem rentalSystem) {
        java.util.Map<String, Integer> vehicleBookings = new java.util.HashMap<>();
        List<K2530341Booking> allBookings = rentalSystem.getAllBookings();

        // Count bookings per vehicle
        for (K2530341Booking booking : allBookings) {
            String vehicleId = booking.getVehicle().getCarId();
            vehicleBookings.put(vehicleId, vehicleBookings.getOrDefault(vehicleId, 0) + 1);
        }

        // Sort vehicles by booking count
        List<K2530341Vehicle> sortedVehicles = new ArrayList<>(rentalSystem.getAllVehicles());
        sortedVehicles.sort((v1, v2) -> {
            int count1 = vehicleBookings.getOrDefault(v1.getCarId(), 0);
            int count2 = vehicleBookings.getOrDefault(v2.getCarId(), 0);
            return Integer.compare(count2, count1); // Descending order
        });

        // Return top vehicles that are available
        List<K2530341Vehicle> popular = new ArrayList<>();
        for (K2530341Vehicle vehicle : sortedVehicles) {
            if ("Available".equals(vehicle.getAvailabilityStatus())) {
                popular.add(vehicle);
                if (popular.size() >= 5) break;
            }
        }

        return popular;
    }
}
