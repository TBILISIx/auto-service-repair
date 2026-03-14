package autoservice.repair.services;

import autoservice.repair.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment extends Document{

    private final Customer customer;
    private final Mechanic mechanic;
    private final Car car;
    private final Motorcycle motorcycle;
    private final Truck truck;
    private final LocalDateTime scheduledTime;
    private String status; // SCHEDULED, IN_PROGRESS, DONE, CANCELLED

    public Appointment(int id,Customer customer, Mechanic mechanic, Car car, LocalDateTime scheduledTime) {
        super(id);
        this.customer = customer;
        this.mechanic = mechanic;
        this.car = car;
        this.motorcycle = null;
        this.truck = null;
        this.scheduledTime = scheduledTime;
        this.status = "SCHEDULED";
    }

    public Appointment(int id, Customer customer, Mechanic mechanic, Motorcycle motorcycle, LocalDateTime scheduledTime) {
        super(id);
        this.customer = customer;
        this.mechanic = mechanic;
        this.car = null;
        this.motorcycle = motorcycle;
        this.truck = null;
        this.scheduledTime = scheduledTime;
        this.status = "SCHEDULED";
    }

    public Appointment(int id, Customer customer, Mechanic mechanic, Truck truck, LocalDateTime scheduledTime) {
        super(id);
        this.customer = customer;
        this.mechanic = mechanic;
        this.car = null;
        this.motorcycle = null;
        this.truck = truck;
        this.scheduledTime = scheduledTime;
        this.status = "SCHEDULED";
    }

    public String getVehicleDescription() {
        if (car != null) return car.getBrand() + " " + car.getModel();
        if (motorcycle != null) return motorcycle.getBrand() + " " + motorcycle.getModel();
        return truck.getBrand() + " " + truck.getModel();
    }

    public void start() {
        if (!status.equals("SCHEDULED")) {
            throw new IllegalStateException("Appointment can only be started if SCHEDULED. Current: " + status);
        }
        this.status = "IN_PROGRESS";
        System.out.println("Appointment started for " + customer.getName()
                + " | Vehicle: " + getVehicleDescription()
                + " | Mechanic: " + mechanic.getName());
    }

    public void complete() {
        if (!status.equals("IN_PROGRESS")) {
            throw new IllegalStateException("Appointment can only be completed if IN_PROGRESS. Current: " + status);
        }
        this.status = "DONE";
        System.out.println("Appointment completed for " + customer.getName());
    }

    public void cancel() {
        if (status.equals("DONE")) {
            throw new IllegalStateException("Cannot cancel an already completed appointment.");
        }
        this.status = "CANCELLED";
        System.out.println("Appointment cancelled for " + customer.getName());
    }

    public String getFormattedScheduledTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return scheduledTime.format(formatter);
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

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public String getStatus() {
        return status;
    }

}
