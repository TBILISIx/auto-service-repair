package com.solvd.autoservicerepair.model;

import com.solvd.autoservicerepair.enums.BikeType;
import com.solvd.autoservicerepair.enums.EngineType;
import com.solvd.autoservicerepair.exceptions.AgeException;
import com.solvd.autoservicerepair.interfaces.Inspectable;
import com.solvd.autoservicerepair.interfaces.Maintainable;
import com.solvd.autoservicerepair.interfaces.Rideable;
import com.solvd.autoservicerepair.interfaces.ValidAge;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Motorcycle extends Vehicle implements Rideable, Maintainable, Inspectable, ValidAge {

    private final EngineType engineType;
    private final Integer engineCapacity;
    private final BikeType bikeType;
    private final Transmission transmission;

    private Boolean ridden = false;
    private Boolean maintenanceDone = false;
    private Boolean inspected = false;

    public Motorcycle(String brand, String model, Integer year, String vin, String licensePlate, EngineType engineType, Integer engineCapacity, BikeType bikeType, Transmission transmission) {
        super(brand, model, year, vin, licensePlate);
        this.engineType = engineType;
        this.engineCapacity = engineCapacity;
        this.bikeType = bikeType;
        this.transmission = transmission;
    }
    public EngineType getEngineType() {
        return engineType;
    }
    public Integer getEngineCapacity() {
        return engineCapacity;
    }
    public BikeType getBikeType() {
        return bikeType;
    }
    public Transmission getTransmission() {
        return transmission;
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", vin='" + getVin() + '\'' +
                ", licensePlate='" + getLicensePlate() + '\'' +
                ", engineCapacity=" + engineCapacity +
                ", bikeType='" + bikeType + '\'' +
                '}';
    }

    @Override
    public void ride() {
        log.info("------------------------------------------------------------------------------");
        log.info("Motorcycle Customer is riding");
        log.info("------------------------------------------------------------------------------");
        log.info("Brand: " + getBrand());
        log.info("Model: " + getModel());
        log.info("VIN: " + getVin());
        log.info("BikeType: " + getBikeType());
        log.info("Year: " + getYear());
        this.ridden = true;
    }

    @Override
    public void performMaintenance() {
        log.info("------------------------------------------------------------------------------");
        log.info("Performing maintenance on motorcycle:");
        log.info("------------------------------------------------------------------------------");
        log.info("Brand: " + getBrand());
        log.info("Model: " + getModel());
        log.info("VIN: " + getVin());
        log.info("Year: " + getYear());
        log.info("Engine Capacity: " + getEngineCapacity() + "cc");
        log.info("Bike Type: " + getBikeType());
        log.info("------------------------------------------------------------------------------");
        log.info("Maintenance Steps:");
        log.info("------------------------------------------------------------------------------");
        log.info("- Check oil levels and change if necessary.");
        log.info("- Test brakes for wear and responsiveness.");
        log.info("- Check tire pressure and tread.");
        maintenanceDone = true;
    }

    @Override
    public void performInspection() {
        log.info("------------------------------------------------------------------------------");
        log.info("Inspection report for motorcycle: " + getBrand() + " " + getModel());
        log.info("------------------------------------------------------------------------------");
        log.info("- Ridden: " + (ridden ? "Yes" : "No"));
        log.info("- Maintenance done: " + (maintenanceDone ? "Yes" : "No"));
        log.info("- Inspected before: " + (inspected ? "Yes" : "No"));

        inspected = true;

        log.info("- Inspected after: " + "Yes");
        log.info("------------------------------------------------------------------------------");
    }

    @Override
    public void validateAge(Customer customer) throws AgeException {
        if (engineCapacity > 50 && customer.getAge() < 16) {
            throw new AgeException("According to Georgian law, customers under 16 cannot drive motorcycles above 50cc!");
        }
        if (engineCapacity > 125 && customer.getAge() == 17) {
            throw new AgeException("According to Georgian law, customers of age 17 cannot drive motorcycles above 125cc!");
        }
    }

}

//    @Override
//    public Boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Motorcycle)) return false;
//        Motorcycle motorcycle = (Motorcycle) o;
//        return Objects.equals(getVin(), motorcycle.getVin());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getVin());
//    }
