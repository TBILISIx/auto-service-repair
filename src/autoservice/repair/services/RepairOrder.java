package autoservice.repair.services;

import autoservice.repair.model.*;

import java.time.LocalDateTime;

public class RepairOrder <T extends Vehicle & ValidAge> {

    private final Customer customer;
    private final Mechanic mechanic;
    private final T vehicle;
    private final Service service;
    private final LocalDateTime orderDate;

    public RepairOrder(Customer customer, Mechanic mechanic, T vehicle, Service service, LocalDateTime orderDate) {
        vehicle.validateAge(customer);
        this.customer = customer;
        this.mechanic = mechanic;
        this.vehicle = vehicle;
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
