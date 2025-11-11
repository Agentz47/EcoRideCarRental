package com.ecoride;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the EcoRide Car Rental System.
 * Demonstrates data structures: ArrayList for lists, HashMap for quick lookups.
 * OOP: Composition, encapsulation.
 */
public class EcoRide_RentalSystem {
    private ArrayList<EcoRide_Vehicle> vehicles;
    private HashMap<String, EcoRide_Vehicle> vehicleMap; // Key: carId
    private ArrayList<EcoRide_Customer> customers;
    private HashMap<String, EcoRide_Customer> customerMap; // Key: nicOrPassport
    private ArrayList<EcoRide_Booking> bookings;
    private HashMap<String, EcoRide_Booking> bookingMap; // Key: bookingId

    public EcoRide_RentalSystem() {
        vehicles = new ArrayList<>();
        vehicleMap = new HashMap<>();
        customers = new ArrayList<>();
        customerMap = new HashMap<>();
        bookings = new ArrayList<>();
        bookingMap = new HashMap<>();
        initializeVehicles(); // Add some sample vehicles
    }

    // Initialize with sample vehicles
    private void initializeVehicles() {
        addVehicle(new EcoRide_Vehicle("V001", "Toyota Aqua", "Hybrid", 7500, "Available"));
        addVehicle(new EcoRide_Vehicle("V002", "Nissan Leaf", "Electric", 10000, "Available"));
        addVehicle(new EcoRide_Vehicle("V003", "BMW X5", "Luxury SUV", 15000, "Available"));
    }

    // CRUD for Vehicles
    public void addVehicle(EcoRide_Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicleMap.put(vehicle.getCarId(), vehicle);
    }

    public EcoRide_Vehicle getVehicle(String carId) {
        return vehicleMap.get(carId);
    }

    public void updateVehicle(String carId, EcoRide_Vehicle updatedVehicle) {
        EcoRide_Vehicle existing = vehicleMap.get(carId);
        if (existing != null) {
            vehicles.remove(existing);
            vehicles.add(updatedVehicle);
            vehicleMap.put(carId, updatedVehicle);
        }
    }

    public void removeVehicle(String carId) {
        EcoRide_Vehicle vehicle = vehicleMap.remove(carId);
        if (vehicle != null) {
            vehicles.remove(vehicle);
        }
    }

    public List<EcoRide_Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    // Customer registration
    public void registerCustomer(EcoRide_Customer customer) {
        customers.add(customer);
        customerMap.put(customer.getNicOrPassport(), customer);
    }

    public EcoRide_Customer getCustomer(String nicOrPassport) {
        return customerMap.get(nicOrPassport);
    }

    // Booking
    public boolean makeBooking(EcoRide_Booking booking) {
        if (booking.isValidBooking()) {
            bookings.add(booking);
            bookingMap.put(booking.getBookingId(), booking);
            booking.getVehicle().setAvailabilityStatus("Reserved");
            return true;
        }
        return false;
    }

    public EcoRide_Booking getBooking(String bookingId) {
        return bookingMap.get(bookingId);
    }

    // Search bookings by customer name
    public List<EcoRide_Booking> searchBookingsByName(String name) {
        List<EcoRide_Booking> results = new ArrayList<>();
        for (EcoRide_Booking booking : bookings) {
            if (booking.getCustomer().getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(booking);
            }
        }
        return results;
    }

    // Generate invoice
    public EcoRide_Invoice generateInvoice(EcoRide_Booking booking) {
        return new EcoRide_Invoice(booking);
    }
}
