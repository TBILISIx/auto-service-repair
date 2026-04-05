package com.solvd.auto.service.repair.model;

import java.util.Objects;

public class Person {

    private final String name;
    private final String idNumber;
    private final String phone;

    public Person(String name, String idNumber, String phone) {
        this.name = name;
        this.idNumber = idNumber;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;
        return Objects.equals(idNumber, person.idNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber);
    }

}
