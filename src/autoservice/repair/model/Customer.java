package autoservice.repair.model;

import java.time.LocalDate;

public class Customer extends Person {

    private int age;
    private int loyaltyPoints;
    private final LocalDate registrationDate;
    private Insurance insurance;
    private final String email;

    public Customer(String name, int age, String phone, Insurance insurance, String email) {
        super(name, phone);
        this.age = age;

        this.loyaltyPoints = 0;
        this.registrationDate = LocalDate.now();
        this.insurance = insurance;
        this.email = email;
    }

    // Add loyalty points
    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        }
    }

    // Getters

    public int getAge() {
        return age;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public String getEmail() {
        return email;
    }

    // Setters

    public void setAge(int age) {
        this.age = age;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

}