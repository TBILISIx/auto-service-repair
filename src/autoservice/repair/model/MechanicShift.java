package autoservice.repair.model;

import autoservice.repair.services.Service;

import java.time.LocalDate;

public class MechanicShift {

    private final Mechanic mechanic;
    private final LocalDate shiftDate;
    private final int startHour;
    private final int endHour;
    private final Service[] assignedServices;
    private int assignedCount;

    public MechanicShift(Mechanic mechanic, LocalDate shiftDate, int startHour, int endHour) {
        if (startHour < 0 || endHour > 24 || startHour >= endHour) {
            throw new IllegalArgumentException("Invalid shift hours: " + startHour + " - " + endHour);
        }
        this.mechanic = mechanic;
        this.shiftDate = shiftDate;
        this.startHour = startHour;
        this.endHour = endHour;
        this.assignedServices = new Service[(endHour - startHour) * 60]; // upper limit of minutes if service took 1 minute
        this.assignedCount = 0;
    }

    public int getShiftDurationMinutes() {
        return (endHour - startHour) * 60;
    }

    public int getRemainingMinutesBeforeShiftEnd() {
        int bookedOrders = 0;
        for (int i = 0; i < assignedCount; i++) {
            bookedOrders += assignedServices[i].getDurationMinutes();
        }
        return (endHour - startHour) * 60 - bookedOrders;
    }

    public boolean canTakeService(Service service) {
        return service.getDurationMinutes() <= getRemainingMinutesBeforeShiftEnd();
    }

    public void assignService(Service service) {
        if (!canTakeService(service)) {
            throw new IllegalStateException("Mechanic " + mechanic.getName()
                    + " has no time for " + service.getServiceName()
                    + " on " + shiftDate + " needs " + service.getDurationMinutes()
                    + " minutes, has" + getRemainingMinutesBeforeShiftEnd() + " minutes left.");
        }
        assignedServices[assignedCount++] = service;
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

    public int getAssignedCount() {return assignedCount;}

    public Service[] getAssignedServices() {return assignedServices;}


}
