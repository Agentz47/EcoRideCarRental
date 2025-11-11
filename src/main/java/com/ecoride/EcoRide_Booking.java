package com.ecoride;

import java.time.LocalDate;

/**
 * Represents a booking in the EcoRide Car Rental System.
 * Demonstrates OOP: Encapsulation, association with Customer and Vehicle.
 */
public class EcoRide_Booking {
    private String bookingId;
    private EcoRide_Customer customer;
    private EcoRide_Vehicle vehicle;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalKm;
    private double deposit; // Refundable LKR 5,000

    // Constructor
    public EcoRide_Booking(String bookingId, EcoRide_Customer customer, EcoRide_Vehicle vehicle,
                            LocalDate startDate, LocalDate endDate, int totalKm) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalKm = totalKm;
        this.deposit = 5000.0; // Fixed deposit
    }

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public EcoRide_Customer getCustomer() { return customer; }
    public void setCustomer(EcoRide_Customer customer) { this.customer = customer; }

    public EcoRide_Vehicle getVehicle() { return vehicle; }
    public void setVehicle(EcoRide_Vehicle vehicle) { this.vehicle = vehicle; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getTotalKm() { return totalKm; }
    public void setTotalKm(int totalKm) { this.totalKm = totalKm; }

    public double getDeposit() { return deposit; }

    // Calculate number of days
    public long getNumberOfDays() {
        if (startDate.isAfter(endDate)) {
            return 0; // Invalid, but handle gracefully
        }
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1; // Inclusive
    }

    // Validation: Check if booking can be made (vehicle available, at least 3 days in advance, valid dates)
    public boolean isValidBooking() {
        LocalDate today = LocalDate.now();
        return vehicle.isAvailable() && startDate.isAfter(today.plusDays(2)) && !startDate.isAfter(endDate); // At least 3 days prior, start <= end
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", customer=" + customer.getName() +
                ", vehicle=" + vehicle.getModel() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalKm=" + totalKm +
                ", deposit=" + deposit +
                '}';
    }
}
