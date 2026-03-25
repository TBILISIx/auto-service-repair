import autoservice.repair.exceptions.AppointmentStatusException;
import autoservice.repair.exceptions.GarageBookingException;
import autoservice.repair.model.*;
import autoservice.repair.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
        Customer customer1 = new Customer("Giorgi", "19207150012", "525-99-93-77", 25, insurance1, "ForGitHomework1@gmail.com");
        Customer customer2 = new Customer("Mariam", "28803220039", "599-11-22-33", 30, insurance2, "ForGitHomework2@gmail.com");
        Customer customer3 = new Customer("David", "30111050047", "577-44-55-66", 40, insurance3, "ForGitHomework3@gmail.com");

        // --- Mechanics ---
        Mechanic mechanic1 = new Mechanic("Nika", "01005078846", "599 10 15 35", "Engine Specialist", 12, new BigDecimal("25.00"));
        Mechanic mechanic2 = new Mechanic("Luka", "03005057137", "577 22 33 44", "Brake & Suspension", 5, new BigDecimal("18.00"));

        // --- Services ---
        Service oilChange = new OilChange(new BigDecimal("50.00"));
        Service tireChange = new TireChange(new BigDecimal("100.00"));
        Service brakeRepair = new BrakeRepair(new BigDecimal("200.00"));

        // --- MechanicShifts ---
        MechanicShift shift1 = new MechanicShift(mechanic1, LocalDate.now(), 8, 18);
        MechanicShift shift2 = new MechanicShift(mechanic2, LocalDate.now(), 9, 17);

        shift1.assignService(oilChange);
        shift2.assignService(brakeRepair);

        // --- Vehicles ---
        Car car = new Car("Toyota", "Camry", "JTNB11HK0L3000001", "GE-462-GE", 2027, 4, "Hybrid", 2.5, carTransmission);
        Motorcycle motorcycle = new Motorcycle("Kawasaki", "Ninja450", 2017, "JKAZXK8J0MA000001", "GE-417", 450, "Sport");
        Truck truck = new Truck("Volvo", "FH16", "GE-804-TR", 2019, "VF6FJ2C0XLN000001", 2, 10, 16.1, 25.0, true, truckTransmission);

        // --- Interface Polymorphism examples ---

        // --- Using methods (polymorphism via parameter) --- (Drivable; Maintainable; Inspectable)

        testDrive(car);
        testDrive(truck);
        testRide(motorcycle);

        performMaintenance(car);
        performMaintenance(truck);
        performMaintenance(motorcycle);

        performInspection(car);
        performInspection(truck);
        performInspection(motorcycle);

        // --- Spare Parts ---
        SparePart oilFilter = new SparePart("Oil Filter", "OF-4521", new BigDecimal("12.50"), 10);
        SparePart brakeDisc = new SparePart("Brake Disc", "BD-9900", new BigDecimal("85.00"), 4);
        SparePart tirePatch = new SparePart("Tire Patch Kit", "TP-0011", new BigDecimal("5.00"), 20);

        oilFilter.useOne();
        brakeDisc.useOne();
        tirePatch.useOne();

        // -- Polymorphism --> same method different objects as parameters (Sellable)
        getSellingPrice("Oil filter", oilFilter);
        getSellingPrice("Brake disk", brakeDisc);
        getSellingPrice("Tire patch", tirePatch);

        // --- Repair Orders ---
        RepairOrder repairOrder1 = new RepairOrder(customer1, mechanic1, car, oilChange, LocalDateTime.now());
        RepairOrder repairOrder2 = new RepairOrder(customer2, mechanic2, motorcycle, brakeRepair, LocalDateTime.now());
        RepairOrder repairOrder3 = new RepairOrder(customer3, mechanic1, truck, tireChange, LocalDateTime.now());

        // --- Appointments ---
        Appointment appointment1 = new Appointment(1, customer1, mechanic1, car, LocalDateTime.now().plusHours(1));
        Appointment appointment2 = new Appointment(2, customer2, mechanic2, motorcycle, LocalDateTime.now().plusHours(2));

        try {
            appointment1.start();
//            appointment1.start(); To throw exception
            appointment1.complete();
            appointment2.start();
        } catch (AppointmentStatusException e) {
            System.out.println("Appointment error: " + e.getMessage());
        } finally {
            System.out.println("Appointment processing finished");
            System.out.println("Appointment 1 status: " + appointment1.getStatus());
            System.out.println("Appointment 2 status: " + appointment2.getStatus());
            System.out.println("------------------------------------------------------------------------------");
        }

        // --- Garage: root object — fully populated ---

        Garage garage = new Garage(
                "AutoFix Tbilisi",
                "Vake District, 14 Chavchavadze Ave",
                5,

                new ArrayList<>(List.of(mechanic1, mechanic2)),
                new ArrayList<>(List.of(shift1, shift2)),

                new HashSet<>(Set.of(customer1, customer2, customer3)),

                new ArrayList<>(List.of(car, motorcycle, truck)),

                new HashMap<>(Map.of(
                        oilFilter.getProductNumber(), oilFilter,
                        brakeDisc.getProductNumber(), brakeDisc,
                        tirePatch.getProductNumber(), tirePatch)
                ),

                new ArrayList<>(List.of(appointment1, appointment2)),
                new ArrayList<>(List.of(repairOrder1, repairOrder2, repairOrder3))
        );

        // --- BookingService uses Garage as root ---

        try (BookingService bookingService = new BookingService(garage)) {
            bookingService.createOrder(List.of(repairOrder1, repairOrder2, repairOrder3));
        } catch (GarageBookingException e) {
            System.out.println("Garage booking error: " + e.getMessage());
        }

        System.out.println("Total orders processed: " + BookingService.getTotalOrders());
        System.out.println("Garage: " + garage.getName() + " | Free bays: " + garage.getFreeBays());
    }

    // --- Helper methods using interfaces as parameters --- ( Drivable ; Rideable ; Maintainable; Inspectable; Sellable)

    public static void testDrive(Drivable vehicle) {
        vehicle.drive();
    }
    public static void testRide(Rideable vehicle) {
        vehicle.ride();
    }
    public static void performMaintenance(Maintainable vehicle) {
        vehicle.performMaintenance();
    }
    public static void performInspection(Inspectable vehicle) {
        vehicle.performInspection();
    }
    public static void getSellingPrice(String name, Sellable sparePart) {
        System.out.println(name + " selling price: " + sparePart.getSellingPrice());
    }

}