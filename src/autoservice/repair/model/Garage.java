package autoservice.repair.model;

import autoservice.repair.services.Appointment;
import autoservice.repair.services.RepairOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.*;

public class Garage {

    private final String name;
    private final String address;
    private final Integer totalBays;
    private Integer occupiedBays;
    private final List<Mechanic> mechanics;
    private final List<MechanicShift> shifts;

    private final Set<Customer> customers;
    private final List<Vehicle> vehicles;
    private final Map<String, SparePart> spareParts;
    private final List<Appointment> appointments;
    private final List<RepairOrder<?>> repairOrders;

    public Garage(String name, String address, Integer totalBays, List<Mechanic> mechanics, List<MechanicShift> shifts, Set<Customer> customers, List<Vehicle> vehicles, Map<String, SparePart> spareParts, List<Appointment> appointments, List<RepairOrder<?>> repairOrders) {

        this.name = name;
        this.address = address;
        this.totalBays = totalBays;
        this.occupiedBays = 0;
        this.mechanics = mechanics;
        this.shifts = shifts;
        this.customers = customers;
        this.vehicles = vehicles;
        this.spareParts = spareParts;
        this.appointments = appointments;
        this.repairOrders = repairOrders;
    }

    /* java.util.functions built in functional interfaces  has exactly one abstract method, method defines the “function” of the interface
     * target for a lambda expression or method reference.*/

    // 1. Predicate true/false with condition filtering vehicles by year inside method argument Yes/No question

    public List<Vehicle> filterVehicles(Predicate<Vehicle> condition) {
        List<Vehicle> filteredList = new ArrayList<>(); // list to store results
        for (Vehicle n : vehicles) {
            if (condition.test(n)) { // if satisfies condition
                filteredList.add(n);
            }
        }

        return filteredList;
    }

    // 2. Function - Turn Mechanic object into a string 1) object 2) type 3) name  // Convert A into B

    public List<String> getMechanicRoster(Function<Mechanic, String> formatter) {
        List<String> roster = new ArrayList<>();
        for (Mechanic n : mechanics) {
            String formatted = formatter.apply(n);
            roster.add(formatted);
        }
        return roster;
    }

    // 3. BiFunction<String, Integer, List<SparePart>> — find parts below certain quantity, also accepting a category prefix
    // Convert A and B into C

    public List<SparePart> getLowStockParts(BiFunction<String, Integer, List<SparePart>> lowStockChecker,
                                            String prefix,
                                            int minQty) {
        return lowStockChecker.apply(prefix, minQty);
    }

    // 4. Consumer<Customer> — perform an action on every customer - give loyalty points (takes object does something to it returns nothing)

    public void forEachCustomer(Consumer<Customer> action) {
        customers.forEach(action);
    }

    // 5. UnaryOperator<T> takes certain type changes its value and returns same type also + added Predicate for first condition

    public void applyRateAdjustment(Predicate<Mechanic> condition, UnaryOperator<BigDecimal> seniorBonus) {
        mechanics.stream()
                .filter(condition)
                .forEach(mechanic -> mechanic.setHourlyRate(seniorBonus.apply(mechanic.getHourlyRate())));
    }

    public Boolean hasFreeBay() {

        return totalBays > occupiedBays;
    }
    public void occupyBay() {

        occupiedBays++;
    }
    public void freeBay(int freeUpNumber) {

        occupiedBays -= freeUpNumber;
    }

    public Integer getFreeBays() {

        return totalBays - occupiedBays;
    }
    public String getName() {

        return name;
    }
    public String getAddress() {

        return address;
    }
    public Integer getTotalBays() {

        return totalBays;
    }
    public Integer getOccupiedBays() {

        return occupiedBays;
    }
    public List<Mechanic> getMechanics() {

        return mechanics;
    }
    public List<MechanicShift> getShifts() {

        return shifts;
    }
    public Set<Customer> getCustomers() {

        return customers;
    }
    public List<Vehicle> getVehicles() {

        return vehicles;
    }
    public Map<String, SparePart> getSpareParts() {

        return spareParts;
    }
    public List<Appointment> getAppointments() {

        return appointments;
    }
    public List<RepairOrder<?>> getRepairOrders() {

        return repairOrders;
    }

    public boolean hasMechanic(Mechanic mechanic) {
        return mechanics.contains(mechanic);      // contains
    }
    public void removeMechanic(Mechanic mechanic) {
        mechanics.remove(mechanic);               // remove
    }

    public void addMechanic(Mechanic mechanic) {
        mechanics.add(mechanic);
    }

    public boolean hasCustomers() {
        return !customers.isEmpty();              // isEmpty
    }
    public void removeCustomer(Customer customer) {
        customers.remove(customer);               // remove (Set)
    }

    public Integer getTotalVehicles() {
        return vehicles.size();                   // size
    }
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);                 // remove (List)
    }

    public void addSparePart(SparePart sparePart) {
        spareParts.put(sparePart.getProductNumber(), sparePart);
    } // add sparePart with .put
    public SparePart getSparePartByNumber(String productNumber) {
        return spareParts.get(productNumber); // get (Map)
    }

}