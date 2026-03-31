package autoservice.repair.services;

import autoservice.repair.enums.ServiceType;

import java.math.BigDecimal;

public class BrakeRepair extends Service {

    public BrakeRepair(BigDecimal price) {

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



