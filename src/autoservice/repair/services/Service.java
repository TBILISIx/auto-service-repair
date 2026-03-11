package autoservice.repair.services;

import java.math.BigDecimal;

public class Service {

    private final String serviceName;
    private BigDecimal price;

    public Service(String serviceName, BigDecimal price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
