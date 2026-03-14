package autoservice.repair.services;

import java.math.BigDecimal;

public abstract class Service {

    protected String serviceName;
    protected BigDecimal price;

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

    public abstract int getDurationMinutes();
    public abstract String getServiceDescription();

}
