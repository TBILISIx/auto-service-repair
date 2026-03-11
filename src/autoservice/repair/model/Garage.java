package autoservice.repair.model;

import autoservice.repair.services.Appointment;
import autoservice.repair.services.RepairOrder;
import autoservice.repair.services.SparePart;

public class Garage {

    private final String name;
    private final String address;
    private final int totalBays;
    private int occupiedBays;
    private final Mechanic[] mechanics;
    private final MechanicShift[] shifts;

    private final Customer[] customers;
    private final Car[] cars;
    private final Motorcycle[] motorcycles;
    private final Truck[] trucks;
    private final SparePart[] spareParts;
    private final Appointment[] appointments;
    private final RepairOrder[] repairOrders;

    public Garage(String name, String address, int totalBays,
                  Mechanic[] mechanics, MechanicShift[] shifts,
                  Customer[] customers,
                  Car[] cars, Motorcycle[] motorcycles, Truck[] trucks,
                  SparePart[] spareParts,
                  Appointment[] appointments, RepairOrder[] repairOrders) {
        this.name = name;
        this.address = address;
        this.totalBays = totalBays;
        this.occupiedBays = 0;
        this.mechanics = mechanics;
        this.shifts = shifts;
        this.customers = customers;
        this.cars = cars;
        this.motorcycles = motorcycles;
        this.trucks = trucks;
        this.spareParts = spareParts;
        this.appointments = appointments;
        this.repairOrders = repairOrders;
    }

    public boolean hasFreeBay() {
        return totalBays > occupiedBays;
    }

    public void occupyBay() {
        if (!hasFreeBay()) {
            throw new IllegalStateException("No free bays available in garage: " + name);
        }
        occupiedBays++;
    }

    public void freeBay() {
        if (occupiedBays > 0) {
            occupiedBays--;
        }
    }

    public int getFreeBays() {
        return totalBays - occupiedBays;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getTotalBays() {
        return totalBays;
    }

    public int getOccupiedBays() {
        return occupiedBays;
    }

    public Mechanic[] getMechanics() {
        return mechanics;
    }

    public MechanicShift[] getShifts() {
        return shifts;
    }

    public Customer[] getCustomers() {
        return customers;
    }

    public Car[] getCars() {
        return cars;
    }

    public Motorcycle[] getMotorcycles() {
        return motorcycles;
    }

    public Truck[] getTrucks() {
        return trucks;
    }

    public SparePart[] getSpareParts() {
        return spareParts;
    }

    public Appointment[] getAppointments() {
        return appointments;
    }

    public RepairOrder[] getRepairOrders() {
        return repairOrders;
    }

}