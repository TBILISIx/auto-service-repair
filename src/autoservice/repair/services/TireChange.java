package autoservice.repair.services;

import java.math.BigDecimal;

public class TireChange extends Service {

    public TireChange(BigDecimal price) {
        super("Tire Change", price);
    }

    @Override
    public int getDurationMinutes() {

        return 60;
    }

    @Override
    public String getServiceDescription() {
        return "Inspecting and replacing the vehicle tires.";
    }
}
