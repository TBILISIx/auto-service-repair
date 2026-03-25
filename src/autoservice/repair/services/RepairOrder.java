package autoservice.repair.services;

import autoservice.repair.model.Customer;
import autoservice.repair.model.Mechanic;
import autoservice.repair.model.ValidAge;
import autoservice.repair.model.Vehicle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RepairOrder<T extends Vehicle & ValidAge> {

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

    public T getVehicle() {
        return vehicle;
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
    public Service getService() {
        return service;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return "RepairOrder{" +
                "customer=" + customer.getName() +
                ", mechanic=" + mechanic.getName() +
                ", vehicle=" + vehicle.getBrand() + " " + vehicle.getModel() +
                ", service=" + service.getClass().getSimpleName() +
                ", date=" + orderDate.format(formatter) +
                '}';
    }

}
