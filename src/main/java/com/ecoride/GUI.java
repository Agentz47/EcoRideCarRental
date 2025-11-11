package com.ecoride;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GUI extends JFrame {
    private EcoRide_RentalSystem rentalSystem;
    private JTextArea outputArea;
    private JTabbedPane tabbedPane;

    public GUI() {
        rentalSystem = new EcoRide_RentalSystem();
        setupModernGUI();
    }

    private void setupModernGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Fallback to default
        }

        setTitle("EcoRide Car Rental System - Modern Interface");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34)); // Green color
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        JLabel titleLabel = new JLabel("EcoRide Car Rental System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane for main content
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Dashboard Tab
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("Dashboard", dashboardPanel);

        // Vehicles Tab
        JPanel vehiclesPanel = createVehiclesPanel();
        tabbedPane.addTab("Vehicles", vehiclesPanel);

        // Customers Tab
        JPanel customersPanel = createCustomersPanel();
        tabbedPane.addTab("Customers", customersPanel);

        // Bookings Tab
        JPanel bookingsPanel = createBookingsPanel();
        tabbedPane.addTab("Bookings", bookingsPanel);

        // Invoices Tab
        JPanel invoicesPanel = createInvoicesPanel();
        tabbedPane.addTab("Invoices", invoicesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setPreferredSize(new Dimension(1000, 30));
        JLabel footerLabel = new JLabel("© 2023 EcoRide - Sustainable Car Rentals", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245));

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        statsPanel.setBorder(new TitledBorder("System Statistics"));
        statsPanel.setBackground(Color.WHITE);

        JLabel vehicleCount = new JLabel("Total Vehicles: " + rentalSystem.getAllVehicles().size());
        vehicleCount.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(vehicleCount);

        JLabel customerCount = new JLabel("Total Customers: " + rentalSystem.getCustomerCount());
        customerCount.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(customerCount);

        JLabel bookingCount = new JLabel("Total Bookings: " + rentalSystem.getBookingCount());
        bookingCount.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(bookingCount);

        JLabel availableVehicles = new JLabel("Available Vehicles: " +
            rentalSystem.getAllVehicles().stream().filter(v -> "Available".equals(v.getAvailabilityStatus())).count());
        availableVehicles.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(availableVehicles);

        panel.add(statsPanel);

        // Welcome Message
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBorder(new TitledBorder("Welcome"));
        welcomePanel.setBackground(Color.WHITE);
        JTextArea welcomeText = new JTextArea("Welcome to EcoRide Car Rental System!\n\n" +
            "Manage your eco-friendly vehicle rentals efficiently.\n" +
            "Use the tabs above to navigate through different sections.");
        welcomeText.setEditable(false);
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeText.setBackground(Color.WHITE);
        welcomePanel.add(welcomeText);
        panel.add(welcomePanel);

        // Quick Actions
        JPanel actionsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        actionsPanel.setBorder(new TitledBorder("Quick Actions"));
        actionsPanel.setBackground(Color.WHITE);

        JButton viewVehiclesBtn = new JButton("View All Vehicles");
        viewVehiclesBtn.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        actionsPanel.add(viewVehiclesBtn);

        JButton addCustomerBtn = new JButton("Register New Customer");
        addCustomerBtn.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        actionsPanel.add(addCustomerBtn);

        JButton makeBookingBtn = new JButton("Make a Booking");
        makeBookingBtn.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        actionsPanel.add(makeBookingBtn);

        panel.add(actionsPanel);

        // Output Area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(new TitledBorder("System Messages"));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel createVehiclesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton viewBtn = new JButton("View Vehicles");
        JButton addBtn = new JButton("Add Vehicle");
        JButton updateBtn = new JButton("Update Vehicle");
        JButton deleteBtn = new JButton("Delete Vehicle");
        buttonsPanel.add(viewBtn);
        buttonsPanel.add(addBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(deleteBtn);

        panel.add(buttonsPanel, BorderLayout.NORTH);

        // Output Area
        JTextArea vehiclesArea = new JTextArea();
        vehiclesArea.setEditable(false);
        vehiclesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(vehiclesArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Set listeners with the specific area
        viewBtn.addActionListener(e -> viewVehicles(vehiclesArea));
        addBtn.addActionListener(e -> addVehicleDialog(vehiclesArea));
        updateBtn.addActionListener(e -> updateVehicleDialog(vehiclesArea));
        deleteBtn.addActionListener(e -> deleteVehicleDialog(vehiclesArea));

        return panel;
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton registerBtn = new JButton("Register Customer");
        JButton viewBtn = new JButton("View Customers");
        JButton updateBtn = new JButton("Update Customer");
        JButton deleteBtn = new JButton("Delete Customer");
        buttonsPanel.add(registerBtn);
        buttonsPanel.add(viewBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(deleteBtn);
        panel.add(buttonsPanel, BorderLayout.NORTH);

        JTextArea customersArea = new JTextArea();
        customersArea.setEditable(false);
        customersArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(customersArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> registerCustomerDialog(customersArea));
        viewBtn.addActionListener(e -> viewCustomers(customersArea));
        updateBtn.addActionListener(e -> updateCustomerDialog(customersArea));
        deleteBtn.addActionListener(e -> deleteCustomerDialog(customersArea));

        return panel;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JButton makeBookingBtn = new JButton("Make Booking");
        JButton searchBtn = new JButton("Search Bookings");
        JButton viewBtn = new JButton("View Bookings");
        JButton updateBtn = new JButton("Update Booking");
        JButton deleteBtn = new JButton("Delete Booking");
        buttonsPanel.add(makeBookingBtn);
        buttonsPanel.add(searchBtn);
        buttonsPanel.add(viewBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(deleteBtn);

        panel.add(buttonsPanel, BorderLayout.NORTH);

        JTextArea bookingsArea = new JTextArea();
        bookingsArea.setEditable(false);
        bookingsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(bookingsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        makeBookingBtn.addActionListener(e -> makeBookingDialog(bookingsArea));
        searchBtn.addActionListener(e -> searchBookingsDialog(bookingsArea));
        viewBtn.addActionListener(e -> viewBookings(bookingsArea));
        updateBtn.addActionListener(e -> updateBookingDialog(bookingsArea));
        deleteBtn.addActionListener(e -> deleteBookingDialog(bookingsArea));

        return panel;
    }

    private JPanel createInvoicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton generateBtn = new JButton("Generate Invoice");
        generateBtn.addActionListener(e -> generateInvoiceDialog());
        panel.add(generateBtn, BorderLayout.NORTH);

        JTextArea invoicesArea = new JTextArea();
        invoicesArea.setEditable(false);
        invoicesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(invoicesArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        outputArea = invoicesArea;

        return panel;
    }

    private void viewVehicles(JTextArea outputArea) {
        StringBuilder sb = new StringBuilder("Available Vehicles:\n");
        for (EcoRide_Vehicle v : rentalSystem.getAllVehicles()) {
            sb.append(v.toString()).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void addVehicleDialog(JTextArea outputArea) {
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

    private void registerCustomerDialog(JTextArea outputArea) {
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

    private void makeBookingDialog(JTextArea outputArea) {
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

    private void searchBookingsDialog(JTextArea outputArea) {
        String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
        if (name != null) {
            StringBuilder sb = new StringBuilder("Bookings for " + name + ":\n");
            for (EcoRide_Booking b : rentalSystem.searchBookingsByName(name)) {
                sb.append(b.toString()).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }

    private void viewCustomers(JTextArea outputArea) {
        StringBuilder sb = new StringBuilder("Registered Customers:\n");
        for (EcoRide_Customer c : rentalSystem.getAllCustomers()) {
            sb.append(c.toString()).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void viewBookings(JTextArea outputArea) {
        StringBuilder sb = new StringBuilder("All Bookings:\n");
        for (EcoRide_Booking b : rentalSystem.getAllBookings()) {
            sb.append(b.toString()).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void updateVehicleDialog(JTextArea outputArea) {
        String carId = JOptionPane.showInputDialog(this, "Enter Vehicle ID to update:");
        if (carId != null) {
            EcoRide_Vehicle existing = rentalSystem.getVehicle(carId);
            if (existing != null) {
                JDialog dialog = new JDialog(this, "Update Vehicle", true);
                dialog.setLayout(new GridLayout(6, 2));

                JTextField modelField = new JTextField(existing.getModel());
                JTextField categoryField = new JTextField(existing.getCategory());
                JTextField priceField = new JTextField(String.valueOf(existing.getDailyRentalPrice()));
                JTextField statusField = new JTextField(existing.getAvailabilityStatus());

                dialog.add(new JLabel("Model:"));
                dialog.add(modelField);
                dialog.add(new JLabel("Category:"));
                dialog.add(categoryField);
                dialog.add(new JLabel("Daily Price:"));
                dialog.add(priceField);
                dialog.add(new JLabel("Status:"));
                dialog.add(statusField);

                JButton updateButton = new JButton("Update");
                updateButton.addActionListener(e -> {
                    try {
                        String model = modelField.getText();
                        String category = categoryField.getText();
                        double price = Double.parseDouble(priceField.getText());
                        String status = statusField.getText();

                        EcoRide_Vehicle updated = new EcoRide_Vehicle(carId, model, category, price, status);
                        if (rentalSystem.updateVehicle(carId, updated)) {
                            outputArea.setText("Vehicle updated successfully.");
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Update failed.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Invalid price format.");
                    }
                });

                dialog.add(updateButton);
                dialog.setSize(300, 200);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found.");
            }
        }
    }

    private void deleteVehicleDialog(JTextArea outputArea) {
        String carId = JOptionPane.showInputDialog(this, "Enter Vehicle ID to delete:");
        if (carId != null) {
            if (rentalSystem.deleteVehicle(carId)) {
                outputArea.setText("Vehicle deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found or delete failed.");
            }
        }
    }

    private void updateCustomerDialog(JTextArea outputArea) {
        String nic = JOptionPane.showInputDialog(this, "Enter Customer NIC to update:");
        if (nic != null) {
            EcoRide_Customer existing = rentalSystem.getCustomer(nic);
            if (existing != null) {
                JDialog dialog = new JDialog(this, "Update Customer", true);
                dialog.setLayout(new GridLayout(5, 2));

                JTextField nameField = new JTextField(existing.getName());
                JTextField contactField = new JTextField(existing.getContactNumber());
                JTextField emailField = new JTextField(existing.getEmail());

                dialog.add(new JLabel("Name:"));
                dialog.add(nameField);
                dialog.add(new JLabel("Contact:"));
                dialog.add(contactField);
                dialog.add(new JLabel("Email:"));
                dialog.add(emailField);

                JButton updateButton = new JButton("Update");
                updateButton.addActionListener(e -> {
                    String name = nameField.getText();
                    String contact = contactField.getText();
                    String email = emailField.getText();

                    EcoRide_Customer updated = new EcoRide_Customer(nic, name, contact, email);
                    if (rentalSystem.updateCustomer(nic, updated)) {
                        outputArea.setText("Customer updated successfully.");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Update failed.");
                    }
                });

                dialog.add(updateButton);
                dialog.setSize(300, 200);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found.");
            }
        }
    }

    private void deleteCustomerDialog(JTextArea outputArea) {
        String nic = JOptionPane.showInputDialog(this, "Enter Customer NIC to delete:");
        if (nic != null) {
            if (rentalSystem.deleteCustomer(nic)) {
                outputArea.setText("Customer deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found or delete failed.");
            }
        }
    }

    private void updateBookingDialog(JTextArea outputArea) {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to update:");
        if (bookingId != null) {
            EcoRide_Booking existing = rentalSystem.getBooking(bookingId);
            if (existing != null) {
                JDialog dialog = new JDialog(this, "Update Booking", true);
                dialog.setLayout(new GridLayout(7, 2));

                JTextField customerNicField = new JTextField(existing.getCustomer().getNicOrPassport());
                JTextField vehicleIdField = new JTextField(existing.getVehicle().getCarId());
                JTextField startDateField = new JTextField(existing.getStartDate().toString());
                JTextField endDateField = new JTextField(existing.getEndDate().toString());
                JTextField totalKmField = new JTextField(String.valueOf(existing.getTotalKm()));

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

                JButton updateButton = new JButton("Update");
                updateButton.addActionListener(e -> {
                    try {
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

                        EcoRide_Booking updated = new EcoRide_Booking(bookingId, customer, vehicle, startDate, endDate, totalKm);
                        if (rentalSystem.updateBooking(bookingId, updated)) {
                            outputArea.setText("Booking updated successfully.");
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Update failed.");
                        }
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Invalid number format.");
                    }
                });

                dialog.add(updateButton);
                dialog.setSize(300, 250);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Booking not found.");
            }
        }
    }

    private void deleteBookingDialog(JTextArea outputArea) {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to delete:");
        if (bookingId != null) {
            if (rentalSystem.deleteBooking(bookingId)) {
                outputArea.setText("Booking deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Booking not found or delete failed.");
            }
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
