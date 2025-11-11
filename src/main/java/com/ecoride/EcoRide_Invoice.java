package com.ecoride;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an invoice for a booking in the EcoRide Car Rental System.
 * Demonstrates algorithms: Fee calculation with discounts, tax, extra km charges.
 */
public class EcoRide_Invoice {
    private EcoRide_Booking booking;
    private double basePrice;
    private double extraKmCharge;
    private double discount;
    private double tax;
    private double totalAmount;

    // Pricing data (from Table 1)
    private static final Map<String, double[]> PRICING = new HashMap<>();
    static {
        PRICING.put("Compact Petrol", new double[]{5000, 100, 50, 0.10});
        PRICING.put("Hybrid", new double[]{7500, 150, 60, 0.12});
        PRICING.put("Electric", new double[]{10000, 200, 40, 0.08});
        PRICING.put("Luxury SUV", new double[]{15000, 250, 75, 0.15});
        PRICING.put("Racing", new double[]{25000, 250, 75, 0.15});
        PRICING.put("Off road SUV", new double[]{20000, 250, 75, 0.15});
        PRICING.put("Super luxury", new double[]{35000, 250, 75, 0.15});
    }

    // Constructor
    public EcoRide_Invoice(EcoRide_Booking booking) {
        this.booking = booking;
        calculateFees();
    }

    // Algorithm: Calculate fees
    private void calculateFees() {
        String category = booking.getVehicle().getCategory().trim().toLowerCase();
        double[] pricing = null;
        for (String key : PRICING.keySet()) {
            if (key.toLowerCase().equals(category)) {
                pricing = PRICING.get(key);
                break;
            }
        }
        if (pricing == null) return;

        double dailyFee = pricing[0];
        int freeKm = (int) pricing[1];
        double extraKmRate = pricing[2];
        double taxRate = pricing[3];

        long days = booking.getNumberOfDays();
        if (days <= 0) {
            basePrice = 0;
            extraKmCharge = 0;
            discount = 0;
            tax = 0;
            totalAmount = 0;
            return;
        }
        basePrice = dailyFee * days;

        int extraKm = Math.max(0, booking.getTotalKm() - freeKm);
        extraKmCharge = extraKm * extraKmRate;

        // Discount: 10% if 7+ days, applied before tax
        discount = (days >= 7) ? basePrice * 0.10 : 0;

        double subtotal = basePrice + extraKmCharge - discount;
        tax = subtotal * taxRate;
        totalAmount = subtotal + tax;
    }

    // Getters
    public EcoRide_Booking getBooking() { return booking; }
    public double getBasePrice() { return basePrice; }
    public double getExtraKmCharge() { return extraKmCharge; }
    public double getDiscount() { return discount; }
    public double getTax() { return tax; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "Invoice for Booking " + booking.getBookingId() + ":\n" +
                "Car: " + booking.getVehicle().getModel() + " (" + booking.getVehicle().getCategory() + ")\n" +
                "Customer: " + booking.getCustomer().getName() + "\n" +
                "Duration: " + booking.getNumberOfDays() + " days\n" +
                "Mileage: " + booking.getTotalKm() + " km\n" +
                "Base Price: LKR " + basePrice + "\n" +
                "Extra Km Charge: LKR " + extraKmCharge + "\n" +
                "Discount: LKR " + discount + "\n" +
                "Tax: LKR " + tax + "\n" +
                "Deposit: LKR " + booking.getDeposit() + "\n" +
                "Total Payable: LKR " + (totalAmount - booking.getDeposit());
    }
}
