package com.ecoride;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class K2530341GUI extends JFrame {
    private K2530341RentalSystem rentalSystem;
    private JTextArea outputArea;
    private JTabbedPane tabbedPane;

    // ---------- Modern, accessible color palette ----------
    private static class Palette {
        // Primary colors
        static final Color PRIMARY = new Color(16, 185, 129);      // Emerald green (eco-friendly)
        static final Color PRIMARY_DARK = new Color(5, 150, 105);  // Darker emerald
        static final Color SECONDARY = new Color(59, 130, 246);    // Blue
        
        // Semantic colors
        static final Color SUCCESS = new Color(34, 197, 94);       // Green
        static final Color WARNING = new Color(251, 146, 60);      // Orange
        static final Color DANGER = new Color(239, 68, 68);        // Red
        static final Color INFO = new Color(96, 165, 250);         // Light blue
        
        // Background colors
        static final Color BG_MAIN = new Color(15, 23, 42);        // Dark slate
        static final Color BG_CARD = new Color(30, 41, 59);        // Slate card
        static final Color BG_LIGHT = new Color(51, 65, 85);       // Lighter slate
        
        // Text colors
        static final Color TEXT_PRIMARY = new Color(248, 250, 252); // Almost white
        static final Color TEXT_SECONDARY = new Color(203, 213, 225); // Light gray
        static final Color TEXT_MUTED = new Color(148, 163, 184);  // Gray
        static final Color TEXT_ACCENT = new Color(52, 211, 153);  // Bright green
        
        // Border & divider
        static final Color BORDER = new Color(71, 85, 105);        // Slate border
        static final Color DIVIDER = new Color(51, 65, 85);        // Subtle divider
        
        // Button colors
        static final Color BTN_NEUTRAL_BG = new Color(71, 85, 105);
        static final Color BTN_NEUTRAL_TEXT = new Color(226, 232, 240);
    }

    public K2530341GUI() {
        rentalSystem = new K2530341RentalSystem();
        setupModernGUI();
    }

    private void selectTabSafe(int idx) {
        if (tabbedPane != null && idx >= 0 && idx < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(idx);
        }
    }

    // ---------- Look & Feel + UI defaults ----------
    private void setupLAF() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
            // Set global UI colors
            UIManager.put("Panel.background", Palette.BG_MAIN);
            UIManager.put("OptionPane.background", Palette.BG_CARD);
            UIManager.put("OptionPane.messageForeground", Palette.TEXT_PRIMARY);
            UIManager.put("TextField.background", Palette.BG_LIGHT);
            UIManager.put("TextField.foreground", Palette.TEXT_PRIMARY);
            UIManager.put("TextField.caretForeground", Palette.TEXT_PRIMARY);
            UIManager.put("TextArea.background", Palette.BG_LIGHT);
            UIManager.put("TextArea.foreground", Palette.TEXT_PRIMARY);
            UIManager.put("Label.foreground", Palette.TEXT_PRIMARY);
            
            // Fonts
            Font baseFont = new Font("Segoe UI", Font.PLAIN, 14);
            UIManager.put("Button.font", baseFont);
            UIManager.put("Label.font", baseFont);
            UIManager.put("TextField.font", baseFont);
            UIManager.put("TextArea.font", baseFont);
            UIManager.put("TabbedPane.font", baseFont);

            // --- Modern TabbedPane Styling ---
            
            // 1. Color of the selected tab's background
            UIManager.put("TabbedPane.selected", Palette.PRIMARY);
            
            // 2. Color of the selected tab's text
            UIManager.put("TabbedPane.selectedForeground", Color.WHITE);

            // 3. Color of unselected tabs (consistency)
            UIManager.put("TabbedPane.background", Palette.BG_CARD); 
            
            // 4. Color of unselected tab text (consistency)
            UIManager.put("TabbedPane.foreground", Palette.TEXT_SECONDARY); 

            // 5. Remove the 3D borders from the default Metal L&F
            //    Set shadow/highlight colors to the background to make them disappear
            UIManager.put("TabbedPane.shadow", Palette.BG_CARD);
            UIManager.put("TabbedPane.darkShadow", Palette.BG_CARD);
            UIManager.put("TabbedPane.light", Palette.BG_CARD);
            UIManager.put("TabbedPane.highlight", Palette.BG_CARD);
            
            // 6. Remove the border around the content panel
            UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
            
        } catch (Exception ignored) { }
    }

    private void setupModernGUI() {
        setupLAF();

        setTitle("EcoRide Car Rental System - Modern Interface");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Palette.BG_MAIN);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Palette.BG_CARD);
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        headerPanel.setBorder(new MatteBorder(0, 0, 2, 0, Palette.PRIMARY));
        
        JLabel titleLabel = new JLabel("EcoRide Car Rental System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Palette.TEXT_ACCENT);
        titleLabel.setBorder(new EmptyBorder(0, 30, 0, 0));
        
        JLabel subtitleLabel = new JLabel("Sustainable Transportation Solutions");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Palette.TEXT_SECONDARY);
        subtitleLabel.setBorder(new EmptyBorder(0, 30, 0, 0));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // tabbedPane.setBackground(Palette.BG_CARD);      // <-- REMOVED (Handled by UIManager)
        // tabbedPane.setForeground(Palette.TEXT_PRIMARY); // <-- REMOVED (Handled by UIManager)

        JPanel dashboardPanel = createDashboardPanel();
        JPanel vehiclesPanel = createVehiclesPanel();
        JPanel customersPanel = createCustomersPanel();
        JPanel bookingsPanel = createBookingsPanel();
        JPanel invoicesPanel = createInvoicesPanel();

        tabbedPane.addTab("Dashboard", dashboardPanel);
        tabbedPane.addTab("Vehicles", vehiclesPanel);
        tabbedPane.addTab("Customers", customersPanel);
        tabbedPane.addTab("Bookings", bookingsPanel);
        tabbedPane.addTab("Invoices", invoicesPanel);

        // Keyboard shortcuts
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_D);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_V);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_C);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_B);
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_I);

        InputMap im = tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = tabbedPane.getActionMap();
        for (int i = 0; i < 5; i++) {
            final int idx = i;
            im.put(KeyStroke.getKeyStroke("control " + (i + 1)), "goTab" + i);
            am.put("goTab" + i, new AbstractAction() {
                @Override public void actionPerformed(ActionEvent e) { selectTabSafe(idx); }
            });
        }

        add(tabbedPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Palette.BG_CARD);
        footerPanel.setPreferredSize(new Dimension(1200, 35));
        footerPanel.setBorder(new MatteBorder(2, 0, 0, 0, Palette.PRIMARY));
        JLabel footerLabel = new JLabel("© 2024 EcoRide - Sustainable Car Rentals");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Palette.TEXT_MUTED);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ---------- Reusable UI helpers ----------
    private JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Palette.BG_CARD);
        p.setBorder(new CompoundBorder(
                new LineBorder(Palette.BORDER, 1),
                new EmptyBorder(20, 20, 20, 20)));
        if (title != null && !title.isBlank()) {
            JLabel t = new JLabel(title);
            t.setFont(new Font("Segoe UI", Font.BOLD, 18));
            t.setForeground(Palette.TEXT_PRIMARY);
            t.setBorder(new EmptyBorder(0, 0, 15, 0));
            p.add(t, BorderLayout.NORTH);
        }
        return p;
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, Palette.PRIMARY, Color.WHITE);
        return b;
    }
    
    private JButton dangerButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, Palette.DANGER, Color.WHITE);
        return b;
    }
    
    private JButton neutralButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, Palette.BTN_NEUTRAL_BG, Palette.BTN_NEUTRAL_TEXT);
        return b;
    }
    
    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(12, 24, 12, 24));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        
        final Color originalBg = bg;
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { 
                b.setBackground(brighten(originalBg, 1.15f)); 
            }
            @Override public void mouseExited(MouseEvent e) { 
                b.setBackground(originalBg); 
            }
        });
    }
    
    private static Color brighten(Color c, float factor) {
        int r = Math.min(255, Math.round(c.getRed() * factor));
        int g = Math.min(255, Math.round(c.getGreen() * factor));
        int b = Math.min(255, Math.round(c.getBlue() * factor));
        return new Color(r, g, b);
    }
    
    private JScrollPane niceScroll(JComponent comp) {
        JScrollPane sp = new JScrollPane(comp);
        sp.setBorder(new LineBorder(Palette.BORDER, 1));
        sp.getViewport().setBackground(Palette.BG_LIGHT);
        return sp;
    }

    // ---------- Panels ----------
    private JPanel createDashboardPanel() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Palette.BG_MAIN);
        grid.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 1; gc.weighty = 0.6;
        gc.fill = GridBagConstraints.BOTH;

        // Stats card
        JPanel stats = card("System Overview");
        stats.add(buildStatsGrid(), BorderLayout.CENTER);
        grid.add(stats, gc);

        // Welcome card
        gc.gridx = 1;
        JPanel welcome = card("Welcome to EcoRide");
        JTextArea welcomeText = new JTextArea(
                "Manage your eco-friendly vehicle rentals efficiently.\n\n" +
                "Quick Navigation:\n" +
                "   Ctrl+1 - Dashboard\n" +
                "   Ctrl+2 - Vehicles\n" +
                "   Ctrl+3 - Customers\n" +
                "   Ctrl+4 - Bookings\n" +
                "   Ctrl+5 - Invoices\n\n" +
                "Start by adding vehicles and registering customers!");
        welcomeText.setEditable(false);
        welcomeText.setLineWrap(true);
        welcomeText.setWrapStyleWord(true);
        welcomeText.setBackground(Palette.BG_CARD);
        welcomeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeText.setForeground(Palette.TEXT_SECONDARY);
        welcome.add(welcomeText, BorderLayout.CENTER);
        grid.add(welcome, gc);

        // Quick actions
        gc.gridx = 0; gc.gridy = 1; gc.weighty = 0.4;
        JPanel actions = card("Quick Actions");
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        row.setBackground(Palette.BG_CARD);
        JButton viewVehiclesBtn = neutralButton("View All Vehicles");
        viewVehiclesBtn.addActionListener(e -> selectTabSafe(1));
        JButton addCustomerBtn = primaryButton("Register Customer");
        addCustomerBtn.addActionListener(e -> selectTabSafe(2));
        JButton makeBookingBtn = primaryButton("Make Booking");
        makeBookingBtn.addActionListener(e -> selectTabSafe(3));
        row.add(viewVehiclesBtn); row.add(addCustomerBtn); row.add(makeBookingBtn);
        actions.add(row, BorderLayout.CENTER);
        grid.add(actions, gc);

        // Messages
        gc.gridx = 1;
        JPanel msgs = card("System Messages");
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(Palette.BG_LIGHT);
        outputArea.setForeground(Palette.TEXT_PRIMARY);
        outputArea.setText("System ready. Welcome to EcoRide!");
        msgs.add(niceScroll(outputArea), BorderLayout.CENTER);
        grid.add(msgs, gc);

        return grid;
    }

    private JPanel buildStatsGrid() {
        JPanel stats = new JPanel(new GridLayout(2, 2, 16, 16));
        stats.setBackground(Palette.BG_CARD);
        stats.add(statChip("Total Vehicles", String.valueOf(rentalSystem.getAllVehicles().size()), Palette.INFO));
        stats.add(statChip("Total Customers", String.valueOf(rentalSystem.getCustomerCount()), Palette.PRIMARY));
        stats.add(statChip("Total Bookings", String.valueOf(rentalSystem.getBookingCount()), Palette.WARNING));
        long avail = rentalSystem.getAllVehicles().stream()
                .filter(v -> "Available".equalsIgnoreCase(v.getAvailabilityStatus()))
                .count();
        stats.add(statChip("Available", String.valueOf(avail), Palette.SUCCESS));
        return stats;
    }

    private JPanel statChip(String title, String value, Color accentColor) {
        JPanel chip = new JPanel(new BorderLayout(0, 8));
        chip.setBackground(Palette.BG_LIGHT);
        chip.setBorder(new CompoundBorder(
            new LineBorder(accentColor, 2, true),
            new EmptyBorder(16, 16, 16, 16)));
        
        JLabel t = new JLabel(title);
        t.setForeground(Palette.TEXT_MUTED);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel v = new JLabel(value);
        v.setForeground(accentColor);
        v.setFont(new Font("Segoe UI", Font.BOLD, 32));
        
        chip.add(t, BorderLayout.NORTH);
        chip.add(v, BorderLayout.CENTER);
        return chip;
    }

    private JPanel createVehiclesPanel() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Palette.BG_MAIN);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel controls = card("Vehicle Management");
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        grid.setBackground(Palette.BG_CARD);
        JButton viewBtn = neutralButton("View All");
        JButton addBtn = primaryButton("Add Vehicle");
        JButton updateBtn = neutralButton("Update");
        JButton deleteBtn = dangerButton("Delete");
        grid.add(viewBtn); grid.add(addBtn); grid.add(updateBtn); grid.add(deleteBtn);
        controls.add(grid, BorderLayout.CENTER);

        JTextArea vehiclesArea = new JTextArea();
        vehiclesArea.setEditable(false);
        vehiclesArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        vehiclesArea.setBackground(Palette.BG_LIGHT);
        vehiclesArea.setForeground(Palette.TEXT_PRIMARY);
        JPanel listCard = card("Vehicle List");
        listCard.add(niceScroll(vehiclesArea), BorderLayout.CENTER);

        viewBtn.addActionListener(e -> viewVehicles(vehiclesArea));
        addBtn.addActionListener(e -> addVehicleDialog(vehiclesArea));
        updateBtn.addActionListener(e -> updateVehicleDialog(vehiclesArea));
        deleteBtn.addActionListener(e -> deleteVehicleDialog(vehiclesArea));

        root.add(controls, BorderLayout.NORTH);
        root.add(listCard, BorderLayout.CENTER);
        return root;
    }

    private JPanel createCustomersPanel() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Palette.BG_MAIN);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel controls = card("Customer Management");
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        grid.setBackground(Palette.BG_CARD);
        JButton registerBtn = primaryButton("Register Customer");
        JButton viewBtn = neutralButton("View All");
        JButton updateBtn = neutralButton("Update");
        JButton deleteBtn = dangerButton("Delete");
        grid.add(registerBtn); grid.add(viewBtn); grid.add(updateBtn); grid.add(deleteBtn);
        controls.add(grid, BorderLayout.CENTER);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setBackground(Palette.BG_LIGHT);
        area.setForeground(Palette.TEXT_PRIMARY);
        JPanel listCard = card("Customer List");
        listCard.add(niceScroll(area), BorderLayout.CENTER);

        registerBtn.addActionListener(e -> registerCustomerDialog(area));
        viewBtn.addActionListener(e -> viewCustomers(area));
        updateBtn.addActionListener(e -> updateCustomerDialog(area));
        deleteBtn.addActionListener(e -> deleteCustomerDialog(area));

        root.add(controls, BorderLayout.NORTH);
        root.add(listCard, BorderLayout.CENTER);
        return root;
    }

    private JPanel createBookingsPanel() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Palette.BG_MAIN);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel controls = card("Booking Management");
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        grid.setBackground(Palette.BG_CARD);
        JButton makeBookingBtn = primaryButton("Make Booking");
        JButton searchBtn = neutralButton("Search");
        JButton viewBtn = neutralButton("View All");
        JButton updateBtn = neutralButton("Update");
        JButton deleteBtn = dangerButton("Delete");
        grid.add(makeBookingBtn); grid.add(searchBtn); grid.add(viewBtn); grid.add(updateBtn); grid.add(deleteBtn);
        controls.add(grid, BorderLayout.CENTER);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setBackground(Palette.BG_LIGHT);
        area.setForeground(Palette.TEXT_PRIMARY);
        JPanel listCard = card("Booking List");
        listCard.add(niceScroll(area), BorderLayout.CENTER);

        makeBookingBtn.addActionListener(e -> makeBookingDialog(area));
        searchBtn.addActionListener(e -> searchBookingsDialog(area));
        viewBtn.addActionListener(e -> viewBookings(area));
        updateBtn.addActionListener(e -> updateBookingDialog(area));
        deleteBtn.addActionListener(e -> deleteBookingDialog(area));

        root.add(controls, BorderLayout.NORTH);
        root.add(listCard, BorderLayout.CENTER);
        return root;
    }

    private JPanel createInvoicesPanel() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Palette.BG_MAIN);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel controls = card("Invoice Generation");
        JButton generateBtn = primaryButton("Generate Invoice");
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        row.setBackground(Palette.BG_CARD);
        row.add(generateBtn);
        controls.add(row, BorderLayout.CENTER);

        JTextArea invoicesArea = new JTextArea();
        invoicesArea.setEditable(false);
        invoicesArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        invoicesArea.setBackground(Palette.BG_LIGHT);
        invoicesArea.setForeground(Palette.TEXT_PRIMARY);
        JPanel listCard = card("Invoice Preview");
        listCard.add(niceScroll(invoicesArea), BorderLayout.CENTER);
        outputArea = invoicesArea;

        generateBtn.addActionListener(e -> generateInvoiceDialog());

        root.add(controls, BorderLayout.NORTH);
        root.add(listCard, BorderLayout.CENTER);
        return root;
    }

    // ---------- Data actions ----------
    private void viewVehicles(JTextArea out) {
        StringBuilder sb = new StringBuilder("AVAILABLE VEHICLES\n");
        sb.append("=".repeat(70)).append("\n\n");
        rentalSystem.getAllVehicles().forEach(v -> sb.append(v).append("\n\n"));
        out.setText(sb.toString());
    }

    private void addVehicleDialog(JTextArea out) {
        JDialog d = modal("Add New Vehicle", 500, 380);
        JPanel f = formGrid(5);
        JTextField id = tf(), model = tf(), price = tf();
        JComboBox<String> category = new JComboBox<>(new String[]{"Compact Petrol", "Hybrid", "Electric", "Luxury SUV", "Racing", "Off road SUV", "Super luxury"});
        category.setBackground(Palette.BG_LIGHT);
        category.setForeground(Palette.TEXT_PRIMARY);
        category.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> status = new JComboBox<>(new String[]{"Available", "Reserved", "Under Maintenance"});
        status.setBackground(Palette.BG_LIGHT);
        status.setForeground(Palette.TEXT_PRIMARY);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        f.add(label("Car ID:")); f.add(id);
        f.add(label("Model:")); f.add(model);
        f.add(label("Category:")); f.add(category);
        f.add(label("Daily Price:")); f.add(price);
        f.add(label("Status:")); f.add(status);

        JButton add = primaryButton("Add Vehicle");
        add.addActionListener(e -> {
            String carId = id.getText().trim();
            if (carId.isEmpty()) {
                warn(d, "Car ID cannot be empty.");
                return;
            }
            if (rentalSystem.getVehicle(carId) != null) {
                warn(d, "Vehicle ID already exists. Please choose a unique ID.");
                return;
            }
            try {
                K2530341Vehicle v = new K2530341Vehicle(
                        carId, model.getText().trim(), (String) category.getSelectedItem(),
                        Double.parseDouble(price.getText().trim()), (String) status.getSelectedItem());
                rentalSystem.addVehicle(v);
                out.setText("[SUCCESS] Vehicle added successfully!\n\n" + v);
                d.dispose();
            } catch (NumberFormatException ex) {
                warn(d, "Invalid price. Please enter a valid number.");
            }
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(add), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void registerCustomerDialog(JTextArea out) {
        JDialog d = modal("Register New Customer", 500, 350);
        JPanel f = formGrid(4);
        JTextField nic = tf(), name = tf(), contact = tf(), email = tf();

        f.add(label("NIC/Passport:")); f.add(nic);
        f.add(label("Full Name:")); f.add(name);
        f.add(label("Contact Number:")); f.add(contact);
        f.add(label("Email Address:")); f.add(email);

        JButton add = primaryButton("Register");
        add.addActionListener(e -> {
            String nicValue = nic.getText().trim();
            if (nicValue.isEmpty()) {
                warn(d, "NIC/Passport cannot be empty.");
                return;
            }
            if (rentalSystem.getCustomer(nicValue) != null) {
                warn(d, "Customer NIC already exists. Please choose a unique NIC.");
                return;
            }
            K2530341Customer c = new K2530341Customer(
                    nicValue, name.getText().trim(), contact.getText().trim(), email.getText().trim());
            rentalSystem.registerCustomer(c);
            out.setText("[SUCCESS] Customer registered successfully!\n\n" + c);
            d.dispose();
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(add), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void makeBookingDialog(JTextArea out) {
        JDialog d = modal("Make New Booking", 520, 420);
        JPanel f = formGrid(6);
        JTextField id = tf(), nic = tf(), vid = tf(), start = tf("YYYY-MM-DD"), end = tf("YYYY-MM-DD"), km = tf();

        f.add(label("Booking ID:")); f.add(id);
        f.add(label("Customer NIC:")); f.add(nic);
        f.add(label("Vehicle ID:")); f.add(vid);
        f.add(label("Start Date:")); f.add(start);
        f.add(label("End Date:")); f.add(end);
        f.add(label("Total KM:")); f.add(km);

        JButton book = primaryButton("Create Booking");
        book.addActionListener(e -> {
            String bookingId = id.getText().trim();
            if (bookingId.isEmpty()) {
                warn(d, "Booking ID cannot be empty.");
                return;
            }
            if (rentalSystem.getBooking(bookingId) != null) {
                warn(d, "Booking ID already exists. Please choose a unique ID.");
                return;
            }
            try {
                K2530341Customer c = rentalSystem.getCustomer(nic.getText().trim());
                K2530341Vehicle v = rentalSystem.getVehicle(vid.getText().trim());
                if (c == null) { warn(d, "Customer not found."); return; }
                if (v == null) { warn(d, "Vehicle not found."); return; }

                LocalDate s = LocalDate.parse(start.getText().trim());
                LocalDate en = LocalDate.parse(end.getText().trim());
                int totalKm = Integer.parseInt(km.getText().trim());

                K2530341Booking b = new K2530341Booking(bookingId, c, v, s, en, totalKm);
                if (rentalSystem.makeBooking(b)) {
                    out.setText("[SUCCESS] Booking successful!\n\n" + b);
                    d.dispose();
                } else {
                    warn(d, "Booking failed. Check vehicle availability or dates.");
                }
            } catch (DateTimeParseException ex) {
                warn(d, "Invalid date format. Please use YYYY-MM-DD.");
            } catch (NumberFormatException ex) {
                warn(d, "Invalid KM value. Please enter a whole number.");
            }
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(book), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void searchBookingsDialog(JTextArea out) {
        String input = JOptionPane.showInputDialog(this, "Enter Customer Name or Date (YYYY-MM-DD):");
        if (input != null && !input.trim().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            try {
                LocalDate date = LocalDate.parse(input.trim());
                sb.append("SEARCH RESULTS FOR DATE: ").append(date).append("\n");
                sb.append("=".repeat(70)).append("\n\n");
                for (K2530341Booking b : rentalSystem.searchBookingsByDate(date)) sb.append(b).append("\n\n");
            } catch (DateTimeParseException e) {
                // Assume it's a name search
                sb.append("SEARCH RESULTS FOR NAME: ").append(input).append("\n");
                sb.append("=".repeat(70)).append("\n\n");
                for (K2530341Booking b : rentalSystem.searchBookingsByName(input)) sb.append(b).append("\n\n");
            }
            out.setText(sb.toString());
        }
    }

    private void viewCustomers(JTextArea out) {
        StringBuilder sb = new StringBuilder("REGISTERED CUSTOMERS\n");
        sb.append("=".repeat(70)).append("\n\n");
        rentalSystem.getAllCustomers().forEach(c -> sb.append(c).append("\n\n"));
        out.setText(sb.toString());
    }

    private void viewBookings(JTextArea out) {
        StringBuilder sb = new StringBuilder("ALL BOOKINGS\n");
        sb.append("=".repeat(70)).append("\n\n");
        rentalSystem.getAllBookings().forEach(b -> sb.append(b).append("\n\n"));
        out.setText(sb.toString());
    }

    private void updateVehicleDialog(JTextArea out) {
        String carId = JOptionPane.showInputDialog(this, "Enter Vehicle ID to update:");
        if (carId == null) return;
        K2530341Vehicle existing = rentalSystem.getVehicle(carId);
        if (existing == null) { warn(this, "Vehicle not found."); return; }

        JDialog d = modal("Update Vehicle", 500, 350);
        JPanel f = formGrid(4);
        JTextField model = tf(existing.getModel());
        JTextField category = tf(existing.getCategory());
        JTextField price = tf(String.valueOf(existing.getDailyRentalPrice()));
        JTextField status = tf(existing.getAvailabilityStatus());

        f.add(label("Model:")); f.add(model);
        f.add(label("Category:")); f.add(category);
        f.add(label("Daily Price:")); f.add(price);
        f.add(label("Status:")); f.add(status);

        JButton update = primaryButton("Update Vehicle");
        update.addActionListener(e -> {
            try {
                K2530341Vehicle v = new K2530341Vehicle(
                        carId, model.getText().trim(), category.getText().trim(),
                        Double.parseDouble(price.getText().trim()), status.getText().trim());
                if (rentalSystem.updateVehicle(carId, v)) {
                    out.setText("[SUCCESS] Vehicle updated successfully!\n\n" + v);
                    d.dispose();
                } else {
                    warn(d, "Update failed.");
                }
            } catch (NumberFormatException ex) { warn(d, "Invalid price."); }
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(update), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void deleteVehicleDialog(JTextArea out) {
        String carId = JOptionPane.showInputDialog(this, "Enter Vehicle ID to delete:");
        if (carId == null) return;
        if (rentalSystem.deleteVehicle(carId)) out.setText("[DELETED] Vehicle deleted successfully.");
        else warn(this, "Vehicle not found or deletion failed.");
    }

    private void updateCustomerDialog(JTextArea out) {
        String nic = JOptionPane.showInputDialog(this, "Enter Customer NIC to update:");
        if (nic == null) return;
        K2530341Customer existing = rentalSystem.getCustomer(nic);
        if (existing == null) { warn(this, "Customer not found."); return; }

        JDialog d = modal("Update Customer", 500, 330);
        JPanel f = formGrid(3);
        JTextField name = tf(existing.getName());
        JTextField contact = tf(existing.getContactNumber());
        JTextField email = tf(existing.getEmail());

        f.add(label("Full Name:")); f.add(name);
        f.add(label("Contact Number:")); f.add(contact);
        f.add(label("Email Address:")); f.add(email);

        JButton update = primaryButton("Update Customer");
        update.addActionListener(e -> {
            K2530341Customer c = new K2530341Customer(nic, name.getText().trim(), contact.getText().trim(), email.getText().trim());
            if (rentalSystem.updateCustomer(nic, c)) {
                out.setText("[SUCCESS] Customer updated successfully!\n\n" + c);
                d.dispose();
            } else warn(d, "Update failed.");
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(update), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void deleteCustomerDialog(JTextArea out) {
        String nic = JOptionPane.showInputDialog(this, "Enter Customer NIC to delete:");
        if (nic == null) return;
        if (rentalSystem.deleteCustomer(nic)) out.setText("[DELETED] Customer deleted successfully.");
        else warn(this, "Customer not found or deletion failed.");
    }

    private void updateBookingDialog(JTextArea out) {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to update:");
        if (bookingId == null) return;
        K2530341Booking existing = rentalSystem.getBooking(bookingId);
        if (existing == null) { warn(this, "Booking not found."); return; }

        JDialog d = modal("Update Booking", 520, 400);
        JPanel f = formGrid(5);
        JTextField nic = tf(existing.getCustomer().getNicOrPassport());
        JTextField vid = tf(existing.getVehicle().getCarId());
        JTextField start = tf(existing.getStartDate().toString());
        JTextField end = tf(existing.getEndDate().toString());
        JTextField km = tf(String.valueOf(existing.getTotalKm()));

        f.add(label("Customer NIC:")); f.add(nic);
        f.add(label("Vehicle ID:")); f.add(vid);
        f.add(label("Start Date:")); f.add(start);
        f.add(label("End Date:")); f.add(end);
        f.add(label("Total KM:")); f.add(km);

        JButton update = primaryButton("Update Booking");
        update.addActionListener(e -> {
            try {
                K2530341Customer c = rentalSystem.getCustomer(nic.getText().trim());
                K2530341Vehicle v = rentalSystem.getVehicle(vid.getText().trim());
                if (c == null) { warn(d, "Customer not found."); return; }
                if (v == null) { warn(d, "Vehicle not found."); return; }
                LocalDate s = LocalDate.parse(start.getText().trim());
                LocalDate en = LocalDate.parse(end.getText().trim());
                int totalKm = Integer.parseInt(km.getText().trim());

                K2530341Booking upd = new K2530341Booking(bookingId, c, v, s, en, totalKm);
                if (rentalSystem.updateBooking(bookingId, upd)) {
                    out.setText("[SUCCESS] Booking updated successfully!\n\n" + upd);
                    d.dispose();
                } else warn(d, "Update failed.");
            } catch (DateTimeParseException ex) { warn(d, "Invalid date format. Use YYYY-MM-DD."); }
              catch (NumberFormatException ex) { warn(d, "Invalid KM value."); }
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(update), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void deleteBookingDialog(JTextArea out) {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to delete:");
        if (bookingId == null) return;
        if (rentalSystem.deleteBooking(bookingId)) out.setText("[DELETED] Booking deleted successfully.");
        else warn(this, "Booking not found or deletion failed.");
    }

    private void generateInvoiceDialog() {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID for invoice:");
        if (bookingId == null) return;
        K2530341Booking b = rentalSystem.getBooking(bookingId);
        if (b == null) { warn(this, "Booking not found."); return; }
        K2530341Invoice invoice = rentalSystem.generateInvoice(b);
        outputArea.setText("INVOICE GENERATED\n" + "=".repeat(70) + "\n\n" + invoice.toString());
    }

    // ---------- Small UI utilities ----------
    private JDialog modal(String title, int w, int h) {
        JDialog d = new JDialog(this, title, true);
        d.setLayout(new BorderLayout(16, 16));
        d.getContentPane().setBackground(Palette.BG_CARD);
        d.setSize(w, h);
        d.setLocationRelativeTo(this);
        d.getRootPane().setBorder(new EmptyBorder(20, 20, 20, 20));
        return d;
    }
    
    private JPanel formGrid(int rows) {
        JPanel p = new JPanel(new GridLayout(rows, 2, 12, 12));
        p.setBackground(Palette.BG_CARD);
        return p;
    }
    
    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Palette.TEXT_PRIMARY);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }
    
    private JTextField tf() { return tf(""); }
    
    private JTextField tf(String text) {
        JTextField t = new JTextField(text);
        t.setBackground(Palette.BG_LIGHT);
        t.setForeground(Palette.TEXT_PRIMARY);
        t.setCaretColor(Palette.TEXT_PRIMARY);
        t.setBorder(new CompoundBorder(
            new LineBorder(Palette.BORDER, 1),
            new EmptyBorder(10, 12, 10, 12)));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return t;
    }
    
    private JPanel footerRight(JButton b) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.setBackground(Palette.BG_CARD);
        p.add(b);
        return p;
    }
    
    private void warn(Component parent, String msg) {
        JOptionPane pane = new JOptionPane(msg, JOptionPane.WARNING_MESSAGE);
        pane.setBackground(Palette.BG_CARD);
        pane.setForeground(Palette.TEXT_PRIMARY);
        JDialog dialog = pane.createDialog(parent, "EcoRide");
        dialog.getContentPane().setBackground(Palette.BG_CARD);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(K2530341GUI::new);
    }
}
