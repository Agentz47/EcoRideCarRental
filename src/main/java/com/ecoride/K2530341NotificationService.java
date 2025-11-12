package com.ecoride;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Notification Service for EcoRide Car Rental System.
 * Handles email notifications for bookings, confirmations, and reminders.
 * Demonstrates OOP: Utility class with static methods.
 */
public class K2530341NotificationService {

    /**
     * Send booking confirmation email
     */
    public static void sendBookingConfirmation(K2530341Booking booking) {
        K2530341Customer customer = booking.getCustomer();
        K2530341Vehicle vehicle = booking.getVehicle();

        String subject = "EcoRide Booking Confirmation - " + booking.getBookingId();
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(customer.getName()).append(",\n\n");
        body.append("Your booking has been confirmed!\n\n");
        body.append("Booking Details:\n");
        body.append("- Booking ID: ").append(booking.getBookingId()).append("\n");
        body.append("- Vehicle: ").append(vehicle.getModel()).append(" (").append(vehicle.getCategory()).append(")\n");
        body.append("- Rental Period: ").append(booking.getStartDate())
             .append(" to ").append(booking.getEndDate()).append("\n");
        body.append("- Duration: ").append(booking.getNumberOfDays()).append(" days\n");
        body.append("- Estimated Cost: LKR ").append(String.format("%.2f", K2530341FeeCalculator.calculateEstimatedFee(booking))).append("\n\n");
        body.append("Important Notes:\n");
        body.append("- Please arrive 15 minutes before your pickup time\n");
        body.append("- Bring your NIC/Passport and this confirmation\n");
        body.append("- A refundable deposit of LKR 5,000 will be collected\n");
        body.append("- Vehicle must be returned with full tank\n\n");
        body.append("Thank you for choosing EcoRide!\n\n");
        body.append("Best regards,\n");
        body.append("EcoRide Car Rental Team\n");
        body.append("Contact: +94 11 123 4567\n");
        body.append("Email: info@ecoride.lk");

        // In a real implementation, this would send actual email
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + customer.getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body.toString());
        System.out.println("==========================");

        // Log the notification
        K2530341AuditLogger.getInstance().log("SYSTEM", "EMAIL_SENT",
            String.format("Booking confirmation sent to %s for booking %s",
                customer.getEmail(), booking.getBookingId()));
    }

    /**
     * Send booking reminder (1 day before pickup)
     */
    public static void sendPickupReminder(K2530341Booking booking) {
        K2530341Customer customer = booking.getCustomer();
        K2530341Vehicle vehicle = booking.getVehicle();

        String subject = "EcoRide Pickup Reminder - Tomorrow";
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(customer.getName()).append(",\n\n");
        body.append("This is a reminder for your upcoming vehicle pickup tomorrow.\n\n");
        body.append("Booking Details:\n");
        body.append("- Booking ID: ").append(booking.getBookingId()).append("\n");
        body.append("- Vehicle: ").append(vehicle.getModel()).append(" (").append(vehicle.getCategory()).append(")\n");
        body.append("- Pickup Date: ").append(booking.getStartDate()).append("\n");
        body.append("- Return Date: ").append(booking.getEndDate()).append("\n\n");
        body.append("Please ensure:\n");
        body.append("- Valid driving license\n");
        body.append("- Credit card for deposit and fuel\n");
        body.append("- Full insurance coverage\n\n");
        body.append("Pickup Location: EcoRide Main Branch, Colombo\n");
        body.append("Contact: +94 11 123 4567\n\n");
        body.append("See you tomorrow!\n\n");
        body.append("Best regards,\n");
        body.append("EcoRide Car Rental Team");

        System.out.println("=== EMAIL REMINDER ===");
        System.out.println("To: " + customer.getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body.toString());
        System.out.println("======================");

        K2530341AuditLogger.getInstance().log("SYSTEM", "REMINDER_SENT",
            String.format("Pickup reminder sent to %s for booking %s",
                customer.getEmail(), booking.getBookingId()));
    }

    /**
     * Send return reminder (1 day before return)
     */
    public static void sendReturnReminder(K2530341Booking booking) {
        K2530341Customer customer = booking.getCustomer();

        String subject = "EcoRide Vehicle Return Reminder - Tomorrow";
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(customer.getName()).append(",\n\n");
        body.append("This is a reminder that your vehicle rental ends tomorrow.\n\n");
        body.append("Booking Details:\n");
        body.append("- Booking ID: ").append(booking.getBookingId()).append("\n");
        body.append("- Return Date: ").append(booking.getEndDate()).append("\n");
        body.append("- Return Location: EcoRide Main Branch, Colombo\n\n");
        body.append("Please ensure:\n");
        body.append("- Vehicle is returned with full fuel tank\n");
        body.append("- No damages beyond normal wear\n");
        body.append("- All accessories are intact\n\n");
        body.append("Late returns will incur additional charges.\n");
        body.append("Contact us immediately if you need to extend your rental.\n\n");
        body.append("Thank you for your business!\n\n");
        body.append("Best regards,\n");
        body.append("EcoRide Car Rental Team\n");
        body.append("Contact: +94 11 123 4567");

        System.out.println("=== RETURN REMINDER ===");
        System.out.println("To: " + customer.getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body.toString());
        System.out.println("=======================");

        K2530341AuditLogger.getInstance().log("SYSTEM", "RETURN_REMINDER_SENT",
            String.format("Return reminder sent to %s for booking %s",
                customer.getEmail(), booking.getBookingId()));
    }

    /**
     * Send invoice after booking completion
     */
    public static void sendInvoice(K2530341Invoice invoice, int actualKm) {
        K2530341Booking booking = invoice.getBooking();
        K2530341Customer customer = booking.getCustomer();

        String subject = "EcoRide Invoice - " + booking.getBookingId();
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(customer.getName()).append(",\n\n");
        body.append("Thank you for using EcoRide! Here is your final invoice.\n\n");

        // Add fee breakdown
        body.append(K2530341FeeCalculator.generateFeeBreakdown(booking, actualKm));
        body.append("\n\n");

        body.append("Payment Details:\n");
        body.append("- Total Amount: LKR ").append(String.format("%.2f", invoice.getTotalAmount())).append("\n");
        body.append("- Deposit Refunded: LKR 5,000\n");
        body.append("- Amount Due: LKR ").append(String.format("%.2f", invoice.getTotalAmount() - 5000)).append("\n\n");

        body.append("We hope you enjoyed your EcoRide experience!\n");
        body.append("Please consider us for your next rental needs.\n\n");

        body.append("Best regards,\n");
        body.append("EcoRide Car Rental Team\n");
        body.append("Contact: +94 11 123 4567\n");
        body.append("Email: info@ecoride.lk");

        System.out.println("=== INVOICE EMAIL ===");
        System.out.println("To: " + customer.getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body.toString());
        System.out.println("=====================");

        K2530341AuditLogger.getInstance().log("SYSTEM", "INVOICE_SENT",
            String.format("Invoice sent to %s for booking %s, amount: LKR %.2f",
                customer.getEmail(), booking.getBookingId(), invoice.getTotalAmount()));
    }

    /**
     * Send cancellation confirmation
     */
    public static void sendCancellationConfirmation(K2530341Booking booking) {
        K2530341Customer customer = booking.getCustomer();

        String subject = "EcoRide Booking Cancellation - " + booking.getBookingId();
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(customer.getName()).append(",\n\n");
        body.append("Your booking has been successfully cancelled.\n\n");
        body.append("Cancelled Booking Details:\n");
        body.append("- Booking ID: ").append(booking.getBookingId()).append("\n");
        body.append("- Vehicle: ").append(booking.getVehicle().getModel()).append("\n");
        body.append("- Original Dates: ").append(booking.getStartDate())
             .append(" to ").append(booking.getEndDate()).append("\n\n");
        body.append("Your refundable deposit of LKR 5,000 will be processed within 3-5 business days.\n\n");
        body.append("We hope to serve you again in the future!\n\n");
        body.append("Best regards,\n");
        body.append("EcoRide Car Rental Team");

        System.out.println("=== CANCELLATION EMAIL ===");
        System.out.println("To: " + customer.getEmail());
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body.toString());
        System.out.println("==========================");

        K2530341AuditLogger.getInstance().log("SYSTEM", "CANCELLATION_CONFIRMED",
            String.format("Cancellation confirmation sent to %s for booking %s",
                customer.getEmail(), booking.getBookingId()));
    }

    /**
     * Check and send automated reminders
     */
    public static void checkAndSendReminders(K2530341RentalSystem rentalSystem) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<K2530341Booking> allBookings = rentalSystem.getAllBookings();

        for (K2530341Booking booking : allBookings) {
            // Send pickup reminder 1 day before
            if (booking.getStartDate().equals(tomorrow)) {
                sendPickupReminder(booking);
            }

            // Send return reminder 1 day before end date
            if (booking.getEndDate().equals(tomorrow)) {
                sendReturnReminder(booking);
            }
        }
    }
}
