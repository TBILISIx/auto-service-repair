package com.solvd.autoservicerepair.services;

import com.solvd.autoservicerepair.interfaces.ValidAge;
import com.solvd.autoservicerepair.model.Customer;
import com.solvd.autoservicerepair.model.Mechanic;
import com.solvd.autoservicerepair.model.Vehicle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record RepairOrder<T extends Vehicle & ValidAge>(Customer customer, Mechanic mechanic, T vehicle,
                                                        Service service, LocalDateTime orderDate) {

    public RepairOrder {
        vehicle.validateAge(customer);
    }

    public String getVehicleBrand() {
        return vehicle.getBrand();
    }
    public String getVehicleModel() {
        return vehicle.getModel();
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
