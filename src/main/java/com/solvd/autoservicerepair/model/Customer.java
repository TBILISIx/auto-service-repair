package com.solvd.autoservicerepair.model;

import java.time.LocalDate;

public class Customer extends Person {

    private Integer age;
    private Integer loyaltyPoints;
    private final LocalDate registrationDate;
    private Insurance insurance;
    private final String email;

    public Customer(String name, String idNumber, String phone, Integer age, Insurance insurance, String email) {
        super(name, idNumber, phone);
        this.age = age;

        this.loyaltyPoints = 0;
        this.registrationDate = LocalDate.now();
        this.insurance = insurance;
        this.email = email;
    }

    // Add loyalty points
    public void addLoyaltyPoints(Integer points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        }
    }

    // Getters
    public Integer getAge() {
        return age;
    }

    public Integer getLoyaltyPoints() {
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
    public void setAge(Integer age) {
        this.age = age;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + getName() + '\'' +
                ", idNumber='" + getIdNumber() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", age=" + age +
                ", loyaltyPoints=" + loyaltyPoints +
                ", registrationDate=" + registrationDate +
                ", insurance=" + insurance +
                ", email='" + email + '\'' +
                '}';
    }

}