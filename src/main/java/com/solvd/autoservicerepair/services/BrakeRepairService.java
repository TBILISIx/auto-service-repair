package com.solvd.autoservicerepair.services;

import com.solvd.autoservicerepair.enums.ServiceType;

import java.math.BigDecimal;

public class BrakeRepairService extends Service {

    public BrakeRepairService(BigDecimal price) {

        super(ServiceType.BRAKE_REPAIR, price);

    }

    @Override
    public Integer getDurationMinutes() {
        return 90;
    }

    @Override
    public String getServiceDescription() {
        return "Inspecting and repairing vehicle brakes — Category: "
                + serviceType.getCategory() + " | " + "Safety Related ? - "
                + (serviceType.isSafetyRelated() ? "Yes |" : "NO |");
    }

}



