import autoservice.repair.model.*;
import autoservice.repair.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

class Main {

    public static void main(String[] args) {

        // --- Transmissions ---
        Transmission carTransmission = new Transmission("Automatic", 8);
        Transmission truckTransmission = new Transmission("Manual", 12);

        // --- Insurance policies ---
        Insurance insurance1 = new Insurance("Aldagi", "POL-1001", LocalDate.of(2026, 12, 31), new BigDecimal("45.00"));
        Insurance insurance2 = new Insurance("GPI Holding", "POL-2042", LocalDate.of(2025, 6, 15), new BigDecimal("60.00"));
        Insurance insurance3 = new Insurance("Irao", "POL-3300", LocalDate.of(2027, 3, 1), new BigDecimal("30.00"));

        // --- Customers ---
        Customer customer1 = new Customer("Giorgi", 25, "525-99-93-77", insurance1);
        Customer customer2 = new Customer("Mariam", 30, "599-11-22-33", insurance2);
        Customer customer3 = new Customer("David", 40, "577-44-55-66", insurance3);

        // --- Mechanics ---
        Mechanic mechanic1 = new Mechanic("Nika", "599 10 15 35", "Engine Specialist", 12, new BigDecimal("25.00"));
        Mechanic mechanic2 = new Mechanic("Luka", "577 22 33 44", "Brake & Suspension", 5, new BigDecimal("18.00"));

        // --- MechanicShifts ---
        MechanicShift shift1 = new MechanicShift(mechanic1, LocalDate.now(), 8, 18);
        MechanicShift shift2 = new MechanicShift(mechanic2, LocalDate.now(), 9, 17);

        shift1.assignOrder();
        shift2.assignOrder();

        // --- Vehicles ---
        Car car = new Car("Toyota", "Camry", 4, 4, "Hybrid", 2.5, carTransmission);
        Motorcycle motorcycle = new Motorcycle("Kawasaki", "Ninja450", 450, "Sport");
        Truck truck = new Truck("Volvo", "FH16", 2, 10, 16.1, 25.0, true, truckTransmission);

        // --- Services ---
        Service oilChange = new Service("Oil Change", new BigDecimal("50.00"));
        Service tireChange = new Service("Tire Change", new BigDecimal("100.00"));
        Service brakeRepair = new Service("Brake Repair", new BigDecimal("200.00"));

        // --- Spare Parts ---
        SparePart oilFilter = new SparePart("Oil Filter", "OF-4521", new BigDecimal("12.50"), 10);
        SparePart brakeDisc = new SparePart("Brake Disc", "BD-9900", new BigDecimal("85.00"), 4);
        SparePart tirePatch = new SparePart("Tire Patch Kit", "TP-0011", new BigDecimal("5.00"), 20);

        oilFilter.useOne();
        brakeDisc.useOne();
        tirePatch.useOne();

        // --- Repair Orders ---
        RepairOrder repairOrder1 = new RepairOrder(customer1, mechanic1, car, oilChange, LocalDateTime.now());
        RepairOrder repairOrder2 = new RepairOrder(customer2, mechanic2, motorcycle, brakeRepair, LocalDateTime.now());
        RepairOrder repairOrder3 = new RepairOrder(customer3, mechanic1, truck, tireChange, LocalDateTime.now());

        // --- Appointments ---
        Appointment appointment1 = new Appointment(customer1, mechanic1, car, LocalDateTime.now().plusHours(1));
        Appointment appointment2 = new Appointment(customer2, mechanic2, motorcycle, LocalDateTime.now().plusHours(2));

        appointment1.start();
        appointment1.complete();
        appointment2.start();

        System.out.println("Appointment 1 status: " + appointment1.getStatus());
        System.out.println("Appointment 2 status: " + appointment2.getStatus());
        System.out.println("------------------------------------------------------------------------------");

        // --- Garage: root object — fully populated ---
        Garage garage = new Garage(
                "AutoFix Tbilisi",
                "Vake District, 14 Chavchavadze Ave",
                5,
                new Mechanic[]{mechanic1, mechanic2},
                new MechanicShift[]{shift1, shift2},
                new Customer[]{customer1, customer2, customer3},
                new Car[]{car},
                new Motorcycle[]{motorcycle},
                new Truck[]{truck},
                new SparePart[]{oilFilter, brakeDisc, tirePatch},
                new Appointment[]{appointment1, appointment2},
                new RepairOrder[]{repairOrder1, repairOrder2, repairOrder3}
        );

        // --- BookingService uses Garage as root ---
        BookingService bookingService = new BookingService(garage);

        bookingService.createOrder(repairOrder1);
        bookingService.createOrder(repairOrder2);
        bookingService.createOrder(repairOrder3);

        System.out.println("Total orders processed: " + BookingService.getTotalOrders());
        System.out.println("Garage: " + garage.getName() + " | Free bays: " + garage.getFreeBays());
    }

}