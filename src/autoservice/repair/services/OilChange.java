package autoservice.repair.services;

import java.math.BigDecimal;

public class OilChange extends Service {

    public OilChange(BigDecimal price) {

        super("Oil Change", price);
    }

    @Override
    public int getDurationMinutes() {

        return 20;
    }

    @Override
    public String getServiceDescription() {
        return "Changing vehicle oil";
    }
}

