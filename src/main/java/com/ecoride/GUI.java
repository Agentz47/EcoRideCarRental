package com.ecoride;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GUI extends JFrame {
    private EcoRide_RentalSystem rentalSystem;
    private JTextArea outputArea;
    private JTabbedPane tabbedPane;

    // ---------- Accessible, modern palette ----------
    private static class Palette {
        static final Color INDIGO = new Color(63, 81, 181);
        static final Color TEAL = new Color(0, 150, 136);
        static final Color AMBER = new Color(255, 193, 7);
        static final Color SUCCESS = new Color(56, 142, 60);
        static final Color INFO = new Color(2, 136, 209);
        static final Color WARNING = new Color(245, 124, 0);
        static final Color DANGER = new Color(229, 57, 53);
        static final Color BG = new Color(248, 249, 251);
        static final Color CARD = Color.WHITE;
        static final Color TEXT = new Color(33, 33, 33);
        static final Color MUTED = new Color(117, 117, 117);
        static final Color DIVIDER = new Color(224, 224, 224);
    }

    public GUI() {
        rentalSystem = new EcoRide_RentalSystem();
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
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", Palette.BG);
            UIManager.put("info", Palette.CARD);
            UIManager.put("nimbusBase", new Color(70, 89, 189));
            UIManager.put("nimbusBlueGrey", new Color(200, 205, 218));
            UIManager.put("nimbusLightBackground", Palette.CARD);
            UIManager.put("text", Palette.TEXT);
            UIManager.put("Button.font", UIManager.getFont("Button.font").deriveFont(14f));
            UIManager.put("Label.font", UIManager.getFont("Label.font").deriveFont(14f));
            UIManager.put("TextField.font", UIManager.getFont("TextField.font").deriveFont(14f));
            UIManager.put("TabbedPane.font", UIManager.getFont("TabbedPane.font").deriveFont(14f));
            UIManager.put("TitledBorder.font", UIManager.getFont("TitledBorder.font").deriveFont(Font.BOLD, 14f));
        } catch (Exception ignored) { }
    }

    private void setupModernGUI() {
        setupLAF();

        setTitle("EcoRide Car Rental System - Modern Interface");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Palette.BG);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        JLabel titleLabel = new JLabel("EcoRide Car Rental System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

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

        // Mnemonics/tooltips (SAFE here, tabs already added)
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_D); // Alt+D
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_V); // Alt+V
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_C); // Alt+C
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_B); // Alt+B
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_I); // Alt+I

        tabbedPane.setToolTipTextAt(0, "Overview & stats");
        tabbedPane.setToolTipTextAt(1, "Manage vehicles");
        tabbedPane.setToolTipTextAt(2, "Manage customers");
        tabbedPane.setToolTipTextAt(3, "Create and manage bookings");
        tabbedPane.setToolTipTextAt(4, "Generate invoices");

        // Ctrl+1..5 key bindings (also safe)
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
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setPreferredSize(new Dimension(1000, 30));
        JLabel footerLabel = new JLabel("© 2023 EcoRide - Sustainable Car Rentals", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ---------- Reusable UI helpers ----------
    private JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Palette.CARD);
        p.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, Palette.DIVIDER),
                new EmptyBorder(16, 16, 16, 16)));
        if (title != null && !title.isBlank()) {
            JLabel t = new JLabel(title);
            t.setFont(t.getFont().deriveFont(Font.BOLD, 16f));
            t.setForeground(Palette.TEXT);
            t.setBorder(new EmptyBorder(0, 0, 12, 0));
            p.add(t, BorderLayout.NORTH);
        }
        return p;
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, Palette.TEAL, Color.WHITE);
        return b;
    }
    private JButton dangerButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, Palette.DANGER, Color.WHITE);
        return b;
    }
    private JButton neutralButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, new Color(236, 239, 241), Palette.TEXT);
        return b;
    }
    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0, 25)),
                new EmptyBorder(10, 16, 10, 16)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(tint(bg, 1.08f)); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });
    }
    private static Color tint(Color c, float factor) {
        int r = Math.min(255, Math.round(c.getRed() * factor));
        int g = Math.min(255, Math.round(c.getGreen() * factor));
        int b = Math.min(255, Math.round(c.getBlue() * factor));
        return new Color(r, g, b);
    }
    private JScrollPane niceScroll(JComponent comp) {
        JScrollPane sp = new JScrollPane(comp);
        sp.setBorder(new MatteBorder(1, 1, 1, 1, Palette.DIVIDER));
        return sp;
    }

    // ---------- Panels ----------
    private JPanel createDashboardPanel() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(12, 12, 12, 12));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 1; gc.weighty = 0.6;
        gc.fill = GridBagConstraints.BOTH;

        // Stats card
        JPanel stats = card("System Overview");
        stats.add(buildStatsGrid(), BorderLayout.CENTER);
        grid.add(stats, gc);

        // Welcome card
        gc.gridx = 1;
        JPanel welcome = card("Welcome");
        JTextArea welcomeText = new JTextArea(
            "Manage your eco-friendly vehicle rentals efficiently.\n" +
            "Use the tabs above to navigate through sections.\n\n" +
            "Shortcuts: Ctrl+1 Dashboard • Ctrl+2 Vehicles • Ctrl+3 Customers\n" +
            "           Ctrl+4 Bookings  • Ctrl+5 Invoices");
        welcomeText.setEditable(false);
        welcomeText.setLineWrap(true);
        welcomeText.setWrapStyleWord(true);
        welcomeText.setBackground(Palette.CARD);
        welcomeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeText.setForeground(Palette.TEXT);
        welcome.add(welcomeText, BorderLayout.CENTER);
        grid.add(welcome, gc);

        // Quick actions
        gc.gridx = 0; gc.gridy = 1; gc.weighty = 0.4;
        JPanel actions = card("Quick Actions");
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        row.setOpaque(false);
        JButton viewVehiclesBtn = new JButton("View All Vehicles");
        viewVehiclesBtn.addActionListener(e -> selectTabSafe(1));
        JButton addCustomerBtn = new JButton("Register New Customer");
        addCustomerBtn.addActionListener(e -> selectTabSafe(2));
        JButton makeBookingBtn = new JButton("Make a Booking");
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
        msgs.add(niceScroll(outputArea), BorderLayout.CENTER);
        grid.add(msgs, gc);

        // (REMOVED) tabbedPane.setMnemonicAt(...) here – caused IndexOutOfBoundsException
        return grid;
    }

    private JPanel buildStatsGrid() {
        JPanel stats = new JPanel(new GridLayout(2, 2, 12, 12));
        stats.setOpaque(false);
        stats.add(statChip("Total Vehicles", String.valueOf(rentalSystem.getAllVehicles().size()), Palette.INFO));
        stats.add(statChip("Total Customers", String.valueOf(rentalSystem.getCustomerCount()), Palette.TEAL));
        stats.add(statChip("Total Bookings", String.valueOf(rentalSystem.getBookingCount()), Palette.AMBER));
        long avail = rentalSystem.getAllVehicles().stream()
                .filter(v -> "Available".equalsIgnoreCase(v.getAvailabilityStatus()))
                .count();
        stats.add(statChip("Available Vehicles", String.valueOf(avail), Palette.SUCCESS));
        return stats;
    }

    private JPanel statChip(String title, String value, Color color) {
        JPanel chip = new JPanel(new BorderLayout());
        chip.setBackground(Palette.CARD);
        chip.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, tint(color, 0.95f)),
                                          new EmptyBorder(12, 12, 12, 12)));
        JLabel t = new JLabel(title);
        t.setForeground(Palette.MUTED);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 13f));
        JLabel v = new JLabel(value);
        v.setForeground(Palette.TEXT);
        v.setFont(v.getFont().deriveFont(Font.BOLD, 22f));
        chip.add(t, BorderLayout.NORTH);
        chip.add(v, BorderLayout.CENTER);
        return chip;
    }

    private JPanel createVehiclesPanel() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel controls = card("Vehicle Actions");
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        grid.setOpaque(false);
        JButton viewBtn = neutralButton("View Vehicles");
        JButton addBtn = primaryButton("Add Vehicle");
        JButton updateBtn = primaryButton("Update Vehicle");
        JButton deleteBtn = dangerButton("Delete Vehicle");
        grid.add(viewBtn); grid.add(addBtn); grid.add(updateBtn); grid.add(deleteBtn);
        controls.add(grid, BorderLayout.CENTER);

        JTextArea vehiclesArea = new JTextArea();
        vehiclesArea.setEditable(false);
        vehiclesArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JPanel listCard = card("Vehicles");
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
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel controls = card("Customer Actions");
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        grid.setOpaque(false);
        JButton registerBtn = primaryButton("Register Customer");
        JButton viewBtn = neutralButton("View Customers");
        JButton updateBtn = primaryButton("Update Customer");
        JButton deleteBtn = dangerButton("Delete Customer");
        grid.add(registerBtn); grid.add(viewBtn); grid.add(updateBtn); grid.add(deleteBtn);
        controls.add(grid, BorderLayout.CENTER);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        JPanel listCard = card("Customers");
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
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel controls = card("Booking Actions");
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        grid.setOpaque(false);
        JButton makeBookingBtn = primaryButton("Make Booking");
        JButton searchBtn = neutralButton("Search Bookings");
        JButton viewBtn = neutralButton("View Bookings");
        JButton updateBtn = primaryButton("Update Booking");
        JButton deleteBtn = dangerButton("Delete Booking");
        grid.add(makeBookingBtn); grid.add(searchBtn); grid.add(viewBtn); grid.add(updateBtn); grid.add(deleteBtn);
        controls.add(grid, BorderLayout.CENTER);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        JPanel listCard = card("Bookings");
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
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel controls = card("Invoice");
        JButton generateBtn = primaryButton("Generate Invoice");
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        row.setOpaque(false);
        row.add(generateBtn);
        controls.add(row, BorderLayout.CENTER);

        JTextArea invoicesArea = new JTextArea();
        invoicesArea.setEditable(false);
        invoicesArea.setFont(new Font("Consolas", Font.PLAIN, 13));
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
        StringBuilder sb = new StringBuilder("Available Vehicles:\n\n");
        rentalSystem.getAllVehicles().forEach(v -> sb.append(v).append("\n"));
        out.setText(sb.toString());
    }

    private void addVehicleDialog(JTextArea out) {
        JDialog d = modal("Add Vehicle", 420, 320);
        JPanel f = formGrid(5);
        JTextField id = tf(), model = tf(), category = tf(), price = tf(), status = tf("Available");

        f.add(new JLabel("Car ID:")); f.add(id);
        f.add(new JLabel("Model:")); f.add(model);
        f.add(new JLabel("Category:")); f.add(category);
        f.add(new JLabel("Daily Price:")); f.add(price);
        f.add(new JLabel("Status:")); f.add(status);

        JButton add = primaryButton("Add");
        add.addActionListener(e -> {
            try {
                EcoRide_Vehicle v = new EcoRide_Vehicle(
                        id.getText().trim(), model.getText().trim(), category.getText().trim(),
                        Double.parseDouble(price.getText().trim()), status.getText().trim());
                rentalSystem.addVehicle(v);
                out.setText("✅ Vehicle added successfully.");
                d.dispose();
            } catch (NumberFormatException ex) {
                warn(d, "Invalid price. Use a number like 7500 or 7500.00.");
            }
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(add), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void registerCustomerDialog(JTextArea out) {
        JDialog d = modal("Register Customer", 420, 320);
        JPanel f = formGrid(4);
        JTextField nic = tf(), name = tf(), contact = tf(), email = tf();

        f.add(new JLabel("NIC/Passport:")); f.add(nic);
        f.add(new JLabel("Name:")); f.add(name);
        f.add(new JLabel("Contact:")); f.add(contact);
        f.add(new JLabel("Email:")); f.add(email);

        JButton add = primaryButton("Register");
        add.addActionListener(e -> {
            EcoRide_Customer c = new EcoRide_Customer(
                    nic.getText().trim(), name.getText().trim(), contact.getText().trim(), email.getText().trim());
            rentalSystem.registerCustomer(c);
            out.setText("✅ Customer registered successfully.");
            d.dispose();
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(add), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void makeBookingDialog(JTextArea out) {
        JDialog d = modal("Make Booking", 460, 380);
        JPanel f = formGrid(6);
        JTextField id = tf(), nic = tf(), vid = tf(), start = tf("YYYY-MM-DD"), end = tf("YYYY-MM-DD"), km = tf();

        f.add(new JLabel("Booking ID:")); f.add(id);
        f.add(new JLabel("Customer NIC:")); f.add(nic);
        f.add(new JLabel("Vehicle ID:")); f.add(vid);
        f.add(new JLabel("Start Date:")); f.add(start);
        f.add(new JLabel("End Date:")); f.add(end);
        f.add(new JLabel("Total KM:")); f.add(km);

        JButton book = primaryButton("Book");
        book.addActionListener(e -> {
            try {
                EcoRide_Customer c = rentalSystem.getCustomer(nic.getText().trim());
                EcoRide_Vehicle v = rentalSystem.getVehicle(vid.getText().trim());
                if (c == null) { warn(d, "Customer not found."); return; }
                if (v == null) { warn(d, "Vehicle not found."); return; }

                LocalDate s = LocalDate.parse(start.getText().trim());
                LocalDate en = LocalDate.parse(end.getText().trim());
                int totalKm = Integer.parseInt(km.getText().trim());

                EcoRide_Booking b = new EcoRide_Booking(id.getText().trim(), c, v, s, en, totalKm);
                if (rentalSystem.makeBooking(b)) {
                    out.setText("✅ Booking successful.");
                    d.dispose();
                } else {
                    warn(d, "Booking failed. Check availability or dates.");
                }
            } catch (DateTimeParseException ex) {
                warn(d, "Invalid date format. Use YYYY-MM-DD.");
            } catch (NumberFormatException ex) {
                warn(d, "Invalid KM. Use a whole number.");
            }
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(book), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void searchBookingsDialog(JTextArea out) {
        String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
        if (name != null) {
            StringBuilder sb = new StringBuilder("Bookings for " + name + ":\n\n");
            for (EcoRide_Booking b : rentalSystem.searchBookingsByName(name)) sb.append(b).append("\n");
            out.setText(sb.toString());
        }
    }

    private void viewCustomers(JTextArea out) {
        StringBuilder sb = new StringBuilder("Registered Customers:\n\n");
        rentalSystem.getAllCustomers().forEach(c -> sb.append(c).append("\n"));
        out.setText(sb.toString());
    }

    private void viewBookings(JTextArea out) {
        StringBuilder sb = new StringBuilder("All Bookings:\n\n");
        rentalSystem.getAllBookings().forEach(b -> sb.append(b).append("\n"));
        out.setText(sb.toString());
    }

    private void updateVehicleDialog(JTextArea out) {
        String carId = JOptionPane.showInputDialog(this, "Enter Vehicle ID to update:");
        if (carId == null) return;
        EcoRide_Vehicle existing = rentalSystem.getVehicle(carId);
        if (existing == null) { warn(this, "Vehicle not found."); return; }

        JDialog d = modal("Update Vehicle", 440, 320);
        JPanel f = formGrid(4);
        JTextField model = tf(existing.getModel());
        JTextField category = tf(existing.getCategory());
        JTextField price = tf(String.valueOf(existing.getDailyRentalPrice()));
        JTextField status = tf(existing.getAvailabilityStatus());

        f.add(new JLabel("Model:")); f.add(model);
        f.add(new JLabel("Category:")); f.add(category);
        f.add(new JLabel("Daily Price:")); f.add(price);
        f.add(new JLabel("Status:")); f.add(status);

        JButton update = primaryButton("Update");
        update.addActionListener(e -> {
            try {
                EcoRide_Vehicle v = new EcoRide_Vehicle(
                        carId, model.getText().trim(), category.getText().trim(),
                        Double.parseDouble(price.getText().trim()), status.getText().trim());
                if (rentalSystem.updateVehicle(carId, v)) {
                    out.setText("✅ Vehicle updated successfully.");
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
        if (rentalSystem.deleteVehicle(carId)) out.setText("🗑️ Vehicle deleted.");
        else warn(this, "Vehicle not found or delete failed.");
    }

    private void updateCustomerDialog(JTextArea out) {
        String nic = JOptionPane.showInputDialog(this, "Enter Customer NIC to update:");
        if (nic == null) return;
        EcoRide_Customer existing = rentalSystem.getCustomer(nic);
        if (existing == null) { warn(this, "Customer not found."); return; }

        JDialog d = modal("Update Customer", 440, 300);
        JPanel f = formGrid(3);
        JTextField name = tf(existing.getName());
        JTextField contact = tf(existing.getContactNumber());
        JTextField email = tf(existing.getEmail());

        f.add(new JLabel("Name:")); f.add(name);
        f.add(new JLabel("Contact:")); f.add(contact);
        f.add(new JLabel("Email:")); f.add(email);

        JButton update = primaryButton("Update");
        update.addActionListener(e -> {
            EcoRide_Customer c = new EcoRide_Customer(nic, name.getText().trim(), contact.getText().trim(), email.getText().trim());
            if (rentalSystem.updateCustomer(nic, c)) {
                out.setText("✅ Customer updated successfully.");
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
        if (rentalSystem.deleteCustomer(nic)) out.setText("🗑️ Customer deleted.");
        else warn(this, "Customer not found or delete failed.");
    }

    private void updateBookingDialog(JTextArea out) {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to update:");
        if (bookingId == null) return;
        EcoRide_Booking existing = rentalSystem.getBooking(bookingId);
        if (existing == null) { warn(this, "Booking not found."); return; }

        JDialog d = modal("Update Booking", 460, 360);
        JPanel f = formGrid(5);
        JTextField nic = tf(existing.getCustomer().getNicOrPassport());
        JTextField vid = tf(existing.getVehicle().getCarId());
        JTextField start = tf(existing.getStartDate().toString());
        JTextField end = tf(existing.getEndDate().toString());
        JTextField km = tf(String.valueOf(existing.getTotalKm()));

        f.add(new JLabel("Customer NIC:")); f.add(nic);
        f.add(new JLabel("Vehicle ID:")); f.add(vid);
        f.add(new JLabel("Start Date:")); f.add(start);
        f.add(new JLabel("End Date:")); f.add(end);
        f.add(new JLabel("Total KM:")); f.add(km);

        JButton update = primaryButton("Update");
        update.addActionListener(e -> {
            try {
                EcoRide_Customer c = rentalSystem.getCustomer(nic.getText().trim());
                EcoRide_Vehicle v = rentalSystem.getVehicle(vid.getText().trim());
                if (c == null) { warn(d, "Customer not found."); return; }
                if (v == null) { warn(d, "Vehicle not found."); return; }
                LocalDate s = LocalDate.parse(start.getText().trim());
                LocalDate en = LocalDate.parse(end.getText().trim());
                int totalKm = Integer.parseInt(km.getText().trim());

                EcoRide_Booking upd = new EcoRide_Booking(bookingId, c, v, s, en, totalKm);
                // Requires rentalSystem.updateBooking(...)
                if (rentalSystem.updateBooking(bookingId, upd)) {
                    out.setText("✅ Booking updated successfully.");
                    d.dispose();
                } else warn(d, "Update failed.");
            } catch (DateTimeParseException ex) { warn(d, "Invalid date format. Use YYYY-MM-DD."); }
              catch (NumberFormatException ex) { warn(d, "Invalid KM."); }
        });

        d.add(f, BorderLayout.CENTER);
        d.add(footerRight(update), BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void deleteBookingDialog(JTextArea out) {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to delete:");
        if (bookingId == null) return;
        // Requires rentalSystem.deleteBooking(...)
        if (rentalSystem.deleteBooking(bookingId)) out.setText("🗑️ Booking deleted.");
        else warn(this, "Booking not found or delete failed.");
    }

    private void generateInvoiceDialog() {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID:");
        if (bookingId == null) return;
        EcoRide_Booking b = rentalSystem.getBooking(bookingId);
        if (b == null) { warn(this, "Booking not found."); return; }
        EcoRide_Invoice invoice = rentalSystem.generateInvoice(b);
        outputArea.setText(invoice.toString());
    }

    // ---------- Small UI utilities ----------
    private JDialog modal(String title, int w, int h) {
        JDialog d = new JDialog(this, title, true);
        d.setLayout(new BorderLayout(12, 12));
        d.getContentPane().setBackground(Palette.CARD);
        d.setSize(w, h);
        d.setLocationRelativeTo(this);
        d.getRootPane().setBorder(new EmptyBorder(12, 12, 12, 12));
        return d;
    }
    private JPanel formGrid(int rows) {
        JPanel p = new JPanel(new GridLayout(rows, 2, 10, 10));
        p.setOpaque(false);
        return p;
    }
    private JTextField tf() { return tf(""); }
    private JTextField tf(String text) {
        JTextField t = new JTextField(text);
        t.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, Palette.DIVIDER),
                                       new EmptyBorder(8, 8, 8, 8)));
        return t;
    }
    private JPanel footerRight(JButton b) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setOpaque(false);
        p.add(b);
        return p;
    }
    private void warn(Component parent, String msg) {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 14));
        JOptionPane.showMessageDialog(parent, msg, "EcoRide", JOptionPane.WARNING_MESSAGE);
        // restore defaults if needed
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
