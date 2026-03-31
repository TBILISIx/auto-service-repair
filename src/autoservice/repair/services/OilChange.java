package autoservice.repair.services;

import autoservice.repair.enums.ServiceType;

import java.math.BigDecimal;

public class OilChange extends Service {

    public OilChange(BigDecimal price) {
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

