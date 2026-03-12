package autoservice.repair.model;

import java.time.LocalDate;

public class Customer {

    private String name;
    private String phone;
    private int age;
    private int loyaltyPoints;
    private LocalDate registrationDate;
    private Insurance insurance;
    private String email;

    public Customer(String name, int age, String phone, Insurance insurance,String email) {
        this.name = name;
        this.age = age;
        this.phone = phone;
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
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

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
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

}