package autoservice.repair.services;

import autoservice.repair.enums.ServiceStatus;
import autoservice.repair.exceptions.AppointmentStatusException;
import autoservice.repair.model.Customer;
import autoservice.repair.model.Mechanic;
import autoservice.repair.model.Vehicle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment extends Document {

    private final Customer customer;
    private final Mechanic mechanic;
    private final Vehicle vehicle;
    private final LocalDateTime scheduledTime;
    private ServiceStatus status;

    // Constructor
    public Appointment(Integer id, Customer customer, Mechanic mechanic, Vehicle vehicle, LocalDateTime scheduledTime) {
        super(id);
        this.customer = customer;
        this.mechanic = mechanic;
        this.vehicle = vehicle;
        this.scheduledTime = scheduledTime;
        this.status = ServiceStatus.SCHEDULED;
    }

    // Vehicle Description
    public String getVehicleDescription() {
        return vehicle.getBrand() + " " + vehicle.getModel();
    }

    // Appointment Actions
    public void start() {
        if (status != ServiceStatus.SCHEDULED) {
            throw new AppointmentStatusException(
                    "Appointment can only be started if SCHEDULED. Current: " + status.getDisplayName()
            );
        }

        this.status = ServiceStatus.IN_PROGRESS;

        System.out.println("------------------------------------------------------------------------------");
        System.out.println("\n=== APPOINTMENT STARTED ===");
        System.out.println("Customer        : " + customer.getName());
        System.out.println("Vehicle         : " + getVehicleDescription());
        System.out.println("Mechanic        : " + mechanic.getName());
        System.out.println("Scheduled Time  : " + getFormattedScheduledTime());
        System.out.println("Status          : " + status.getDisplayName());
        System.out.println("============================");
    }

    public void complete() {
        if (status != ServiceStatus.IN_PROGRESS) {
            throw new AppointmentStatusException(
                    "Appointment can only be completed if IN_PROGRESS. Current: " + status.getDisplayName()
            );
        }

        this.status = ServiceStatus.DONE;

        System.out.println("=== APPOINTMENT COMPLETED ===");
        System.out.println("Customer : " + customer.getName());
        System.out.println("Vehicle  : " + getVehicleDescription());
        System.out.println("Status   : " + status.getDisplayName());
        System.out.println("=============================");
    }

    public void cancel() {
        if (!status.canBeCancelled()) {
            throw new AppointmentStatusException(
                    "Cannot cancel appointment in status: " + status.getDisplayName()
            );
        }

        this.status = ServiceStatus.CANCELLED;

        System.out.println("=== APPOINTMENT CANCELLED ===");
        System.out.println("Customer : " + customer.getName());
        System.out.println("Vehicle  : " + getVehicleDescription());
        System.out.println("Status   : " + status.getDisplayName());
        System.out.println("==============================");
    }

    // Formatting Scheduled Time
    public String getFormattedScheduledTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return scheduledTime.format(formatter);
    }

    // Getters
    public Customer getCustomer() {
        return customer;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + getId() +
                ", customer=" + customer.getName() +
                ", mechanic=" + mechanic.getName() +
                ", vehicle=" + getVehicleDescription() +
                ", scheduledTime=" + getFormattedScheduledTime() +
                ", status='" + status.getDisplayName() + '\'' +
                '}';
    }
}