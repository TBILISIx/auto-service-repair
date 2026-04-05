package com.solvd.auto.service.repair.services;

import com.solvd.auto.service.repair.enums.ServiceType;

import java.math.BigDecimal;

public class TireChange extends Service {

    public TireChange(BigDecimal price) {
        super(ServiceType.TIRE_CHANGE, price); // pass displayName
    }

    @Override
    public Integer getDurationMinutes() {
        return 60;
    }

    @Override
    public String getServiceDescription() {
        return serviceType.getCategory() + " | Safety Related ? - "
                + (serviceType.isSafetyRelated() ? "Yes |" : "NO |");
    }

}
