package autoservice.repair.services;

import autoservice.repair.model.*;

import java.time.LocalDateTime;

public class RepairOrder {

    private final Customer customer;
    private final Mechanic mechanic;
    private final Car car;
    private final Motorcycle motorcycle;
    private final Truck truck;
    private final Service service;
    private final LocalDateTime orderDate;

    public RepairOrder(Customer customer, Mechanic mechanic, Car car, Service service, LocalDateTime orderDate) {
        if (customer.getAge() < 18) {
            throw new IllegalArgumentException("According to Georgian law underage (-18) customers cannot drive cars!");
        }
        this.customer = customer;
        this.mechanic = mechanic;
        this.car = car;
        this.motorcycle = null;
        this.truck = null;
        this.service = service;
        this.orderDate = orderDate;
    }

    public RepairOrder(Customer customer, Mechanic mechanic, Motorcycle motorcycle, Service service, LocalDateTime orderDate) {
        int age = customer.getAge();
        if (age < 16 && motorcycle.getEngineCapacity() > 50) {
            throw new IllegalArgumentException("According to Georgian law customers under 16 cannot drive motorcycles above 50cc.");
        }
        if (age == 17 && motorcycle.getEngineCapacity() > 125) {
            throw new IllegalArgumentException("According to Georgian law customers of age 17 cannot drive motorcycles above 125cc.");
        }
        this.customer = customer;
        this.mechanic = mechanic;
        this.car = null;
        this.motorcycle = motorcycle;
        this.truck = null;
        this.service = service;
        this.orderDate = orderDate;
    }

    public RepairOrder(Customer customer, Mechanic mechanic, Truck truck, Service service, LocalDateTime orderDate) {
        if (customer.getAge() < 18) {
            throw new IllegalArgumentException("According to Georgian law customers under 18 cannot drive trucks.");
        }
        this.customer = customer;
        this.mechanic = mechanic;
        this.car = null;
        this.motorcycle = null;
        this.truck = truck;
        this.service = service;
        this.orderDate = orderDate;
    }

    public String getVehicleBrand() {
        if (car != null) return car.getBrand();
        if (motorcycle != null) return motorcycle.getBrand();
        return truck.getBrand();
    }

    public String getVehicleModel() {
        if (car != null) return car.getModel();
        if (motorcycle != null) return motorcycle.getModel();
        return truck.getModel();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public Car getCar() {
        return car;
    }

    public Motorcycle getMotorcycle() {
        return motorcycle;
    }

    public Truck getTruck() {
        return truck;
    }

    public Service getService() {
        return service;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

}
