package autoservice.repair.functional;

import autoservice.repair.services.AppointmentService;

@FunctionalInterface
public interface AppointmentFilter {

    boolean test(AppointmentService appointment);

}