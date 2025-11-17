package com.ecoride;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Load Test for EcoRide Car Rental System.
 * Generates synthetic data and measures performance of key operations.
 */
public class K2530341LoadTest {

    private static final Random random = new Random();
    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Sarah", "David", "Emma", "Chris", "Lisa", "Robert", "Anna"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
    private static final String[] VEHICLE_MODELS = {"Toyota Aqua", "Nissan Leaf", "BMW X5", "Honda Civic", "Ford Mustang", "Tesla Model 3", "Audi Q7", "Mercedes C-Class"};
    private static final String[] CATEGORIES = {"Compact Petrol", "Hybrid", "Electric", "Luxury SUV"};
    private static final double[] DAILY_PRICES = {5000.0, 7500.0, 10000.0, 15000.0};

    /**
     * Run the load test with specified number of records.
     */
    public static void runLoadTest(K2530341RentalSystem system, int numCustomers, int numVehicles, int numBookings) {
        System.out.println("=== Starting Load Test ===");
        System.out.println("Generating " + numCustomers + " customers, " + numVehicles + " vehicles, " + numBookings + " bookings");

        long startTime = System.nanoTime();

        // Generate and add customers
        long customerAddStart = System.nanoTime();
        List<K2530341Customer> customers = generateCustomers(numCustomers);
        for (K2530341Customer customer : customers) {
            system.registerCustomer(customer);
        }
        long customerAddTime = System.nanoTime() - customerAddStart;

        // Generate and add vehicles
        long vehicleAddStart = System.nanoTime();
        List<K2530341Vehicle> vehicles = generateVehicles(numVehicles);
        for (K2530341Vehicle vehicle : vehicles) {
            system.addVehicle(vehicle);
        }
        long vehicleAddTime = System.nanoTime() - vehicleAddStart;

        // Generate and add bookings
        long bookingAddStart = System.nanoTime();
        List<K2530341Booking> bookings = generateBookings(numBookings, customers, vehicles);
        for (K2530341Booking booking : bookings) {
            system.makeBooking(booking);
        }
        long bookingAddTime = System.nanoTime() - bookingAddStart;

        long totalAddTime = System.nanoTime() - startTime;

        System.out.println("\n=== Data Generation Results ===");
        System.out.printf("Customer addition time: %.2f ms (avg: %.2f μs per customer)\n",
            customerAddTime / 1_000_000.0, customerAddTime / 1_000.0 / numCustomers);
        System.out.printf("Vehicle addition time: %.2f ms (avg: %.2f μs per vehicle)\n",
            vehicleAddTime / 1_000_000.0, vehicleAddTime / 1_000.0 / numVehicles);
        System.out.printf("Booking addition time: %.2f ms (avg: %.2f μs per booking)\n",
            bookingAddTime / 1_000_000.0, bookingAddTime / 1_000.0 / numBookings);
        System.out.printf("Total addition time: %.2f ms\n", totalAddTime / 1_000_000.0);

        // Test search operations
        testSearchOperations(system, bookings);

        // Test fee calculations
        testFeeCalculations(system, bookings);

        System.out.println("=== Load Test Completed ===");
    }

    private static List<K2530341Customer> generateCustomers(int count) {
        List<K2530341Customer> customers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String name = firstName + " " + lastName;
            String nic = "NIC" + String.format("%06d", i);
            String contact = "07" + String.format("%08d", random.nextInt(100000000));
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
            customers.add(new K2530341Customer(nic, name, contact, email));
        }
        return customers;
    }

    private static List<K2530341Vehicle> generateVehicles(int count) {
        List<K2530341Vehicle> vehicles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String carId = "V" + String.format("%04d", i);
            String model = VEHICLE_MODELS[random.nextInt(VEHICLE_MODELS.length)];
            String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
            double price = DAILY_PRICES[random.nextInt(DAILY_PRICES.length)];
            vehicles.add(new K2530341Vehicle(carId, model, category, price, "Available"));
        }
        return vehicles;
    }

    private static List<K2530341Booking> generateBookings(int count, List<K2530341Customer> customers, List<K2530341Vehicle> vehicles) {
        List<K2530341Booking> bookings = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < count; i++) {
            String bookingId = "B" + String.format("%06d", i);
            K2530341Customer customer = customers.get(random.nextInt(customers.size()));
            K2530341Vehicle vehicle = vehicles.get(random.nextInt(vehicles.size()));

            // Ensure start date is at least 3 days in future
            LocalDate startDate = today.plusDays(3 + random.nextInt(30));
            LocalDate endDate = startDate.plusDays(1 + random.nextInt(14)); // 1-14 days
            int totalKm = 50 + random.nextInt(500); // 50-549 km

            K2530341Booking booking = new K2530341Booking(bookingId, customer, vehicle, startDate, endDate, totalKm);
            // Only add if valid
            if (booking.isValidBooking()) {
                bookings.add(booking);
            }
        }
        return bookings;
    }

    private static void testSearchOperations(K2530341RentalSystem system, List<K2530341Booking> bookings) {
        System.out.println("\n=== Search Operations Test ===");

        // Test search by name
        long searchStart = System.nanoTime();
        List<K2530341Booking> johnBookings = system.searchBookingsByName("John");
        long searchTime = System.nanoTime() - searchStart;

        System.out.printf("Search bookings by name 'John': %.2f ms (found %d bookings)\n",
            searchTime / 1_000_000.0, johnBookings.size());

        // Test search by date (pick a random future date)
        LocalDate testDate = LocalDate.now().plusDays(10);
        searchStart = System.nanoTime();
        List<K2530341Booking> dateBookings = system.searchBookingsByDate(testDate);
        searchTime = System.nanoTime() - searchStart;

        System.out.printf("Search bookings by date %s: %.2f ms (found %d bookings)\n",
            testDate, searchTime / 1_000_000.0, dateBookings.size());
    }

    private static void testFeeCalculations(K2530341RentalSystem system, List<K2530341Booking> bookings) {
        System.out.println("\n=== Fee Calculation Test ===");

        // Test fee calculation for 10 random bookings
        int testCount = Math.min(10, bookings.size());
        long totalFeeTime = 0;

        for (int i = 0; i < testCount; i++) {
            K2530341Booking booking = bookings.get(random.nextInt(bookings.size()));
            long feeStart = System.nanoTime();
            double fee = K2530341FeeCalculator.calculateTotalFee(booking, booking.getTotalKm());
            long feeTime = System.nanoTime() - feeStart;
            totalFeeTime += feeTime;
        }

        System.out.printf("Fee calculation for %d bookings: %.2f ms (avg: %.2f μs per calculation)\n",
            testCount, totalFeeTime / 1_000_000.0, totalFeeTime / 1_000.0 / testCount);
    }
}
