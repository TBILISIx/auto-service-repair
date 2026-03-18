package autoservice.repair.services;

import autoservice.repair.model.*;

import java.time.LocalDateTime;

public class RepairOrder {

    private final Customer customer;
    private final Mechanic mechanic;
    private final Vehicle vehicle;
    private final Service service;
    private final LocalDateTime orderDate;

    public RepairOrder(Customer customer, Mechanic mechanic, Car car, Service service, LocalDateTime orderDate) {
        if (customer.getAge() < 18) {
            throw new IllegalArgumentException("According to Georgian law underage (-18) customers cannot drive cars!");
        }
        this.customer = customer;
        this.mechanic = mechanic;
        this.vehicle = car;
        this.service = service;
        this.orderDate = orderDate;
    }

    public RepairOrder(Customer customer, Mechanic mechanic, Motorcycle motorcycle, Service service, LocalDateTime orderDate) {
        Integer age = customer.getAge();
        if (age < 16 && motorcycle.getEngineCapacity() > 50) {
            throw new IllegalArgumentException("According to Georgian law customers under 16 cannot drive motorcycles above 50cc.");
        }
        if (age == 17 && motorcycle.getEngineCapacity() > 125) {
            throw new IllegalArgumentException("According to Georgian law customers of age 17 cannot drive motorcycles above 125cc.");
        }
        this.customer = customer;
        this.mechanic = mechanic;
        this.vehicle = motorcycle;
        this.service = service;
        this.orderDate = orderDate;
    }

    public RepairOrder(Customer customer, Mechanic mechanic, Truck truck, Service service, LocalDateTime orderDate) {
        if (customer.getAge() < 18) {
            throw new IllegalArgumentException("According to Georgian law customers under 18 cannot drive trucks.");
        }
        this.customer = customer;
        this.mechanic = mechanic;
        this.vehicle = truck;
        this.service = service;
        this.orderDate = orderDate;
    }

    public String getVehicleBrand() {
        return vehicle.getBrand();
    }
    public String getVehicleModel() {
        return vehicle.getModel();
    }

    public Customer getCustomer() {
        return customer;
    }
    public Mechanic getMechanic() {
        return mechanic;
    }

    public Car getCar() {
        return (vehicle instanceof Car) ? (Car) vehicle : null;
    }
    public Motorcycle getMotorcycle() {
        return (vehicle instanceof Motorcycle) ? (Motorcycle) vehicle : null;
    }
    public Truck getTruck() {
        return (vehicle instanceof Truck) ? (Truck) vehicle : null;
    }

    public Service getService() {
        return service;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

}
