package com.solvd.autoservicerepair.functional;

import com.solvd.autoservicerepair.services.AppointmentService;

@FunctionalInterface
public interface AppointmentFilter {

    boolean test(AppointmentService appointment);

}