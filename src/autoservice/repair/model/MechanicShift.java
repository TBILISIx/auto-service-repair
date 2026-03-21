package autoservice.repair.model;

import autoservice.repair.exceptions.InsufficientResourceException;
import autoservice.repair.exceptions.InvalidArgumentException;
import autoservice.repair.services.Service;

import java.time.LocalDate;

public class MechanicShift {

    private final Mechanic mechanic;
    private final LocalDate shiftDate;
    private final Integer startHour;
    private final Integer endHour;
    private final Service[] assignedServices;
    private Integer assignedCount;

    public MechanicShift(Mechanic mechanic, LocalDate shiftDate, Integer startHour, Integer endHour) {
        if (startHour < 0 || endHour > 24 || startHour >= endHour) {
            throw new InvalidArgumentException("Invalid shift hours: " + startHour + " - " + endHour);
        }
        this.mechanic = mechanic;
        this.shiftDate = shiftDate;
        this.startHour = startHour;
        this.endHour = endHour;
        this.assignedServices = new Service[(endHour - startHour) * 60]; // upper limit of minutes if service took 1 minute
        this.assignedCount = 0;
    }

    public Integer getShiftDurationMinutes() {
        return (endHour - startHour) * 60;
    }

    public Integer getRemainingMinutesBeforeShiftEnd() {
        Integer bookedOrders = 0;
        for (int i = 0; i < assignedCount; i++) {
            bookedOrders += assignedServices[i].getDurationMinutes();
        }
        return getShiftDurationMinutes() - bookedOrders;
    }

    public Boolean canTakeService(Service service) {
        return service.getDurationMinutes() <= getRemainingMinutesBeforeShiftEnd();
    }

    public void assignService(Service service) {
        if (!canTakeService(service)) {
            throw new InsufficientResourceException("Mechanic " + mechanic.getName()
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

    public Integer getStartHour() {
        return startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public Integer getAssignedCount() {
        return assignedCount;
    }

    public Service[] getAssignedServices() {
        return assignedServices;
    }

}
