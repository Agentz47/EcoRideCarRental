EcoRide is a Java-based car rental management system designed to demonstrate object-oriented programming concepts, data structures, GUI development, and performance testing. The system manages vehicles, customers, bookings, fees, and authentication while storing all data in CSV files.

-----------------------------------------
Features

- Vehicle Management: Add, edit, delete, and search vehicles (Petrol, Hybrid, Electric, Luxury SUV).
- Customer Management: Register and manage customer details.
- Booking System: Create, update, and cancel bookings with validation rules.
- Fee Calculation: Automatic calculation of rental fees, discounts, extra-kilometre charges, and taxes.
- Authentication: Login with Admin and Customer roles.
- Data Persistence: Stores vehicles, customers, and bookings in CSV files.
- GUI Interface: Java Swing interface for easy system interaction.
- Load Testing: Generates synthetic data and measures system performance.

-----------------------------------------
Requirements

- Java 11 or higher
- Maven 3.6+
- Works on Windows, macOS, or Linux

-----------------------------------------
Installation and Setup

1. Download or clone the project.
2. Open a terminal and navigate to the project folder:
   cd EcoRideCarRental
3. Build the project using Maven:
   mvn clean compile
4. Run the application:

   • GUI Mode (default):
     java -cp target/classes com.ecoride.K2530341Main

   • Load Test Mode:
     java -cp target/classes com.ecoride.K2530341Main --load-test <customers> <vehicles> <bookings>

   Example:
   java -cp target/classes com.ecoride.K2530341Main --load-test 1000 500 2000

-----------------------------------------
Usage

GUI Mode:
- Log in as Admin or Customer
- Manage vehicles, customers, and bookings
- Generate invoices and view reports

Load Testing:
- Run the system using the --load-test option
- Produces synthetic data and prints performance results
- Useful for scalability and stress testing

Data Files:
- vehicles.csv – vehicle records
- customers.csv – customer records
- bookings.csv – booking records
(Automatically created and loaded on startup)

-----------------------------------------
Project Structure (Summary)

EcoRideCarRental/
- src/main/java/com/ecoride/
  - K2530341Main.java – Application entry point
  - K2530341RentalSystem.java – Core logic
  - K2530341LoadTest.java – Performance testing
  - K2530341GUI.java – Swing interface
  - K2530341AuthSystem.java – Authentication
  - Models for Vehicle, Customer, Booking
- pom.xml – Maven configuration
- target/ – Compiled classes

-----------------------------------------
Key Concepts

- Data Structures: ArrayList, HashMap
- OOP: Encapsulation, inheritance, polymorphism
- Design Patterns: Factory, simple Singleton usage
- Business Rules: Minimum 3-day advance booking, cancellation policies, fee formula

-----------------------------------------
Load Test Summary

With approximately 350 records:
- Data insertion: ~840 ms
- Searching: under 1 ms
- Fee calculation: ~121 microseconds

The system remains responsive and stable at these scales.

-----------------------------------------
License

Developed for educational use as part of the Programming III coursework of Student K2530341 SAJIDH YAZEEN.
