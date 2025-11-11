package com.ecoride;

/**
 * Represents a vehicle in the EcoRide Car Rental System.
 * Demonstrates OOP: Encapsulation with private fields and public getters/setters.
 */
public class K2530341Vehicle {
    private String carId;
    private String model;
    private String category; // Compact Petrol, Hybrid, Electric, Luxury SUV
    private double dailyRentalPrice;
    private String availabilityStatus; // Available, Reserved, Under Maintenance

    // Constructor
    public K2530341Vehicle(String carId, String model, String category, double dailyRentalPrice, String availabilityStatus) {
        this.carId = carId;
        this.model = model;
        this.category = category;
        this.dailyRentalPrice = dailyRentalPrice;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and Setters
    public String getCarId() { return carId; }
    public void setCarId(String carId) { this.carId = carId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getDailyRentalPrice() { return dailyRentalPrice; }
    public void setDailyRentalPrice(double dailyRentalPrice) { this.dailyRentalPrice = dailyRentalPrice; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    // Method to check if available for booking
    public boolean isAvailable() {
        return "Available".equals(availabilityStatus);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "carId='" + carId + '\'' +
                ", model='" + model + '\'' +
                ", category='" + category + '\'' +
                ", dailyRentalPrice=" + dailyRentalPrice +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                '}';
    }
}
