package com.ecoride;

/**
 * Fee Calculator for EcoRide Car Rental System.
 * Handles all fee calculations including base price, discounts, extra km charges, and taxes.
 * Demonstrates OOP: Utility class with static methods.
 */
public class K2530341FeeCalculator {

    // Package details constants
    private static final double COMPACT_PETROL_DAILY = 5000.0;
    private static final double HYBRID_DAILY = 7500.0;
    private static final double ELECTRIC_DAILY = 10000.0;
    private static final double LUXURY_SUV_DAILY = 15000.0;

    private static final int COMPACT_PETROL_FREE_KM = 100;
    private static final int HYBRID_FREE_KM = 150;
    private static final int ELECTRIC_FREE_KM = 200;
    private static final int LUXURY_SUV_FREE_KM = 250;

    private static final double COMPACT_PETROL_EXTRA_RATE = 50.0;
    private static final double HYBRID_EXTRA_RATE = 60.0;
    private static final double ELECTRIC_EXTRA_RATE = 40.0;
    private static final double LUXURY_SUV_EXTRA_RATE = 75.0;

    private static final double COMPACT_PETROL_TAX = 0.10;
    private static final double HYBRID_TAX = 0.12;
    private static final double ELECTRIC_TAX = 0.08;
    private static final double LUXURY_SUV_TAX = 0.15;

    private static final double DEPOSIT = 5000.0;
    private static final double DISCOUNT_RATE = 0.10; // 10% for 7+ days

    /**
     * Calculate total fee for a booking with actual kilometers driven
     */
    public static double calculateTotalFee(K2530341Booking booking, int actualKm) {
        String category = booking.getVehicle().getCategory();
        long days = booking.getNumberOfDays();

        // Calculate base price
        double basePrice = getDailyRate(category) * days;

        // Apply discount for 7+ days
        if (days >= 7) {
            basePrice *= (1 - DISCOUNT_RATE);
        }

        // Calculate extra km charges
        int freeKm = getFreeKmForCategory(category);
        int extraKm = Math.max(0, actualKm - (freeKm * (int)days));
        double extraKmCharge = extraKm * getExtraKmRateForCategory(category);

        // Calculate subtotal before tax
        double subtotal = basePrice + extraKmCharge;

        // Add tax
        double tax = subtotal * getTaxRateForCategory(category);

        // Add deposit (refundable)
        double total = subtotal + tax + DEPOSIT;

        return total;
    }

    /**
     * Calculate estimated fee for booking (without actual km)
     */
    public static double calculateEstimatedFee(K2530341Booking booking) {
        return calculateTotalFee(booking, booking.getTotalKm());
    }

    /**
     * Get daily rental rate for category
     */
    public static double getDailyRate(String category) {
        switch (category.toLowerCase()) {
            case "compact petrol": return COMPACT_PETROL_DAILY;
            case "hybrid": return HYBRID_DAILY;
            case "electric": return ELECTRIC_DAILY;
            case "luxury suv": return LUXURY_SUV_DAILY;
            default: return 0.0;
        }
    }

    /**
     * Get free km per day for category
     */
    public static int getFreeKmForCategory(String category) {
        switch (category.toLowerCase()) {
            case "compact petrol": return COMPACT_PETROL_FREE_KM;
            case "hybrid": return HYBRID_FREE_KM;
            case "electric": return ELECTRIC_FREE_KM;
            case "luxury suv": return LUXURY_SUV_FREE_KM;
            default: return 0;
        }
    }

    /**
     * Get extra km charge rate for category
     */
    public static double getExtraKmRateForCategory(String category) {
        switch (category.toLowerCase()) {
            case "compact petrol": return COMPACT_PETROL_EXTRA_RATE;
            case "hybrid": return HYBRID_EXTRA_RATE;
            case "electric": return ELECTRIC_EXTRA_RATE;
            case "luxury suv": return LUXURY_SUV_EXTRA_RATE;
            default: return 0.0;
        }
    }

    /**
     * Get tax rate for category
     */
    public static double getTaxRateForCategory(String category) {
        switch (category.toLowerCase()) {
            case "compact petrol": return COMPACT_PETROL_TAX;
            case "hybrid": return HYBRID_TAX;
            case "electric": return ELECTRIC_TAX;
            case "luxury suv": return LUXURY_SUV_TAX;
            default: return 0.0;
        }
    }

    /**
     * Get deposit amount
     */
    public static double getDeposit() {
        return DEPOSIT;
    }

    /**
     * Calculate discount amount for long-term rentals
     */
    public static double calculateDiscount(double basePrice, long days) {
        if (days >= 7) {
            return basePrice * DISCOUNT_RATE;
        }
        return 0.0;
    }

    /**
     * Generate detailed fee breakdown
     */
    public static String generateFeeBreakdown(K2530341Booking booking, int actualKm) {
        String category = booking.getVehicle().getCategory();
        long days = booking.getNumberOfDays();

        double dailyRate = getDailyRate(category);
        double basePrice = dailyRate * days;
        double discount = calculateDiscount(basePrice, days);
        double discountedBase = basePrice - discount;

        int freeKm = getFreeKmForCategory(category);
        int totalFreeKm = freeKm * (int)days;
        int extraKm = Math.max(0, actualKm - totalFreeKm);
        double extraKmRate = getExtraKmRateForCategory(category);
        double extraKmCharge = extraKm * extraKmRate;

        double subtotal = discountedBase + extraKmCharge;
        double taxRate = getTaxRateForCategory(category);
        double tax = subtotal * taxRate;

        double total = subtotal + tax + DEPOSIT;

        StringBuilder breakdown = new StringBuilder();
        breakdown.append("=== FEE BREAKDOWN ===\n");
        breakdown.append(String.format("Daily Rate: LKR %.2f\n", dailyRate));
        breakdown.append(String.format("Rental Days: %d\n", days));
        breakdown.append(String.format("Base Price: LKR %.2f\n", basePrice));

        if (discount > 0) {
            breakdown.append(String.format("Long-term Discount (10%%): -LKR %.2f\n", discount));
            breakdown.append(String.format("Discounted Base: LKR %.2f\n", discountedBase));
        }

        breakdown.append(String.format("Free Km per Day: %d km\n", freeKm));
        breakdown.append(String.format("Total Free Km: %d km\n", totalFreeKm));
        breakdown.append(String.format("Actual Km Driven: %d km\n", actualKm));
        breakdown.append(String.format("Extra Km: %d km\n", extraKm));
        breakdown.append(String.format("Extra Km Rate: LKR %.2f per km\n", extraKmRate));
        breakdown.append(String.format("Extra Km Charge: LKR %.2f\n", extraKmCharge));
        breakdown.append(String.format("Subtotal (before tax): LKR %.2f\n", subtotal));
        breakdown.append(String.format("Tax Rate: %.1f%%\n", taxRate * 100));
        breakdown.append(String.format("Tax Amount: LKR %.2f\n", tax));
        breakdown.append(String.format("Refundable Deposit: LKR %.2f\n", DEPOSIT));
        breakdown.append(String.format("TOTAL AMOUNT: LKR %.2f\n", total));

        return breakdown.toString();
    }
}
