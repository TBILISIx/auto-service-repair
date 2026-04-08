package com.solvd.autoservicerepair.model;

import com.solvd.autoservicerepair.enums.EngineType;
import com.solvd.autoservicerepair.exceptions.AgeException;
import com.solvd.autoservicerepair.interfaces.Drivable;
import com.solvd.autoservicerepair.interfaces.Inspectable;
import com.solvd.autoservicerepair.interfaces.Maintainable;
import com.solvd.autoservicerepair.interfaces.ValidAge;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Truck extends Vehicle implements Drivable, Maintainable, Inspectable, ValidAge {

    private final Integer doors;
    private final Integer tires;
    private final EngineType engineType;
    private final Double engineSize;
    private final Double payloadCapacityTons;
    private final Boolean hasSleepingCabin;
    private final Transmission transmission;

    private Boolean driven = false;
    private Boolean maintenanceDone = false;
    private Boolean inspected = false;

    public Truck(String brand, String model, String vin, Integer year, String licensePlate, Integer doors, Integer tires, EngineType engineType, Double engineSize, Double payloadCapacityTons, Boolean hasSleepingCabin, Transmission transmission) {
        super(brand, model, year, vin, licensePlate);
        this.doors = doors;
        this.tires = tires;
        this.engineType = engineType;
        this.engineSize = engineSize;
        this.payloadCapacityTons = payloadCapacityTons;
        this.hasSleepingCabin = hasSleepingCabin;
        this.transmission = transmission;
    }

    public Integer getDoors() {
        return doors;
    }
    public Integer getTires() {
        return tires;
    }
    public EngineType getEngineType() {
        return engineType;
    }
    public Double getEngineSize() {
        return engineSize;
    }
    public Double getPayloadCapacityTons() {
        return payloadCapacityTons;
    }
    public Transmission getTransmission() {
        return transmission;
    }

    public Boolean hasSleepingCabin() {
        return hasSleepingCabin;
    }

    @Override
    public String toString() {
        return "Truck{" + "brand='" + getBrand() + '\'' + ", model='" + getModel() + '\'' + ", vin='" + getVin() + '\'' + ", licensePlate='" + getLicensePlate() + '\'' + ", doors=" + doors + ", tires=" + tires + ", engineSize=" + engineSize + ", payloadCapacityTons=" + payloadCapacityTons + ", hasSleepingCabin=" + hasSleepingCabin + ", transmission=" + transmission + '}';
    }

    @Override
    public void drive() {
        log.info("------------------------------------------------------------------------------");
        log.info("Truck Customer is driving");
        log.info("------------------------------------------------------------------------------");
        log.info("Brand: " + getBrand());
        log.info("Model: " + getModel());
        log.info("VIN: " + getVin());
        log.info("Year: " + getYear());
        log.info("Engine Size: " + getEngineSize() + "L");
        log.info("Payload Capacity: " + getPayloadCapacityTons() + " tons");
        log.info("Transmission: " + getTransmission().type());
        log.info("Sleeping Cabin: " + (hasSleepingCabin() ? "Yes" : "No"));
        this.driven = true;
    }

    @Override
    public void performMaintenance() {
        log.info("------------------------------------------------------------------------------");
        log.info("Performing maintenance on truck:");
        log.info("------------------------------------------------------------------------------");
        log.info("Brand: " + getBrand());
        log.info("Model: " + getModel());
        log.info("VIN: " + getVin());
        log.info("Engine Size: " + getEngineSize() + "L");
        log.info("Payload Capacity: " + getPayloadCapacityTons() + " tons");
        log.info("Sleeping Cabin: " + (hasSleepingCabin() ? "Yes" : "No"));
        log.info("------------------------------------------------------------------------------");
        log.info("Maintenance Steps:");
        log.info("------------------------------------------------------------------------------");
        log.info("- Check oil and fluid levels.");
        log.info("- Inspect brakes and tires.");
        log.info("- Check transmission fluid.");
        log.info("- Inspect suspension and load-bearing parts.");
        this.maintenanceDone = true;
    }

    @Override
    public void performInspection() {
        log.info("------------------------------------------------------------------------------");
        log.info("Inspection report for Truck: " + getBrand() + " " + getModel());
        log.info("------------------------------------------------------------------------------");
        log.info("- Driven: " + (driven ? "Yes" : "No"));
        log.info("- Maintenance done: " + (maintenanceDone ? "Yes" : "No"));
        log.info("- Inspected before: " + (inspected ? "Yes" : "No"));

        inspected = true;

        log.info("- Inspected after: " + "Yes");
        log.info("------------------------------------------------------------------------------");
    }

    @Override
    public void validateAge(Customer customer) throws AgeException {
        if (customer.getAge() < 18) {
            throw new AgeException("According to Georgian law, customers under 18 cannot drive trucks!");
        }
    }

}

//    @Override
//    public Boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Truck)) return false;
//        Truck truck = (Truck) o;
//        return Objects.equals(getVin(), truck.getVin());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getVin());
//    }
