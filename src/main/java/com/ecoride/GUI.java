package com.ecoride;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GUI extends JFrame {
    private EcoRide_RentalSystem rentalSystem;
    private JTextArea outputArea;

    public GUI() {
        rentalSystem = new EcoRide_RentalSystem();
        initializeSampleData();
        setupGUI();
    }

    private void initializeSampleData() {
        // Add sample vehicles
        rentalSystem.addVehicle(new EcoRide_Vehicle("V001", "Toyota Aqua", "Hybrid", 7500.0, "Available"));
        rentalSystem.addVehicle(new EcoRide_Vehicle("V002", "Nissan Leaf", "Electric", 10000.0, "Available"));
        rentalSystem.addVehicle(new EcoRide_Vehicle("V003", "BMW X5", "Luxury SUV", 15000.0, "Available"));
    }

    private void setupGUI() {
        setTitle("EcoRide Car Rental System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu vehicleMenu = new JMenu("Vehicles");
        JMenu customerMenu = new JMenu("Customers");
        JMenu bookingMenu = new JMenu("Bookings");
        JMenu invoiceMenu = new JMenu("Invoices");

        // Vehicle menu items
        JMenuItem viewVehicles = new JMenuItem("View Vehicles");
        viewVehicles.addActionListener(e -> viewVehicles());
        vehicleMenu.add(viewVehicles);

        JMenuItem addVehicle = new JMenuItem("Add Vehicle");
        addVehicle.addActionListener(e -> addVehicleDialog());
        vehicleMenu.add(addVehicle);

        // Customer menu items
        JMenuItem registerCustomer = new JMenuItem("Register Customer");
        registerCustomer.addActionListener(e -> registerCustomerDialog());
        customerMenu.add(registerCustomer);

        // Booking menu items
        JMenuItem makeBooking = new JMenuItem("Make Booking");
        makeBooking.addActionListener(e -> makeBookingDialog());
        bookingMenu.add(makeBooking);

        JMenuItem searchBookings = new JMenuItem("Search Bookings");
        searchBookings.addActionListener(e -> searchBookingsDialog());
        bookingMenu.add(searchBookings);

        // Invoice menu items
        JMenuItem generateInvoice = new JMenuItem("Generate Invoice");
        generateInvoice.addActionListener(e -> generateInvoiceDialog());
        invoiceMenu.add(generateInvoice);

        menuBar.add(vehicleMenu);
        menuBar.add(customerMenu);
        menuBar.add(bookingMenu);
        menuBar.add(invoiceMenu);
        setJMenuBar(menuBar);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void viewVehicles() {
        StringBuilder sb = new StringBuilder("Available Vehicles:\n");
        for (EcoRide_Vehicle v : rentalSystem.getAllVehicles()) {
            sb.append(v.toString()).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void addVehicleDialog() {
        JDialog dialog = new JDialog(this, "Add Vehicle", true);
        dialog.setLayout(new GridLayout(6, 2));

        JTextField idField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField statusField = new JTextField("Available");

        dialog.add(new JLabel("Car ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Model:"));
        dialog.add(modelField);
        dialog.add(new JLabel("Category:"));
        dialog.add(categoryField);
        dialog.add(new JLabel("Daily Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String model = modelField.getText();
                String category = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                String status = statusField.getText();

                EcoRide_Vehicle vehicle = new EcoRide_Vehicle(id, model, category, price, status);
                rentalSystem.addVehicle(vehicle);
                outputArea.setText("Vehicle added successfully.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid price format.");
            }
        });

        dialog.add(addButton);
        dialog.setSize(300, 200);
        dialog.setVisible(true);
    }

    private void registerCustomerDialog() {
        JDialog dialog = new JDialog(this, "Register Customer", true);
        dialog.setLayout(new GridLayout(5, 2));

        JTextField nicField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField emailField = new JTextField();

        dialog.add(new JLabel("NIC/Passport:"));
        dialog.add(nicField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Contact:"));
        dialog.add(contactField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String nic = nicField.getText();
            String name = nameField.getText();
            String contact = contactField.getText();
            String email = emailField.getText();

            EcoRide_Customer customer = new EcoRide_Customer(nic, name, contact, email);
            rentalSystem.registerCustomer(customer);
            outputArea.setText("Customer registered successfully.");
            dialog.dispose();
        });

        dialog.add(registerButton);
        dialog.setSize(300, 200);
        dialog.setVisible(true);
    }

    private void makeBookingDialog() {
        JDialog dialog = new JDialog(this, "Make Booking", true);
        dialog.setLayout(new GridLayout(7, 2));

        JTextField bookingIdField = new JTextField();
        JTextField customerNicField = new JTextField();
        JTextField vehicleIdField = new JTextField();
        JTextField startDateField = new JTextField("YYYY-MM-DD");
        JTextField endDateField = new JTextField("YYYY-MM-DD");
        JTextField totalKmField = new JTextField();

        dialog.add(new JLabel("Booking ID:"));
        dialog.add(bookingIdField);
        dialog.add(new JLabel("Customer NIC:"));
        dialog.add(customerNicField);
        dialog.add(new JLabel("Vehicle ID:"));
        dialog.add(vehicleIdField);
        dialog.add(new JLabel("Start Date:"));
        dialog.add(startDateField);
        dialog.add(new JLabel("End Date:"));
        dialog.add(endDateField);
        dialog.add(new JLabel("Total KM:"));
        dialog.add(totalKmField);

        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(e -> {
            try {
                String bookingId = bookingIdField.getText();
                String customerNic = customerNicField.getText();
                String vehicleId = vehicleIdField.getText();
                LocalDate startDate = LocalDate.parse(startDateField.getText());
                LocalDate endDate = LocalDate.parse(endDateField.getText());
                int totalKm = Integer.parseInt(totalKmField.getText());

                EcoRide_Customer customer = rentalSystem.getCustomer(customerNic);
                EcoRide_Vehicle vehicle = rentalSystem.getVehicle(vehicleId);

                if (customer == null) {
                    JOptionPane.showMessageDialog(dialog, "Customer not found.");
                    return;
                }
                if (vehicle == null) {
                    JOptionPane.showMessageDialog(dialog, "Vehicle not found.");
                    return;
                }

        EcoRide_Booking booking = new EcoRide_Booking(bookingId, customer, vehicle, startDate, endDate, totalKm);
                if (rentalSystem.makeBooking(booking)) {
                    outputArea.setText("Booking successful.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Booking failed. Check availability or dates.");
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid number format.");
            }
        });

        dialog.add(bookButton);
        dialog.setSize(300, 250);
        dialog.setVisible(true);
    }

    private void searchBookingsDialog() {
        String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
        if (name != null) {
            StringBuilder sb = new StringBuilder("Bookings for " + name + ":\n");
            for (EcoRide_Booking b : rentalSystem.searchBookingsByName(name)) {
                sb.append(b.toString()).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }

    private void generateInvoiceDialog() {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID:");
        if (bookingId != null) {
            EcoRide_Booking booking = rentalSystem.getBooking(bookingId);
            if (booking != null) {
                EcoRide_Invoice invoice = rentalSystem.generateInvoice(booking);
                outputArea.setText(invoice.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Booking not found.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}
