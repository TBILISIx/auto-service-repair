package autoservice.repair.services;

import java.math.BigDecimal;

public class OilChange extends Service {

    public OilChange(BigDecimal price) {

        super("Oil Change", price);
    }
}
