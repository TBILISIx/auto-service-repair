package com.solvd.auto.service.repair.functional;

import com.solvd.auto.service.repair.services.AppointmentService;

@FunctionalInterface
public interface AppointmentFilter {

    boolean test(AppointmentService appointment);

}