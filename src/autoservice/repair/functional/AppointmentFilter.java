package autoservice.repair.functional;

import autoservice.repair.services.Appointment;

@FunctionalInterface
public interface AppointmentFilter {
    boolean test(Appointment appointment);
}