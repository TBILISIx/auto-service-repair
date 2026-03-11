package autoservice.repair.services;

import java.math.BigDecimal;

public class TireChange extends Service {

    public TireChange(BigDecimal price) {
        super("Tire Change", price);
    }
}
