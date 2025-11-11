package com.ecoride;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    /** Prevents saveData() from running during load. */
    private boolean isLoadingData = false;

    public EcoRide_RentalSystem() {
        vehicles = new ArrayList<>();
        vehicleMap = new HashMap<>();
        customers = new ArrayList<>();
        customerMap = new HashMap<>();
        bookings = new ArrayList<>();
        bookingMap = new HashMap<>();
        loadData(); // Load data from files
        if (vehicles.isEmpty()) {
            initializeVehicles(); // Add some sample vehicles if no data loaded
        }
    }

    // Initialize with sample vehicles
    private void initializeVehicles() {
        addVehicle(new EcoRide_Vehicle("V001", "Toyota Aqua", "Hybrid", 7500.0, "Available"));
        addVehicle(new EcoRide_Vehicle("V002", "Nissan Leaf", "Electric", 10000.0, "Available"));
        addVehicle(new EcoRide_Vehicle("V003", "BMW X5", "Luxury SUV", 15000.0, "Available"));
        // addVehicle will save since we are not loading now
    }

    // ------------ Load / Save orchestration ------------
    private void loadData() {
        isLoadingData = true;
        try {
            loadVehicles();
            loadCustomers();
            loadBookings();
        } finally {
            isLoadingData = false;
        }
    }

    private void saveData() {
        saveVehicles();
        saveCustomers();
        saveBookings();
    }

    // ------------ Vehicles ------------
    private void loadVehicles() {
        Path path = Paths.get("vehicles.csv");
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0];
                    String model = parts[1];
                    String category = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    String status = parts[4];

                    // Directly add to memory (DO NOT call addVehicle here)
                    EcoRide_Vehicle v = new EcoRide_Vehicle(id, model, category, price, status);
                    vehicles.add(v);
                    vehicleMap.put(id, v);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }
    }

    private void saveVehicles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("vehicles.csv"))) {
            for (EcoRide_Vehicle v : vehicles) {
                writer.println(v.getCarId() + "," + v.getModel() + "," + v.getCategory() + "," + v.getDailyRentalPrice() + "," + v.getAvailabilityStatus());
            }
        } catch (IOException e) {
            System.err.println("Error saving vehicles: " + e.getMessage());
        }
    }

    // ------------ Customers ------------
    private void loadCustomers() {
        Path path = Paths.get("customers.csv");
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String nic = parts[0];
                    String name = parts[1];
                    String contact = parts[2];
                    String email = parts[3];

                    // Directly add to memory (DO NOT call registerCustomer here)
                    EcoRide_Customer c = new EcoRide_Customer(nic, name, contact, email);
                    customers.add(c);
                    customerMap.put(nic, c);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    private void saveCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("customers.csv"))) {
            for (EcoRide_Customer c : customers) {
                writer.println(c.getNicOrPassport() + "," + c.getName() + "," + c.getContactNumber() + "," + c.getEmail());
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    // ------------ Bookings ------------
    // Try multiple common formats; default to ISO-8601 if possible.
    private LocalDate parseDate(String s) {
        if (s == null || s.isBlank()) return null;
        String[] patterns = {
            "yyyy-MM-dd",     // ISO (recommended)
            "dd/MM/yyyy",
            "MM/dd/yyyy"
        };
        for (String p : patterns) {
            try {
                return LocalDate.parse(s, DateTimeFormatter.ofPattern(p));
            } catch (DateTimeParseException ignored) {}
        }
        // last attempt: LocalDate.parse default (works for ISO)
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            System.err.println("Could not parse date: " + s);
            return null;
        }
    }

    private void loadBookings() {
        Path path = Paths.get("bookings.csv");
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String bookingId   = parts[0];
                    String customerNic = parts[1];
                    String vehicleId   = parts[2];
                    String startDateStr= parts[3];
                    String endDateStr  = parts[4];
                    int totalKm        = Integer.parseInt(parts[5]);

                    EcoRide_Customer customer = customerMap.get(customerNic);
                    EcoRide_Vehicle vehicle   = vehicleMap.get(vehicleId);
                    if (customer != null && vehicle != null) {
                        LocalDate startDate = parseDate(startDateStr);
                        LocalDate endDate   = parseDate(endDateStr);
                        if (startDate != null && endDate != null) {
                            // Use the constructor that expects LocalDate
                            EcoRide_Booking b = new EcoRide_Booking(
                                bookingId, customer, vehicle, startDate, endDate, totalKm
                            );
                            bookings.add(b);
                            bookingMap.put(bookingId, b);
                            // reflect reserved state
                            vehicle.setAvailabilityStatus("Reserved");
                        } else {
                            System.err.println("Skipping booking " + bookingId + " due to bad dates.");
                        }
                    } else {
                        System.err.println("Skipping booking " + bookingId + " (missing customer/vehicle).");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
    }

    private void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("bookings.csv"))) {
            for (EcoRide_Booking b : bookings) {
                // Assuming getters return LocalDate; toString() => ISO-8601 (yyyy-MM-dd)
                writer.println(
                    b.getBookingId() + "," +
                    b.getCustomer().getNicOrPassport() + "," +
                    b.getVehicle().getCarId() + "," +
                    b.getStartDate() + "," +
                    b.getEndDate() + "," +
                    b.getTotalKm()
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    // ------------ CRUD for Vehicles ------------
    public void addVehicle(EcoRide_Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicleMap.put(vehicle.getCarId(), vehicle);
        if (!isLoadingData) saveData(); // Persist after add, unless loading
    }

    public EcoRide_Vehicle getVehicle(String carId) {
        return vehicleMap.get(carId);
    }

    public boolean updateVehicle(String carId, EcoRide_Vehicle updatedVehicle) {
        EcoRide_Vehicle existing = vehicleMap.get(carId);
        if (existing != null) {
            vehicles.remove(existing);
            vehicles.add(updatedVehicle);
            vehicleMap.put(carId, updatedVehicle);
            if (!isLoadingData) saveData();
            return true;
        }
        return false;
    }

    public boolean deleteVehicle(String carId) {
        EcoRide_Vehicle vehicle = vehicleMap.remove(carId);
        if (vehicle != null) {
            vehicles.remove(vehicle);
            if (!isLoadingData) saveData();
            return true;
        }
        return false;
    }

    public List<EcoRide_Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    // ------------ Customers ------------
    public void registerCustomer(EcoRide_Customer customer) {
        customers.add(customer);
        customerMap.put(customer.getNicOrPassport(), customer);
        if (!isLoadingData) saveData();
    }

    public EcoRide_Customer getCustomer(String nicOrPassport) {
        return customerMap.get(nicOrPassport);
    }

    public boolean updateCustomer(String nicOrPassport, EcoRide_Customer updatedCustomer) {
        EcoRide_Customer existing = customerMap.get(nicOrPassport);
        if (existing != null) {
            customers.remove(existing);
            customers.add(updatedCustomer);
            customerMap.put(nicOrPassport, updatedCustomer);
            if (!isLoadingData) saveData();
            return true;
        }
        return false;
    }

    public boolean deleteCustomer(String nicOrPassport) {
        EcoRide_Customer customer = customerMap.remove(nicOrPassport);
        if (customer != null) {
            customers.remove(customer);
            if (!isLoadingData) saveData();
            return true;
        }
        return false;
    }

    public ArrayList<EcoRide_Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public int getCustomerCount() {
        return customers.size();
    }

    // ------------ Bookings ------------
    public boolean makeBooking(EcoRide_Booking booking) {
        if (booking.isValidBooking()) {
            bookings.add(booking);
            bookingMap.put(booking.getBookingId(), booking);
            booking.getVehicle().setAvailabilityStatus("Reserved");
            if (!isLoadingData) saveData();
            return true;
        }
        return false;
    }

    public EcoRide_Booking getBooking(String bookingId) {
        return bookingMap.get(bookingId);
    }

    public List<EcoRide_Booking> searchBookingsByName(String name) {
        List<EcoRide_Booking> results = new ArrayList<>();
        for (EcoRide_Booking booking : bookings) {
            if (booking.getCustomer().getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(booking);
            }
        }
        return results;
    }

    public EcoRide_Invoice generateInvoice(EcoRide_Booking booking) {
        return new EcoRide_Invoice(booking);
    }

    public ArrayList<EcoRide_Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public int getBookingCount() {
        return bookings.size();
    }

    // Update booking
    public boolean updateBooking(String bookingId, EcoRide_Booking updatedBooking) {
        EcoRide_Booking existing = bookingMap.get(bookingId);
        if (existing != null) {
            bookings.remove(existing);
            bookings.add(updatedBooking);
            bookingMap.put(bookingId, updatedBooking);
            if (!isLoadingData) saveData(); // Persist after update
            return true;
        }
        return false;
    }

    // Delete booking
    public boolean deleteBooking(String bookingId) {
        EcoRide_Booking booking = bookingMap.remove(bookingId);
        if (booking != null) {
            bookings.remove(booking);
            booking.getVehicle().setAvailabilityStatus("Available"); // Free up vehicle
            if (!isLoadingData) saveData(); // Persist after delete
            return true;
        }
        return false;
    }
}
