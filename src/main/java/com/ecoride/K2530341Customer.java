package com.ecoride;

/**
 * Represents a customer in the EcoRide Car Rental System.
 * Demonstrates OOP: Encapsulation.
 */
public class K2530341Customer {
    private String nicOrPassport;
    private String name;
    private String contactNumber;
    private String email;

    // Constructor
    public K2530341Customer(String nicOrPassport, String name, String contactNumber, String email) {
        this.nicOrPassport = nicOrPassport;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // Getters and Setters
    public String getNicOrPassport() { return nicOrPassport; }
    public void setNicOrPassport(String nicOrPassport) { this.nicOrPassport = nicOrPassport; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Customer{" +
                "nicOrPassport='" + nicOrPassport + '\'' +
                ", name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
