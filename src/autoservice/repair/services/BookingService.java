package autoservice.repair.services;

import autoservice.repair.enums.PaymentMethod;
import autoservice.repair.exceptions.GarageBookingException;
import autoservice.repair.model.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record BookingService(Garage garage) implements AutoCloseable {

    private static Integer totalOrders;
    static {
        totalOrders = 0;
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Auto Repair System Started");
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

            System.out.println("Order created!");
            System.out.println("Order Date          : " + repairOrder.orderDate().format(formatter));
            System.out.println("Total Orders        : " + totalOrders);
            System.out.println("Free Bays Remaining : " + garage.getFreeBays());
            System.out.println("------------------------------------------------------------------------------");

            System.out.println("Mechanic Name       : " + repairOrder.mechanic().getName());
            System.out.println("Specialization      : " + repairOrder.mechanic().getSpecialization());
            System.out.println("Experience          : " + repairOrder.mechanic().getYearsOfExperience() + " years");
            System.out.println("Seniority level     : " + repairOrder.mechanic().getLevel().getDisplayName());
            System.out.println("Is mechanic senior level or above ? " + (repairOrder.mechanic().getLevel().isSeniorLevel() ? "- Yes" : "- No"));
            System.out.println("Mechanic Phone      : " + repairOrder.mechanic().getPhone());
            System.out.println("Hourly Rate         : " + repairOrder.mechanic().getHourlyRate() + " GEL/h");
            System.out.println("------------------------------------------------------------------------------");

            System.out.println("Customer Name       : " + repairOrder.customer().getName());
            System.out.println("Customer Age        : " + repairOrder.customer().getAge());
            System.out.println("Customer Phone      : " + repairOrder.customer().getPhone());
            System.out.println("Loyalty Points      : " + repairOrder.customer().getLoyaltyPoints());
            System.out.println("------------------------------------------------------------------------------");

            System.out.println("Vehicle             : " + repairOrder.getVehicleBrand() + " " + repairOrder.getVehicleModel());

            Vehicle vehicle = repairOrder.vehicle();
            if (vehicle instanceof Car car) {
                System.out.println("Engine Type         : " + car.getEngineType());
                System.out.println("Engine Size         : " + car.getEngineSize() + "L");
                System.out.println("Transmission        : " + car.getTransmission().type() + " | " + car.getTransmission().gears() + " gears");
            } else if (vehicle instanceof Motorcycle moto) {
                System.out.println("Engine Capacity     : " + moto.getEngineCapacity() + " cc");
                System.out.println("Bike Type           : " + moto.getBikeType());
            } else if (vehicle instanceof Truck truck) {
                System.out.println("Engine Size         : " + truck.getEngineSize() + "L");
                System.out.println("Doors & Tires       : " + truck.getDoors() + " / " + truck.getTires());
                System.out.println("Payload Capacity    : " + truck.getPayloadCapacityTons() + " tons");
                System.out.println("Sleeping Cabin      : " + (truck.hasSleepingCabin() ? "Yes" : "No"));
                System.out.println("Transmission        : " + truck.getTransmission().type() + " | " + truck.getTransmission().gears() + " gears");
            }
            System.out.println("------------------------------------------------------------------------------");

            System.out.println("Service             : " + repairOrder.service().getServiceName());
            System.out.println("Price               : " + repairOrder.service().getPrice() + " GEL");
            System.out.println("------------------------------------------------------------------------------");

            Integer pointsEarned = repairOrder.service().getPrice().intValue() / 10;
            repairOrder.customer().addLoyaltyPoints(pointsEarned);
            System.out.println("Loyalty points awarded: +" + pointsEarned + " | Total: " + repairOrder.customer().getLoyaltyPoints());
            System.out.println("------------------------------------------------------------------------------");

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
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("BookingService closed. Released " + occupied + " occupied bay(s) in: " + garage.getName());
            System.out.println("------------------------------------------------------------------------------");
        } else {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("BookingService closed. All bays were already free in: " + garage.getName());
            System.out.println("------------------------------------------------------------------------------");
        }
    }

}