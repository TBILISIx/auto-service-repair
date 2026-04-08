package com.solvd.autoservicerepair.services;

import com.solvd.autoservicerepair.enums.PaymentMethod;
import com.solvd.autoservicerepair.exceptions.GarageBookingException;
import com.solvd.autoservicerepair.model.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public record BookingService(Garage garage) implements AutoCloseable {

    private static Integer totalOrders;
    static {
        totalOrders = 0;
        log.info("------------------------------------------------------------------------------");
        log.info("Auto Repair System Started");
    }

    public static Integer getTotalOrders() {
        return totalOrders;
    }

    public void createOrder(List<RepairOrder<?>> repairOrders) throws GarageBookingException {
        for (RepairOrder<?> repairOrder : repairOrders) {
            if (!garage.hasFreeBay()) {
                throw new GarageBookingException("No free bays available in garage: " + garage.getName());
            }

            garage.occupyBay();
            totalOrders++;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            log.info("Order created!");
            log.info("Order Date          : " + repairOrder.orderDate().format(formatter));
            log.info("Total Orders        : " + totalOrders);
            log.info("Free Bays Remaining : " + garage.getFreeBays());
            log.info("------------------------------------------------------------------------------");

            log.info("Mechanic Name       : " + repairOrder.mechanic().getName());
            log.info("Specialization      : " + repairOrder.mechanic().getSpecialization());
            log.info("Experience          : " + repairOrder.mechanic().getYearsOfExperience() + " years");
            log.info("Seniority level     : " + repairOrder.mechanic().getLevel().getDisplayName());
            log.info("Is mechanic senior level or above ? " + (repairOrder.mechanic().getLevel().isSeniorLevel() ? "- Yes" : "- No"));
            log.info("Mechanic Phone      : " + repairOrder.mechanic().getPhone());
            log.info("Hourly Rate         : " + repairOrder.mechanic().getHourlyRate() + " GEL/h");
            log.info("------------------------------------------------------------------------------");

            log.info("Customer Name       : " + repairOrder.customer().getName());
            log.info("Customer Age        : " + repairOrder.customer().getAge());
            log.info("Customer Phone      : " + repairOrder.customer().getPhone());
            log.info("Loyalty Points      : " + repairOrder.customer().getLoyaltyPoints());
            log.info("------------------------------------------------------------------------------");

            log.info("Vehicle             : " + repairOrder.getVehicleBrand() + " " + repairOrder.getVehicleModel());

            Vehicle vehicle = repairOrder.vehicle();
            if (vehicle instanceof Car car) {
                log.info("Engine Type         : " + car.getEngineType());
                log.info("Engine Size         : " + car.getEngineSize() + "L");
                log.info("Transmission        : " + car.getTransmission().type() + " | " + car.getTransmission().gears() + " gears");
            } else if (vehicle instanceof Motorcycle moto) {
                log.info("Engine Capacity     : " + moto.getEngineCapacity() + " cc");
                log.info("Bike Type           : " + moto.getBikeType());
            } else if (vehicle instanceof Truck truck) {
                log.info("Engine Size         : " + truck.getEngineSize() + "L");
                log.info("Doors & Tires       : " + truck.getDoors() + " / " + truck.getTires());
                log.info("Payload Capacity    : " + truck.getPayloadCapacityTons() + " tons");
                log.info("Sleeping Cabin      : " + (truck.hasSleepingCabin() ? "Yes" : "No"));
                log.info("Transmission        : " + truck.getTransmission().type() + " | " + truck.getTransmission().gears() + " gears");
            }
            log.info("------------------------------------------------------------------------------");

            log.info("Service             : " + repairOrder.service().getServiceName());
            log.info("Price               : " + repairOrder.service().getPrice() + " GEL");
            log.info("------------------------------------------------------------------------------");

            Integer pointsEarned = repairOrder.service().getPrice().intValue() / 10;
            repairOrder.customer().addLoyaltyPoints(pointsEarned);
            log.info("Loyalty points awarded: +" + pointsEarned + " | Total: " + repairOrder.customer().getLoyaltyPoints());
            log.info("------------------------------------------------------------------------------");

            // 1. Create Invoice
            InvoiceService invoice = new InvoiceService(totalOrders, repairOrder.customer(), repairOrder, new BigDecimal("10"));

            // 2. Create Payment with method only — no amount yet, Invoice will calculate and stamp it
            Payment payment = new Payment(totalOrders, PaymentMethod.CARD);

            // 3. Invoice receives Payment, calculates final total (discount + fee), sets final total amount, confirms
            invoice.addPayment(payment);

            // 4. Print full invoice
            invoice.generate();
        }
    }

    @Override
    public void close() {
        Integer occupied = garage.getOccupiedBays();
        if (occupied > 0) {
            garage.freeBay(occupied);
            log.info("------------------------------------------------------------------------------");
            log.info("BookingService closed. Released " + occupied + " occupied bay(s) in: " + garage.getName());
            log.info("------------------------------------------------------------------------------");
        } else {
            log.info("------------------------------------------------------------------------------");
            log.info("BookingService closed. All bays were already free in: " + garage.getName());
            log.info("------------------------------------------------------------------------------");
        }
    }

}