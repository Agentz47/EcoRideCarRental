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
public class K2530341RentalSystem {
    private ArrayList<K2530341Vehicle> vehicles;
    private HashMap<String, K2530341Vehicle> vehicleMap; // Key: carId
    private ArrayList<K2530341Customer> customers;
    private HashMap<String, K2530341Customer> customerMap; // Key: nicOrPassport
    private ArrayList<K2530341Booking> bookings;
    private HashMap<String, K2530341Booking> bookingMap; // Key: bookingId
    private K2530341AuthSystem authSystem;

    /** Prevents saveData() from running during load. */
    private boolean isLoadingData = false;

    public K2530341RentalSystem() {
        vehicles = new ArrayList<>();
        vehicleMap = new HashMap<>();
        customers = new ArrayList<>();
        customerMap = new HashMap<>();
        bookings = new ArrayList<>();
        bookingMap = new HashMap<>();
        authSystem = new K2530341AuthSystem();
        loadData(); // Load data from files
        if (vehicles.isEmpty()) {
            initializeVehicles(); // Add some sample vehicles if no data loaded
        }
    }

    // Initialize with sample vehicles
    private void initializeVehicles() {
        addVehicle(new K2530341Vehicle("V001", "Toyota Aqua", "Hybrid", 7500.0, "Available"));
        addVehicle(new K2530341Vehicle("V002", "Nissan Leaf", "Electric", 10000.0, "Available"));
        addVehicle(new K2530341Vehicle("V003", "BMW X5", "Luxury SUV", 15000.0, "Available"));
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
                    K2530341Vehicle v = new K2530341Vehicle(id, model, category, price, status);
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
            for (K2530341Vehicle v : vehicles) {
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
                    K2530341Customer c = new K2530341Customer(nic, name, contact, email);
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
            for (K2530341Customer c : customers) {
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

                    K2530341Customer customer = customerMap.get(customerNic);
                    K2530341Vehicle vehicle   = vehicleMap.get(vehicleId);
                    if (customer != null && vehicle != null) {
                        LocalDate startDate = parseDate(startDateStr);
                        LocalDate endDate   = parseDate(endDateStr);
                        if (startDate != null && endDate != null) {
                            // Use the constructor that expects LocalDate
                            K2530341Booking b = new K2530341Booking(
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
            for (K2530341Booking b : bookings) {
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
    public void addVehicle(K2530341Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicleMap.put(vehicle.getCarId(), vehicle);
        if (!isLoadingData) saveData(); // Persist after add, unless loading
    }

    public K2530341Vehicle getVehicle(String carId) {
        return vehicleMap.get(carId);
    }

    public boolean updateVehicle(String carId, K2530341Vehicle updatedVehicle) {
        K2530341Vehicle existing = vehicleMap.get(carId);
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
        K2530341Vehicle vehicle = vehicleMap.remove(carId);
        if (vehicle != null) {
            vehicles.remove(vehicle);
            if (!isLoadingData) saveData();
            return true;
        }
        return false;
    }

    public List<K2530341Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    // ------------ Customers ------------
    public void registerCustomer(K2530341Customer customer) {
        customers.add(customer);
        customerMap.put(customer.getNicOrPassport(), customer);
        if (!isLoadingData) saveData();
    }

    public K2530341Customer getCustomer(String nicOrPassport) {
        return customerMap.get(nicOrPassport);
    }

    public boolean updateCustomer(String nicOrPassport, K2530341Customer updatedCustomer) {
        K2530341Customer existing = customerMap.get(nicOrPassport);
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
        K2530341Customer customer = customerMap.remove(nicOrPassport);
        if (customer != null) {
            customers.remove(customer);
            if (!isLoadingData) saveData();
            return true;
        }
        return false;
    }

    public ArrayList<K2530341Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public int getCustomerCount() {
        return customers.size();
    }

    // ------------ Bookings ------------
    public boolean makeBooking(K2530341Booking booking) {
        if (booking.isValidBooking()) {
            bookings.add(booking);
            bookingMap.put(booking.getBookingId(), booking);
            booking.getVehicle().setAvailabilityStatus("Reserved");
            if (!isLoadingData) saveData();
            return true;
        }
        return false;
    }

    public K2530341Booking getBooking(String bookingId) {
        return bookingMap.get(bookingId);
    }

    public List<K2530341Booking> searchBookingsByName(String name) {
        List<K2530341Booking> results = new ArrayList<>();
        for (K2530341Booking booking : bookings) {
            if (booking.getCustomer().getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(booking);
            }
        }
        return results;
    }

    public List<K2530341Booking> searchBookingsByDate(LocalDate date) {
        List<K2530341Booking> results = new ArrayList<>();
        for (K2530341Booking booking : bookings) {
            if ((booking.getStartDate().isBefore(date) || booking.getStartDate().isEqual(date)) &&
                (booking.getEndDate().isAfter(date) || booking.getEndDate().isEqual(date))) {
                results.add(booking);
            }
        }
        return results;
    }

    public K2530341Invoice generateInvoice(K2530341Booking booking) {
        return new K2530341Invoice(booking);
    }

    public ArrayList<K2530341Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public int getBookingCount() {
        return bookings.size();
    }

    // Update booking
    public boolean updateBooking(String bookingId, K2530341Booking updatedBooking) {
        K2530341Booking existing = bookingMap.get(bookingId);
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
        K2530341Booking booking = bookingMap.remove(bookingId);
        if (booking != null) {
            bookings.remove(booking);
            booking.getVehicle().setAvailabilityStatus("Available"); // Free up vehicle
            if (!isLoadingData) saveData(); // Persist after delete
            return true;
        }
        return false;
    }

    // ------------ Authentication ------------
    public K2530341AuthSystem getAuthSystem() {
        return authSystem;
    }

    public boolean registerUser(String username, String password, String role, String employeeId) {
        return authSystem.registerUser(username, password, role, employeeId);
    }

    public boolean registerCustomer(String username, String password, String nic, String name, String contact, String email) {
        if (authSystem.registerCustomer(username, password, nic, name, contact, email)) {
            // Also create customer record
            K2530341Customer customer = new K2530341Customer(nic, name, contact, email);
            registerCustomer(customer);
            return true;
        }
        return false;
    }

    public K2530341User loginUser(String username, String password) {
        return authSystem.loginUser(username, password);
    }

    public void logout() {
        authSystem.logout();
    }

    public K2530341User getCurrentUser() {
        return authSystem.getCurrentUser();
    }

    // Get bookings for current user (customer only)
    public List<K2530341Booking> getMyBookings() {
        K2530341User user = getCurrentUser();
        if (user == null || user.isAdmin()) return new ArrayList<>();
        List<K2530341Booking> myBookings = new ArrayList<>();
        for (K2530341Booking b : bookings) {
            if (b.getCustomer().getNicOrPassport().equals(user.getNicOrPassport())) {
                myBookings.add(b);
            }
        }
        return myBookings;
    }
}
