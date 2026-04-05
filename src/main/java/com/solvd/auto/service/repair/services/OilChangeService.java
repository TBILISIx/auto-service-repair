package com.solvd.auto.service.repair.services;

import com.solvd.auto.service.repair.enums.ServiceType;

import java.math.BigDecimal;

public class OilChangeService extends Service {

    public OilChangeService(BigDecimal price) {
        super(ServiceType.OIL_CHANGE, price);

    }

    @Override
    public Integer getDurationMinutes() {
        return 20;
    }

    @Override
    public String getServiceDescription() {
        return "Changing vehicle oil — Category: "
                + serviceType.getCategory() + " | Safety Related? - "
                + (serviceType.isSafetyRelated() ? "Yes |" : "NO |");
    }

}

