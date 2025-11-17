package com.ecoride;

/**
 * Main class to launch the EcoRide Car Rental System GUI.
 */
public class K2530341Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--load-test")) {
            // Run load test instead of GUI
            K2530341RentalSystem system = new K2530341RentalSystem();
            int numCustomers = args.length > 1 ? Integer.parseInt(args[1]) : 1000;
            int numVehicles = args.length > 2 ? Integer.parseInt(args[2]) : 500;
            int numBookings = args.length > 3 ? Integer.parseInt(args[3]) : 2000;
            system.runLoadTest(numCustomers, numVehicles, numBookings);
        } else {
            // Launch the GUI
            javax.swing.SwingUtilities.invokeLater(() -> new K2530341GUI());
        }
    }
}
