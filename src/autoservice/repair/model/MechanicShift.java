package autoservice.repair.model;

import java.time.LocalDate;

public class MechanicShift {

    private final Mechanic mechanic;
    private final LocalDate shiftDate;
    private final int startHour;
    private final int endHour;
    private int assignedOrders;

    public MechanicShift(Mechanic mechanic, LocalDate shiftDate, int startHour, int endHour) {
        if (startHour < 0 || endHour > 24 || startHour >= endHour) {
            throw new IllegalArgumentException("Invalid shift hours: " + startHour + " - " + endHour);
        }
        this.mechanic = mechanic;
        this.shiftDate = shiftDate;
        this.startHour = startHour;
        this.endHour = endHour;
        this.assignedOrders = 0;
    }

    public int getShiftDurationHours() {
        return endHour - startHour;
    }

    public boolean canTakeMoreOrders() {
        int maxOrders = getShiftDurationHours() / 2;
        return assignedOrders < maxOrders;
    }

    public void assignOrder() {
        if (!canTakeMoreOrders()) {
            throw new IllegalStateException("Mechanic " + mechanic.getName()
                    + " cannot take more orders on " + shiftDate);
        }
        assignedOrders++;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getAssignedOrders() {
        return assignedOrders;
    }

}
