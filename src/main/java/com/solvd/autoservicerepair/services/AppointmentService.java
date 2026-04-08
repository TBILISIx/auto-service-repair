package com.solvd.autoservicerepair.services;

import com.solvd.autoservicerepair.enums.ServiceStatus;
import com.solvd.autoservicerepair.exceptions.AppointmentStatusException;
import com.solvd.autoservicerepair.model.Customer;
import com.solvd.autoservicerepair.model.Mechanic;
import com.solvd.autoservicerepair.model.Vehicle;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class AppointmentService extends Document {

    private final Customer customer;
    private final Mechanic mechanic;
    private final Vehicle vehicle;
    private final LocalDateTime scheduledTime;
    private ServiceStatus status;

    // Constructor
    public AppointmentService(Integer id, Customer customer, Mechanic mechanic, Vehicle vehicle, LocalDateTime scheduledTime) {
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

        log.info("------------------------------------------------------------------------------");
        log.info("\n=== APPOINTMENT STARTED ===");
        log.info("Customer        : " + customer.getName());
        log.info("Vehicle         : " + getVehicleDescription());
        log.info("Mechanic        : " + mechanic.getName());
        log.info("Scheduled Time  : " + getFormattedScheduledTime());
        log.info("Status          : " + status.getDisplayName());
        log.info("============================");
    }

    public void complete() {
        if (status != ServiceStatus.IN_PROGRESS) {
            throw new AppointmentStatusException(
                    "Appointment can only be completed if IN_PROGRESS. Current: " + status.getDisplayName()
            );
        }

        this.status = ServiceStatus.DONE;

        log.info("=== APPOINTMENT COMPLETED ===");
        log.info("Customer : " + customer.getName());
        log.info("Vehicle  : " + getVehicleDescription());
        log.info("Status   : " + status.getDisplayName());
        log.info("=============================");
    }

    public void cancel() {
        if (!status.canBeCancelled()) {
            throw new AppointmentStatusException(
                    "Cannot cancel appointment in status: " + status.getDisplayName()
            );
        }

        this.status = ServiceStatus.CANCELLED;

        log.info("=== APPOINTMENT CANCELLED ===");
        log.info("Customer : " + customer.getName());
        log.info("Vehicle  : " + getVehicleDescription());
        log.info("Status   : " + status.getDisplayName());
        log.info("==============================");
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