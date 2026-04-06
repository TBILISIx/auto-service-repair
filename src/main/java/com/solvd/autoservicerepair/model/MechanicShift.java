package com.solvd.autoservicerepair.model;

import com.solvd.autoservicerepair.exceptions.InsufficientResourceException;
import com.solvd.autoservicerepair.exceptions.InvalidArgumentException;
import com.solvd.autoservicerepair.services.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MechanicShift {

    private final Mechanic mechanic;
    private final LocalDate shiftDate;
    private final Integer startHour;
    private final Integer endHour;
    private final List<Service> assignedServices;

    public MechanicShift(Mechanic mechanic, LocalDate shiftDate, Integer startHour, Integer endHour) {
        if (startHour < 0 || endHour > 24 || startHour >= endHour) {
            throw new InvalidArgumentException("Invalid shift hours: " + startHour + " - " + endHour);
        }
        this.mechanic = mechanic;
        this.shiftDate = shiftDate;
        this.startHour = startHour;
        this.endHour = endHour;
        this.assignedServices = new ArrayList<>();
    }

    public Integer getShiftDurationMinutes() {
        return (endHour - startHour) * 60;
    }

    public Integer getRemainingMinutesBeforeShiftEnd() {
        int bookedMinutes = assignedServices.stream()
                .mapToInt(Service::getDurationMinutes) // Since method works only on instance of that class (object)
                .sum();
        return getShiftDurationMinutes() - bookedMinutes;
    }

    public Boolean canTakeService(Service service) {
        return service.getDurationMinutes() <= getRemainingMinutesBeforeShiftEnd();
    }

    public void assignService(Service service) {
        if (!canTakeService(service)) {
            throw new InsufficientResourceException("Mechanic " + mechanic.getName()
                    + " has no time for " + service.getServiceName()
                    + " on " + shiftDate + " needs " + service.getDurationMinutes()
                    + " minutes, has " + getRemainingMinutesBeforeShiftEnd() + " minutes left.");
        }
        assignedServices.add(service);
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

    public List<Service> getAssignedServices() {
        return Collections.unmodifiableList(assignedServices);
    }

}