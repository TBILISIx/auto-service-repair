package autoservice.repair.services;

import java.math.BigDecimal;

public class BrakeRepair extends Service {

    public BrakeRepair(BigDecimal price) {

        super("Brake Repair", price);

    }

    @Override
    public int getDurationMinutes() {

        return 90;
    }

    @Override
    public String getServiceDescription() {
        return "Inspecting and Repairing vehicle brakes";
    }
}



