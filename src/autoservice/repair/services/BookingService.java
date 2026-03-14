package autoservice.repair.services;

import autoservice.repair.model.Car;
import autoservice.repair.model.Garage;
import autoservice.repair.model.Motorcycle;
import autoservice.repair.model.Truck;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class BookingService {

    private static int totalOrders;
    private final Garage garage;

    static {
        totalOrders = 0;
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Auto Repair System Started");
        System.out.println("------------------------------------------------------------------------------");
    }

    public BookingService(Garage garage) {
        this.garage = garage;
    }

    public static int getTotalOrders() {
        return totalOrders;
    }

    public Garage getGarage() {
        return garage;
    }

    public void createOrder(RepairOrder repairOrder) {
        if (!garage.hasFreeBay()) {
            System.out.println("Cannot create order: no free bays in garage " + garage.getName());
            return;
        }

        garage.occupyBay();
        totalOrders++;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("Order created!");
        System.out.println("Order Date          : " + repairOrder.getOrderDate().format(formatter));
        System.out.println("Total Orders        : " + totalOrders);
        System.out.println("Free Bays Remaining : " + garage.getFreeBays());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Mechanic Name       : " + repairOrder.getMechanic().getName());
        System.out.println("Specialization      : " + repairOrder.getMechanic().getSpecialization());
        System.out.println("Experience          :" + repairOrder.getMechanic().getYearsOfExperience() + " years");
        System.out.println("Mechanic Phone      : " + repairOrder.getMechanic().getPhone());
        System.out.println("Hourly Rate         : " + repairOrder.getMechanic().getHourlyRate() + " GEL/h");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Customer Name       : " + repairOrder.getCustomer().getName());
        System.out.println("Customer Age        : " + repairOrder.getCustomer().getAge());
        System.out.println("Customer Phone      : " + repairOrder.getCustomer().getPhone());
        System.out.println("Loyalty Points      : " + repairOrder.getCustomer().getLoyaltyPoints());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Vehicle             : " + repairOrder.getVehicleBrand() + " " + repairOrder.getVehicleModel());

        if (repairOrder.getCar() != null) {
            Car car = repairOrder.getCar();
            System.out.println("Engine Type         : " + car.getEngineType());
            System.out.println("Engine Size         : " + car.getEngineSize() + "L");
            System.out.println("Transmission        : " + car.getTransmission().getType()
                    + " | " + car.getTransmission().getGears() + " gears");
        } else if (repairOrder.getMotorcycle() != null) {
            Motorcycle moto = repairOrder.getMotorcycle();
            System.out.println("Engine Capacity     : " + moto.getEngineCapacity() + " cc");
            System.out.println("Bike Type           : " + moto.getBikeType());

        } else if (repairOrder.getTruck() != null) {
            Truck truck = repairOrder.getTruck();
            System.out.println("Engine Size         : " + truck.getEngineSize() + "L");
            System.out.println("Doors / Tires: " + truck.getDoors() + " / " + truck.getTires());
            System.out.println("Payload Capacity    : " + truck.getPayloadCapacityTons() + " tons");
            System.out.println("Sleeping Cabin      : " + truck.hasSleepingCabin());
            System.out.println("Transmission        : " + truck.getTransmission().getType()
                    + " | " + truck.getTransmission().getGears() + " gears");
        }

        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Service             : " + repairOrder.getService().getServiceName());
        System.out.println("Price               : " + repairOrder.getService().getPrice() + " GEL");
        System.out.println("------------------------------------------------------------------------------");

        // Calculating addloyaltypoints parameter it will be 10%
        int pointsEarned = repairOrder.getService().getPrice().intValue()
                - ((repairOrder.getService().getPrice().intValue() / 100) * 90);


        repairOrder.getCustomer().addLoyaltyPoints(pointsEarned);
        System.out.println("Loyalty points awarded: +" + pointsEarned
                + " | Total: " + repairOrder.getCustomer().getLoyaltyPoints());
        System.out.println("------------------------------------------------------------------------------");

        Invoice invoice = new Invoice(repairOrder.getCustomer(), repairOrder, new BigDecimal("10"));
        Payment payment = new Payment(invoice.calculateTotal(), "CARD");
        invoice.addPayment(payment);
        invoice.generate();

        garage.freeBay();

    }

}
