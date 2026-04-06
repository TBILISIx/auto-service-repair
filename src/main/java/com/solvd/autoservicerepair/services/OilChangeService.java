package com.solvd.autoservicerepair.services;

import com.solvd.autoservicerepair.enums.ServiceType;

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

