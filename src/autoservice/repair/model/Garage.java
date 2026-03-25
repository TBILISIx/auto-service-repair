package autoservice.repair.model;

import autoservice.repair.services.Appointment;
import autoservice.repair.services.RepairOrder;

import java.util.List;
import java.util.Map;
import java.util.Set;

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