package com.solvd;

import com.solvd.autoservicerepair.annotations.Checker;
import com.solvd.autoservicerepair.annotations.Description;
import com.solvd.autoservicerepair.annotations.ServiceInfo;
import com.solvd.autoservicerepair.enums.*;
import com.solvd.autoservicerepair.exceptions.AppointmentStatusException;
import com.solvd.autoservicerepair.exceptions.GarageBookingException;
import com.solvd.autoservicerepair.functional.AppointmentFilter;
import com.solvd.autoservicerepair.functional.DiscountStrategy;
import com.solvd.autoservicerepair.functional.ObjectFormatter;
import com.solvd.autoservicerepair.interfaces.*;
import com.solvd.autoservicerepair.model.*;
import com.solvd.autoservicerepair.services.*;
import com.solvd.autoservicerepair.utils.FileReaderUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

@Slf4j
public class Main {

    public static void main(String[] args) {

        // --- Transmissions ---
        Transmission carTransmission = new Transmission(TransmissionType.AUTOMATIC, 8);
        Transmission truckTransmission = new Transmission(TransmissionType.MANUAL, 12);
        Transmission motoTransmission = new Transmission(TransmissionType.SEMI_AUTOMATIC, 6);

        // --- Insurance policies ---
        Insurance insurance1 = new Insurance("Aldagi", "POL-1001", LocalDate.of(2026, 12, 31), new BigDecimal("45.00"), InsuranceTier.STANDARD);
        Insurance insurance2 = new Insurance("GPI Holding", "POL-2042", LocalDate.of(2025, 6, 15), new BigDecimal("60.00"), InsuranceTier.PREMIUM);
        Insurance insurance3 = new Insurance("Irao", "POL-3300", LocalDate.of(2027, 3, 1), new BigDecimal("30.00"), InsuranceTier.BASIC);

        // --- Customers ---
        Customer customer1 = new Customer("Giorgi", "19207150012", "525-99-93-77", 25, insurance1, "ForGitHomework1@gmail.com");
        Customer customer2 = new Customer("Mariam", "28803220039", "599-11-22-33", 30, insurance2, "ForGitHomework2@gmail.com");
        Customer customer3 = new Customer("David", "30111050047", "577-44-55-66", 45, insurance3, "ForGitHomework3@gmail.com");

        // --- Mechanics ---
        Mechanic mechanic1 = new Mechanic("Nika", "01005078846", "599 10 15 35", "Engine Specialist", 12, MechanicSeniorityLevel.SENIOR, new BigDecimal("25.00"));
        Mechanic mechanic2 = new Mechanic("Luka", "03005057137", "577 22 33 44", "Brake & Suspension", 2, MechanicSeniorityLevel.JUNIOR, new BigDecimal("18.00"));
        Mechanic mechanic3 = new Mechanic("Gia", "01505027167", "527 55 23 14", "Transmission", 35, MechanicSeniorityLevel.MASTER, new BigDecimal("30.00"));

        // --- Services ---
        Service oilChange = new OilChangeService(new BigDecimal("50.00"));
        Service tireChange = new TireChange(new BigDecimal("100.00"));
        Service brakeRepair = new BrakeRepairService(new BigDecimal("200.00"));

        // --- MechanicShifts ---
        MechanicShift shift1 = new MechanicShift(mechanic1, LocalDate.now(), 8, 18);
        MechanicShift shift2 = new MechanicShift(mechanic2, LocalDate.now(), 9, 17);

        shift1.assignService(oilChange);
        shift2.assignService(brakeRepair);

        // --- Vehicles ---
        Car car = new Car("Toyota", "Camry", "JTNB11HK0L3000001", "GE-462-GE", 2027, 4, EngineType.HYBRID, 2.5, carTransmission);
        Motorcycle motorcycle = new Motorcycle("Kawasaki", "Ninja450", 2023, "JKAZXK8J0MA000001", "GE-417", EngineType.PETROL, 450, BikeType.SPORT, motoTransmission);
        Truck truck = new Truck("Volvo", "FH16", "VF6FJ2C0XLN000001", 2019, "GE-804-TR", 2, 10, EngineType.DIESEL, 16.1, 25.0, true, truckTransmission);

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

        // --- Repair Orders (First Generic)---
        RepairOrder<Car> repairOrder1 = new RepairOrder<>(customer1, mechanic1, car, oilChange, LocalDateTime.now());
        RepairOrder<Motorcycle> repairOrder2 = new RepairOrder<>(customer2, mechanic2, motorcycle, brakeRepair, LocalDateTime.now());
        RepairOrder<Truck> repairOrder3 = new RepairOrder<>(customer3, mechanic1, truck, tireChange, LocalDateTime.now());

        // --- Service Queue (waiting orders / Second Generic) ---
        ServiceQueue<RepairOrder<?>> serviceQueue = new ServiceQueue<>(5);

        // add orders to queue
        serviceQueue.add(repairOrder1);
        serviceQueue.add(repairOrder2);
        serviceQueue.add(repairOrder3);

        // display queue
        serviceQueue.displayQueue();

        // simulate processing next order
        RepairOrder<?> nextOrder = serviceQueue.poll();
        log.info("Processing next order: {}", nextOrder);

        // check remaining
        log.info("Orders left in queue: {}", serviceQueue.size());

        // --- Appointments ---
        AppointmentService appointment1 = new AppointmentService(1, customer1, mechanic1, car, LocalDateTime.now().plusHours(1));
        AppointmentService appointment2 = new AppointmentService(2, customer2, mechanic2, motorcycle, LocalDateTime.now().plusHours(2));

        try {
            appointment1.start();
//            appointment1.start(); To throw exception
            appointment1.complete();
            appointment2.start();
        } catch (AppointmentStatusException e) {
            log.error("Appointment error: {}", e.getMessage());
        } finally {
            log.info("Appointment processing finished");
            log.info("Appointment 1 status: {}", appointment1.getStatus());
            log.info("Appointment 2 status: {}", appointment2.getStatus());
            log.info("------------------------------------------------------------------------------");
        }

        // --- Garage: root object — fully populated ---

        List<Mechanic> listOfMechanics = new ArrayList<>();
        listOfMechanics.add(mechanic1);
        listOfMechanics.add(mechanic2);
        listOfMechanics.add(mechanic3);

        List<MechanicShift> listOfShifts = new ArrayList<>();
        listOfShifts.add(shift1);
        listOfShifts.add(shift2);

        Set<Customer> listOfCustomers = new HashSet<>();
        listOfCustomers.add(customer1);
        listOfCustomers.add(customer2);
        listOfCustomers.add(customer3);

        List<Vehicle> listOfVehicles = new ArrayList<>();
        listOfVehicles.add(car);
        listOfVehicles.add(motorcycle);
        listOfVehicles.add(truck);

        Map<String, SparePart> sparePartMap = new HashMap<>();
        sparePartMap.put(oilFilter.getProductNumber(), oilFilter);
        sparePartMap.put(brakeDisc.getProductNumber(), brakeDisc);
        sparePartMap.put(tirePatch.getProductNumber(), tirePatch);

        List<AppointmentService> listOfAppointments = new ArrayList<>();
        listOfAppointments.add(appointment1);
        listOfAppointments.add(appointment2);

        List<RepairOrder<?>> listOfRepairOrders = new ArrayList<>();
        listOfRepairOrders.add(repairOrder1);
        listOfRepairOrders.add(repairOrder2);
        listOfRepairOrders.add(repairOrder3);

        Garage garage = new Garage(
                "AutoFix Tbilisi",
                "Vake District, 14 Chavchavadze Ave",
                5,
                listOfMechanics,
                listOfShifts,
                listOfCustomers,
                listOfVehicles,
                sparePartMap,
                listOfAppointments,
                listOfRepairOrders
        );

        // --- BookingService uses Garage as root ---

        try (BookingService bookingService = new BookingService(garage)) {
            bookingService.createOrder(List.of(repairOrder1, repairOrder2, repairOrder3));
        } catch (GarageBookingException e) {
            log.error("Garage booking error: {}", e.getMessage());
        }

        log.info("Total orders processed: {}", BookingService.getTotalOrders());
        log.info("Garage: {} | Free bays: {}", garage.getName(), garage.getFreeBays());

        /* ********************************************************************************************* */

        // --- Collection method demonstrations ---

        log.info("------------------------------------------------------------------------------");

        // Iteration through List (mechanics) + print first element + remove + contains

        log.info("\nAll mechanics:");
        for (Mechanic mechanic : garage.getMechanics()) {
            log.info("- {} | {} | {}", mechanic.getName(), mechanic.getSpecialization(), mechanic.getYearsOfExperience());
        }
        /* List - with index */
        Mechanic firstMechanic = garage.getMechanics().get(0);
        log.info("\n- First mechanic: {}", firstMechanic.getName());

        /* remove */
        log.info("\n- Mechanics before remove: {}", garage.getMechanics().size());
        garage.removeMechanic(mechanic3);
        log.info("- Mechanics after remove: {}", garage.getMechanics().size());

        /* contains */
        log.info("- Has mechanic Nika: {}", garage.hasMechanic(mechanic1));

        garage.addMechanic(mechanic3);

        // Iteration through Set (customers) + print first element + remove + isEmpty

        log.info("\nAll customers:");
        for (Customer customer : garage.getCustomers()) {
            log.info("- {} | {} | {}", customer.getName(), customer.getPhone(), customer.getEmail());
        }

        /* Set — iterator (Sets have no index) */
        Customer firstCustomer = garage.getCustomers().iterator().next();
        log.info("\n- First customer: {}", firstCustomer.getName());

        /* remove */
        log.info("\n- Customers before remove: {}", garage.getCustomers().size());
        garage.removeCustomer(customer3);
        log.info("- Customers after remove: {}", garage.getCustomers().size());

        /* isEmpty ? */
        log.info("- Has customers: {}", garage.hasCustomers());

        // Iterate through Map (spareParts) + put (add a sparePart) + get (sparePart with key-->value pair)

        log.info("\nAll spare parts:");
        for (Map.Entry<String, SparePart> entry : garage.getSpareParts().entrySet()) {
            log.info("- {} | {} | quantity: {}", entry.getKey(), entry.getValue().getProductName(), entry.getValue().getQuantity());
        }
        /* Map — first entry via entrySet iterator */
        SparePart firstPart = garage.getSpareParts().entrySet().iterator().next().getValue();
        log.info("\n- First spare part: {}", firstPart.getProductName());

        /* put */
        log.info("\n- Spare parts before put: {}", garage.getSpareParts().size());
        SparePart sparkPlug = new SparePart("Spark Plug", "SP-1122", new BigDecimal("8.00"), 15);
        garage.addSparePart(sparkPlug);
        log.info("- Spare parts after put: {}", garage.getSpareParts().size());

        /* get */
        log.info("- Spare part lookup: {}", garage.getSparePartByNumber("OF-4521").getProductName());

        // Iteration through List (vehicles) + print first element + remove
        Vehicle firstVehicle = garage.getVehicles().get(0);
        log.info("\n- First vehicle: {} {}", firstVehicle.getBrand(), firstVehicle.getModel());
        log.info("\nAll vehicles: ");
        for (Vehicle vehicle : garage.getVehicles()) {
            log.info("- {} | {} | {} | {}", vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), vehicle.getVin());
        }
        log.info("\n- Total vehicles before remove: {}", garage.getTotalVehicles());
        garage.removeVehicle(truck);
        log.info("- Vehicles after remove: {}", garage.getTotalVehicles());

        /* ************************************************************************************************************ */

        // ---  java.util.function LAMBDA DEMONSTRATIONS  lambda = '->' = parameter -> action-- //

        log.info("\n------------------------------------------------------------------------------");
        log.info("  java.util.function LAMBDA DEMONSTRATIONS");
        log.info("------------------------------------------------------------------------------");

        // 1) PREDICATE <Vehicle> - Filters the garage's vehicle list by a given condition. //

        log.info("\n--- 1. Predicate<Vehicle>: vehicles from 2018 or newer ---");

        Predicate<Vehicle> isModernVehicleCondition = vehicle -> vehicle.getYear() >= 2018;

        List<Vehicle> modernVehicles = garage.filterVehicles(isModernVehicleCondition);
        modernVehicles.forEach(vehicle ->
                log.info("  {} {} ({})", vehicle.getBrand(), vehicle.getModel(), vehicle.getYear())
        );

        /* second condition */

        Predicate<Vehicle> isNotMotorcycleCondition = vehicle -> !(vehicle instanceof Motorcycle);

        /* 2 conditions with .and */
        List<Vehicle> modernNonMotorcycles = garage.filterVehicles(isModernVehicleCondition.and(isNotMotorcycleCondition));

        log.info("  Modern non-motorcycles:");
        modernNonMotorcycles.forEach(v ->
                log.info("  {} {}", v.getBrand(), v.getModel())
        );

        // 2) Function<Mechanic, String> Transforms each mechanic into a formatted roster String

        log.info("\n--- 2. Function<Mechanic, String>: formatted mechanic roster ---");

        Function<Mechanic, String> formatter = mechanic ->
                String.format("%-10s | %-20s | %d yrs exp | %.2f GEL/h",
                        mechanic.getName(),
                        mechanic.getSpecialization(),
                        mechanic.getYearsOfExperience(),
                        mechanic.getHourlyRate());

        List<String> roster = garage.getMechanicRoster(formatter);

        roster.forEach(line -> log.info("  {}", line));

        // 3) BiFunction<String, Integer, List<SparePart>>

        /* finds spare parts whose product number starts with a given prefix
            and whose quantity is below a minimum threshold,
            useful for finding what is low in stock. */

        log.info("\n--- 3. BiFunction<String, Integer, List<SparePart>>: low in stock---");

        BiFunction<String, Integer, List<SparePart>> lowStockChecker = (prefix, minQty)
                -> garage.getSpareParts().values().stream()
                .filter(part -> part.getProductNumber().startsWith(prefix) && part.getQuantity() < minQty)
                .collect(Collectors.toList());

        List<SparePart> lowStock = garage.getLowStockParts(lowStockChecker, "BD", 5);
        if (lowStock.isEmpty()) {
            log.info("  No low-stock parts found for prefix 'BD'.");
        } else {
            lowStock.forEach(part ->
                    log.info("  LOW STOCK: {} ({}) — qty: {}", part.getProductName(), part.getProductNumber(), part.getQuantity())
            );
        }

        // check all parts below quantity 10 (no prefix filter needed, empty prefix)
        BiFunction<String, Integer, List<SparePart>> lowInStock = (ignored, minQty) ->
                garage.getSpareParts().values().stream()
                        .filter(part -> part.getQuantity() < minQty)
                        .collect(Collectors.toList());

        log.info("  All parts with qty < 10:");
        garage.getLowStockParts(lowInStock, "", 10).forEach(part ->
                log.info("  - {} | qty: {}", part.getProductName(), part.getQuantity())
        );

        // 4. Consumer<Customer> takes an object, does something return nothing

        log.info("\n--- 4. Consumer<Customer>: award 10 bonus loyalty points to all ---");

        Consumer<Customer> awardBonusPoints = customer -> {
            customer.addLoyaltyPoints(10);
            log.info("  Awarded 10 pts to {} | Total: {}", customer.getName(), customer.getLoyaltyPoints());
        };

        garage.forEachCustomer(awardBonusPoints);

        // 5. UnaryOperator <T> - takes certain type changes value returns the same type + Predicate Yes/No logic

        log.info("\n--- 5. UnaryOperator<BigDecimal>: 20% senior bonus for mechanics with 10+ years ---");

        Predicate<Mechanic> isSenior = mechanic -> mechanic.getYearsOfExperience() >= 10;
        UnaryOperator<BigDecimal> seniorBonus = rate -> rate.multiply(new BigDecimal("1.20"));

        log.info("  Before:");
        garage.getMechanics().forEach(n ->
                System.out.printf("  %s | %.2f GEL/h%n", n.getName(), n.getHourlyRate())
        );

        garage.applyRateAdjustment(isSenior, seniorBonus);

        log.info("  After:");
        garage.getMechanics().forEach(n ->
                System.out.printf("  %s | %.2f GEL/h%n", n.getName(), n.getHourlyRate())
        );

        // 6. Runnable - takes nothing, returns nothing, just runs a task
        log.info("\n--- Runnable: print garage status ---");
        Runnable garageStatusPrinter = () -> {
            log.info("Garage: {}", garage.getName());
            log.info("Free bays: {}", garage.getFreeBays());
            log.info("Total vehicles: {}", garage.getTotalVehicles());
        };
        garageStatusPrinter.run();

        // 7. Supplier - takes nothing, returns something
        log.info("\n--- Supplier: get first customer name ---");
        Supplier<String> firstCustomerName = () -> garage.getCustomers().iterator().next().getName();
        log.info("First customer: {}", firstCustomerName.get());

        // --- Custom Functional Interfaces just to showcase ---

        // 1. DiscountStrategy
        DiscountStrategy thirtyPercentDiscount = price -> price * 0.7;
        BigDecimal discounted = new BigDecimal(thirtyPercentDiscount.apply(200));
        log.info("\nDiscounted price: {}", discounted);

        // 2. AppointmentFilter
        AppointmentFilter onlyScheduled = a -> a.getStatus() == ServiceStatus.SCHEDULED;
        log.info("Is appointment scheduled? | {}", onlyScheduled.test(appointment1) ? "Yes" : "No");

        // 3. ObjectFormatter
        ObjectFormatter<AppointmentService> objectFormatter = appointment -> appointment.getCustomer().getName()
                + " | " + appointment.getStatus();
        log.info("Appointment with customer: {}", objectFormatter.format(appointment1));



        /* ************************************************************************************************************ */

        // ---  java.util.Collection.stream additional streams to showcase --- //

        /* 1 getHighestPaidMechanic */
        garage.getHighestPaidMechanic()
                .ifPresent(mechanic -> log.info("\nHighest paid mechanic: {} | {} GEL/h", mechanic.getName(), mechanic.getHourlyRate()));

        /* 2 countVehiclesByClassType */
        log.info("\nCars in garage: {}", garage.countVehiclesByClassType(Car.class));
        log.info("Trucks in garage: {}", garage.countVehiclesByClassType(Truck.class));
        log.info("Motorcycles in garage: {}", garage.countVehiclesByClassType(Motorcycle.class));

        /* 3 getCustomersWithExpiredInsurance */
        List<Customer> expiredInsurance = garage.getCustomersWithExpiredInsurance();
        Optional.of(expiredInsurance)
                .filter(list -> !list.isEmpty()) // needed because if still is empty ifPresent checks only for Null and never reaches second sys.out
                .ifPresentOrElse(
                        list -> list.forEach(customer -> log.info("\nExpired insurance: {}", customer.getName())),
                        () -> log.info("\nNo customers with expired insurance.")
                );

        /* 4 hasMasterMechanic  - .anymatch() condition  with enum SENIOR */
        log.info("\nHas master mechanic: {}", garage.hasMasterMechanic());

        /* 5 getTotalSparePartsValue */
        log.info("\nTotal spare parts value: {} GEL\n", garage.getTotalSparePartsValue());

        /* getAppointmentsSortedByTime */
        garage.getAppointmentsSortedByTime()
                .forEach(appointment -> log.info("Appointment: {} | {} | {}", appointment.getCustomer().getName(), appointment.getFormattedScheduledTime(), appointment.getStatus().getDisplayName()));




        /* ********************************************************************************************* */

        // --- "Reflection" Demonstration from - Java Application programming interface (API) java.lang.reflect --- //

        try {
            Class<?> carClass = Car.class;

            /* 1. Class info */
            log.info("\n--- CLASS INFO ---");
            log.info("Class name  : {}", carClass.getName());
            log.info("Superclass  : {}", carClass.getSuperclass().getSimpleName());
            System.out.print("Interfaces  : ");
            for (Class<?> implementedInterface : carClass.getInterfaces()) {
                System.out.print(implementedInterface.getSimpleName() + " ");
            }
            log.info("");

            /* 2. Declared fields */
            log.info("\n--- FIELDS (declared in Car) ---");
            for (Field field : carClass.getDeclaredFields()) {
                System.out.printf("  [%s] %s %s%n",
                        Modifier.toString(field.getModifiers()),
                        field.getType().getSimpleName(),
                        field.getName());
            }


            /* 3. Constructors */
            log.info("\n--- CONSTRUCTORS ---");
            for (Constructor<?> declaredConstructors : carClass.getDeclaredConstructors()) {
                System.out.print("  " + declaredConstructors.getName() + "(");
                Parameter[] parameters = declaredConstructors.getParameters();
                for (Parameter parameter : parameters) {
                    System.out.print(parameter.getType().getSimpleName() + " ");
                }
                log.info(")");
            }

            /* 4. Declared methods */
            log.info("\n--- METHODS (declared in Car) ---");
            for (Method method : carClass.getDeclaredMethods()) {
                System.out.printf("  [%s] %s %s(%s)%n",
                        Modifier.toString(method.getModifiers()),
                        method.getReturnType().getSimpleName(),
                        method.getName(),
                        method.getParameterCount() > 0 ? "..." : "");
            }

            /* 5. Create a NEW Car instance via Constructor reflection */

            log.info("\n---CREATING CAR INSTANCE VIA REFLECTION ---");
            Transmission reflectTransmission = new Transmission(TransmissionType.AUTOMATIC, 6);
            Constructor<?> reflectCarConstructor = carClass.getDeclaredConstructor(
                    String.class, String.class, String.class, String.class,
                    Integer.class, Integer.class, EngineType.class,
                    Double.class, Transmission.class
            );
            Object reflectedCar = reflectCarConstructor.newInstance(
                    "Honda", "Civic", "VIN-REFLECT-001", "GE-REF-01",
                    2024, 4, EngineType.PETROL, 1.5, reflectTransmission
            );
            log.info("Created via reflection: {}", reflectedCar);

            /* 6. Read a private field value via reflection */
            log.info("\n--- READING PRIVATE FIELD VALUE ---");
            Field doorsField = carClass.getDeclaredField("doors");
            doorsField.setAccessible(true);
            log.info("doors field value: {}", doorsField.get(reflectedCar));

            /* 7. Invoke a method via reflection */
            log.info("\n--- INVOKING drive() VIA REFLECTION ---");
            Method driveMethod = carClass.getDeclaredMethod("drive");
            driveMethod.setAccessible(true);
            driveMethod.invoke(reflectedCar);

            /* 8. Find and invoke @ServiceInfo, Description, Checker annotated methods */
            log.info("\n--- @ServiceInfo, Description, Checker ANNOTATED METHODS ---");
            for (Method method : carClass.getDeclaredMethods()) {

                String methodName = method.getName();
                String description = null;
                String extra = null;

                if (method.isAnnotationPresent(ServiceInfo.class)) {
                    ServiceInfo info = method.getAnnotation(ServiceInfo.class);
                    description = info.description();
                    extra = "  Safety : " + info.isSafetyCheck();
                } else if (method.isAnnotationPresent(Description.class)) {
                    description = method.getAnnotation(Description.class).description();
                } else if (method.isAnnotationPresent(Checker.class)) {
                    description = method.getAnnotation(Checker.class).description();
                }

                if (description != null) {
                    log.info("  Method : {}", methodName);
                    log.info("  Description : {}", description);
                    if (extra != null) log.info(extra);
                    if (method.getParameterCount() == 0) {
                        log.info("  → Invoking...");
                        method.invoke(reflectedCar);
                    }
                    log.info("");
                }
            }

        } catch (Exception e) {
            log.error("Reflection Error: {}", e.getMessage());
        }

        FileReaderUtil.readFile("IHNMSIMS", "logs/output.txt");

    }


    // --- Helper methods using interfaces as parameters --- (Drivable; Rideable; Maintainable; Inspectable; Sellable)

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
        log.info("{} selling price: {}", name, sparePart.getSellingPrice());
    }

}



