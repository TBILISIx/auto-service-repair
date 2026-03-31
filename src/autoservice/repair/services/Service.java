package autoservice.repair.services;

import autoservice.repair.enums.ServiceType;

import java.math.BigDecimal;

public abstract class Service {

    protected ServiceType serviceType;
    protected BigDecimal price;

    public Service(ServiceType serviceType, BigDecimal price) {
        this.serviceType = serviceType;
        this.price = price;
    }

    public String getServiceName() {
        return serviceType.getDisplayName();
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public abstract Integer getDurationMinutes();

    public abstract String getServiceDescription();

}
