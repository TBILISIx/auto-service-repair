import autoservice.repair.enums.*;
import autoservice.repair.exceptions.AppointmentStatusException;
import autoservice.repair.exceptions.GarageBookingException;
import autoservice.repair.functional.AppointmentFilter;
import autoservice.repair.functional.DiscountStrategy;
import autoservice.repair.functional.ObjectFormatter;
import autoservice.repair.model.*;
import autoservice.repair.services.*;

void main() {

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
    Service oilChange = new OilChange(new BigDecimal("50.00"));
    Service tireChange = new TireChange(new BigDecimal("100.00"));
    Service brakeRepair = new BrakeRepair(new BigDecimal("200.00"));

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
    System.out.println("Processing next order: " + nextOrder);

    // check remaining
    System.out.println("Orders left in queue: " + serviceQueue.size());

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

    Map<String, SparePart> productMap = new HashMap<>();
    productMap.put(oilFilter.getProductNumber(), oilFilter);
    productMap.put(brakeDisc.getProductNumber(), brakeDisc);
    productMap.put(tirePatch.getProductNumber(), tirePatch);

    List<Appointment> listOfAppointments = new ArrayList<>();
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
            productMap,
            listOfAppointments,
            listOfRepairOrders
    );

    // --- BookingService uses Garage as root ---

    try (BookingService bookingService = new BookingService(garage)) {
        bookingService.createOrder(List.of(repairOrder1, repairOrder2, repairOrder3));
    } catch (GarageBookingException e) {
        System.out.println("Garage booking error: " + e.getMessage());
    }

    System.out.println("Total orders processed: " + BookingService.getTotalOrders());
    System.out.println("Garage: " + garage.getName() + " | Free bays: " + garage.getFreeBays());

    /* ********************************************************************************************* */

    // --- Collection method demonstrations ---

    System.out.println("------------------------------------------------------------------------------");

    // Iteration through List (mechanics) + print first element + remove + contains

    System.out.println("\nAll mechanics:");
    for (Mechanic mechanic : garage.getMechanics()) {
        System.out.println("- " + mechanic.getName()
                + " | " + mechanic.getSpecialization()
                + " | " + mechanic.getYearsOfExperience());
    }
    /* List - with index */
    Mechanic firstMechanic = garage.getMechanics().get(0);
    System.out.println("\n- First mechanic: " + firstMechanic.getName());
    /* remove */
    System.out.println("\n- Mechanics before remove: " + garage.getMechanics().size());
    garage.removeMechanic(mechanic3);
    System.out.println("- Mechanics after remove: " + garage.getMechanics().size());
    /* contains */
    System.out.println("- Has mechanic Nika: " + garage.hasMechanic(mechanic1));

    garage.addMechanic(mechanic3);

    // Iteration through Set (customers) + print first element + remove + isEmpty

    System.out.println("\nAll customers:");
    for (Customer customer : garage.getCustomers()) {
        System.out.println("- " + customer.getName()
                + " | " + customer.getPhone()
                + " | " + customer.getEmail());
    }
    /* Set — iterator (Sets have no index) */
    Customer firstCustomer = garage.getCustomers().iterator().next();
    System.out.println("\n- First customer: " + firstCustomer.getName());
    /* remove */
    System.out.println("\n- Customers before remove: " + garage.getCustomers().size());
    garage.removeCustomer(customer3);
    System.out.println("- Customers after remove: " + garage.getCustomers().size());
    /* isEmpty ? */
    System.out.println("- Has customers: " + garage.hasCustomers());

    // Iterate through Map (spareParts) + put (add a sparePart) + get (sparePart with key-->value pair)

    System.out.println("\nAll spare parts:");
    for (Map.Entry<String, SparePart> entry : garage.getSpareParts().entrySet()) {
        System.out.println("- " + entry.getKey()
                + " | " + entry.getValue().getProductName()
                + " | quantity: " + entry.getValue().getQuantity());
    }
    /* Map — first entry via entrySet iterator */
    SparePart firstPart = garage.getSpareParts().entrySet().iterator().next().getValue();
    System.out.println("\n- First spare part: " + firstPart.getProductName());
    /* put */
    System.out.println("\n- Spare parts before put: " + garage.getSpareParts().size());
    SparePart sparkPlug = new SparePart("Spark Plug", "SP-1122", new BigDecimal("8.00"), 15);
    garage.addSparePart(sparkPlug);
    System.out.println("- Spare parts after put: " + garage.getSpareParts().size());
    /* get */
    System.out.println("- Spare part lookup: " + garage.getSparePartByNumber("OF-4521").getProductName());

    // Iteration through List (vehicles) + print first element + remove
    Vehicle firstVehicle = garage.getVehicles().get(0);
    System.out.println("\n- First vehicle: " + firstVehicle.getBrand()
            + " " + firstVehicle.getModel());
    System.out.println("\nAll vehicles: ");
    for (Vehicle vehicle : garage.getVehicles()) {
        System.out.println("- " + vehicle.getBrand()
                + " | " + vehicle.getModel()
                + " | " + vehicle.getYear()
                + " | " + vehicle.getVin());
    }
    System.out.println("\n- Total vehicles before remove: " + garage.getTotalVehicles());
    garage.removeVehicle(truck);
    System.out.println("- Vehicles after remove: " + garage.getTotalVehicles());

    /* ************************************************************************************************************ */

    // ---  java.util.function LAMBDA DEMONSTRATIONS  lambda = '->' = parameter -> action-- //
    System.out.println("\n------------------------------------------------------------------------------");
    System.out.println("  java.util.function LAMBDA DEMONSTRATIONS");
    System.out.println("------------------------------------------------------------------------------");

    // 1) PREDICATE <Vehicle> - Filters the garage's vehicle list by a given condition. //

    System.out.println("\n--- 1. Predicate<Vehicle>: vehicles from 2018 or newer ---");

    Predicate<Vehicle> isModernVehicleCondition = vehicle -> vehicle.getYear() >= 2018;

    List<Vehicle> modernVehicles = garage.filterVehicles(isModernVehicleCondition);
    modernVehicles.forEach(vehicle ->
            System.out.println("  " + vehicle.getBrand() + " " + vehicle.getModel() + " (" + vehicle.getYear() + ")")
    );

    /* second condition */

    Predicate<Vehicle> isNotMotorcycleCondition = vehicle -> !(vehicle instanceof Motorcycle);

    /* 2 conditions with .and */
    List<Vehicle> modernNonMotorcycles = garage.filterVehicles(isModernVehicleCondition.and(isNotMotorcycleCondition));

    System.out.println("  Modern non-motorcycles:");
    modernNonMotorcycles.forEach(v ->
            System.out.println("  " + v.getBrand() + " " + v.getModel())
    );

    // 2) Function<Mechanic, String> Transforms each mechanic into a formatted roster String

    System.out.println("\n--- 2. Function<Mechanic, String>: formatted mechanic roster ---");

    Function<Mechanic, String> formatter = mechanic ->
            String.format("%-10s | %-20s | %d yrs exp | %.2f GEL/h",
                    mechanic.getName(),
                    mechanic.getSpecialization(),
                    mechanic.getYearsOfExperience(),
                    mechanic.getHourlyRate());

    List<String> roster = garage.getMechanicRoster(formatter);

    roster.forEach(line -> System.out.println("  " + line));

    // 3) BiFunction<String, Integer, List<SparePart>>

        /* finds spare parts whose product number starts with a given prefix
            and whose quantity is below a minimum threshold,
            useful for finding what is low in stock. */

    System.out.println("\n--- 3. BiFunction<String, Integer, List<SparePart>>: low in stock---");

    BiFunction<String, Integer, List<SparePart>> lowStockChecker = (prefix, minQty) -> {

        List<SparePart> result = new ArrayList<>();

        for (SparePart part : garage.getSpareParts().values()) {
            if (part.getProductNumber().startsWith(prefix) && part.getQuantity() < minQty) {
                result.add(part);
            }
        }
        return result;
    };

    List<SparePart> lowStock = garage.getLowStockParts(lowStockChecker, "BD", 5);
    if (lowStock.isEmpty()) {
        System.out.println("  No low-stock parts found for prefix 'BD'.");
    } else {
        lowStock.forEach(part ->
                System.out.println("  LOW STOCK: " + part.getProductName()
                        + " (" + part.getProductNumber() + ") — qty: " + part.getQuantity())
        );
    }

    // check all parts below quantity 10 (no prefix filter needed,empty prefix)
    BiFunction<String, Integer, List<SparePart>> lowInStock = (_, minQty) ->
            garage.getSpareParts().values().stream()
                    .filter(part -> part.getQuantity() < minQty)
                    .collect(Collectors.toList());

    System.out.println("  All parts with qty < 10:");
    garage.getLowStockParts(lowInStock, "", 10).forEach(part ->
            System.out.println("  - " + part.getProductName() + " | qty: " + part.getQuantity())
    );

    // 4. Consumer<Customer> takes object,  does something return nothing

    System.out.println("\n--- 4. Consumer<Customer>: award 10 bonus loyalty points to all ---");

    Consumer<Customer> awardBonusPoints = customer -> {
        customer.addLoyaltyPoints(10);
        System.out.println("  Awarded 10 pts to " + customer.getName()
                + " | Total: " + customer.getLoyaltyPoints());
    };

    garage.forEachCustomer(awardBonusPoints);

    // 5. UnaryOperator <T> - takes certain type changes value returns same type + Predicate Yes/No logic

    System.out.println("\n--- 5. UnaryOperator<BigDecimal>: 20% senior bonus for mechanics with 10+ years ---");

    Predicate<Mechanic> isSenior = mechanic -> mechanic.getYearsOfExperience() >= 10;
    UnaryOperator<BigDecimal> seniorBonus = rate -> rate.multiply(new BigDecimal("1.20"));

    System.out.println("  Before:");
    garage.getMechanics().forEach(n ->
            System.out.printf("  %s | %.2f GEL/h%n", n.getName(), n.getHourlyRate())
    );

    garage.applyRateAdjustment(isSenior, seniorBonus);

    System.out.println("  After:");
    garage.getMechanics().forEach(n ->
            System.out.printf("  %s | %.2f GEL/h%n", n.getName(), n.getHourlyRate())
    );

    // 6. Runnable - takes nothing, returns nothing, just runs a task
    System.out.println("\n--- Runnable: print garage status ---");
    Runnable garageStatusPrinter = () -> {
        System.out.println("Garage: " + garage.getName());
        System.out.println("Free bays: " + garage.getFreeBays());
        System.out.println("Total vehicles: " + garage.getTotalVehicles());
    };
    garageStatusPrinter.run();

    // 7. Supplier - takes nothing, returns something
    System.out.println("\n--- Supplier: get first customer name ---");
    Supplier<String> firstCustomerName = () -> garage.getCustomers().iterator().next().getName();
    System.out.println("First customer: " + firstCustomerName.get());

    // --- Custom Functional Interfaces just to showcase ---

    // 1. DiscountStrategy
    DiscountStrategy thirtyPercentDiscount = price -> price * 0.7;
    BigDecimal discounted = new BigDecimal(thirtyPercentDiscount.apply(200));
    System.out.println("\nDiscounted price: " + discounted);

    // 2. AppointmentFilter
    AppointmentFilter onlyScheduled = a -> a.getStatus() == ServiceStatus.SCHEDULED;
    System.out.println("Is appointment scheduled? | " + (onlyScheduled.test(appointment1) ? "Yes" : "No"));

    // 3. ObjectFormatter
    ObjectFormatter<Appointment> objectFormatter = appointment -> appointment.getCustomer().getName()
            + " | " + appointment.getStatus();
    System.out.println("Appointment with customer: " + objectFormatter.format(appointment1));
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
