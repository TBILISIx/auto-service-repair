package com.solvd.autoservicerepair.model;

import com.solvd.autoservicerepair.annotations.Checker;
import com.solvd.autoservicerepair.annotations.Description;
import com.solvd.autoservicerepair.annotations.ServiceInfo;
import com.solvd.autoservicerepair.enums.EngineType;
import com.solvd.autoservicerepair.exceptions.AgeException;
import com.solvd.autoservicerepair.interfaces.Drivable;
import com.solvd.autoservicerepair.interfaces.Inspectable;
import com.solvd.autoservicerepair.interfaces.Maintainable;
import com.solvd.autoservicerepair.interfaces.ValidAge;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Car extends Vehicle implements Drivable, Maintainable, Inspectable, ValidAge {

    private Integer doors;
    private EngineType engineType;
    private Double engineSize;
    private Transmission transmission;

    // --- Dynamic states ---
    private Boolean driven = false;
    private Boolean maintenanceDone = false;
    private Boolean inspected = false;

    public Car(String brand, String model, String vin, String licensePlate, Integer year, Integer doors, EngineType engineType, Double engineSize, Transmission transmission) {
        super(brand, model, year, vin, licensePlate);
        this.doors = doors;
        this.engineType = engineType;
        this.engineSize = engineSize;
        this.transmission = transmission;
    }

    // --- Getters and setters ---
    public Integer getDoors() {
        return doors;
    }
    public EngineType getEngineType() {
        return engineType;
    }
    public Double getEngineSize() {
        return engineSize;
    }
    public Transmission getTransmission() {
        return transmission;
    }

    public void setDoors(Integer doors) {
        this.doors = doors;
    }
    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }
    public void setEngineSize(Double engineSize) {
        this.engineSize = engineSize;
    }
    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    // --- Dynamic state getters ---
    public Boolean isDriven() {
        return driven;
    }
    public Boolean isMaintenanceDone() {
        return maintenanceDone;
    }
    public Boolean isInspected() {
        return inspected;
    }

    @Override
    public String toString() {
        return "Car{" + "brand='" + getBrand() + '\'' + ", model='" + getModel() + '\'' + ", vin='" + getVin() + '\'' + ", licensePlate='" + getLicensePlate() + '\'' + ", doors=" + doors + ", engineType='" + engineType + '\'' + ", engineSize=" + engineSize + ", transmission=" + transmission + '}';
    }

    // --- Drivable implementation + Custom Annotation implementation ----
    @Description(description = "This method gives us information about customers vehicle")
    @Override
    public void drive() {
        log.info("------------------------------------------------------------------------------");
        log.info("Car Customer is driving");
        log.info("------------------------------------------------------------------------------");
        log.info("Brand: " + getBrand());
        log.info("Model: " + getModel());
        log.info("Engine Type: " + getEngineType());
        log.info("VIN: " + getVin());
        log.info("Year: " + getYear());

        driven = true;
    }

    // --- Maintainable implementation + Custom Annotation implementation
    @ServiceInfo(description = "This method describes maintenance steps and on which vehicle it is performed",
            isSafetyCheck = false)
    @Override
    public void performMaintenance() {
        log.info("------------------------------------------------------------------------------");
        log.info("Performing maintenance on the vehicle:");
        log.info("------------------------------------------------------------------------------");
        log.info("Brand: " + getBrand());
        log.info("Model: " + getModel());
        log.info("VIN: " + getVin());
        log.info("Year: " + getYear());
        log.info("Engine Type: " + getEngineType());
        log.info("------------------------------------------------------------------------------");
        log.info("Maintenance Steps:");
        log.info("------------------------------------------------------------------------------");
        log.info("- Check oil levels and change if necessary.");
        log.info("- Test brakes for wear and responsiveness.");
        log.info("- Inspect suspension and tires.");

        maintenanceDone = true;
    }

    // --- Inspectable implementation + Custom Annotation implementation
    @ServiceInfo(description = "After maintenance official inspection is done, and this method gives its report",
            isSafetyCheck = true)
    @Override
    public void performInspection() {
        log.info("------------------------------------------------------------------------------");
        log.info("Inspection report for car: " + getBrand() + " " + getModel());
        log.info("------------------------------------------------------------------------------");
        log.info("- Driven: " + (driven ? "Yes" : "No"));
        log.info("- Maintenance done: " + (maintenanceDone ? "Yes" : "No"));
        log.info("- Inspected before: " + (inspected ? "Yes" : "No"));

        inspected = true;

        log.info("- Inspected after: " + "Yes");
        log.info("------------------------------------------------------------------------------");
    }

    // --- Custom Annotation Checker --- //

    @Checker(description = "This method checks customers age and whether is eligible to drive a car or not ")
    @Override
    public void validateAge(Customer customer) throws AgeException {
        if (customer.getAge() < 18) {
            throw new AgeException("According to Georgian law, customers under 18 cannot drive cars!");
        }
    }

}

// if in future I add more than Vin use these methods if not subclasses inherit from vehicle vin equals hash override.
//    @Override
//    public Boolean equals(Object o) {
//        if (this == o) return true; // same object
//        if (!(o instanceof Car)) return false; // not the same type
//        Car car = (Car) o;
//
//        // Cars are equal if Vin codes match
//        return Objects.equals(getVin(), car.getVin());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getVin());
//    }

// Override toString() , Purpose: human-readable representation of your object.
// Without equals() Override, Java uses reference equality by default (memory addresses). checks equality of memory addresses which don't match.

// | Method       | Default behavior                                  | After override                          |
// | ------------ | ------------------------------------------------- | --------------------------------------- |
// | `equals()`   | Checks memory address                             | Checks license plate (or chosen fields) |
// | `hashCode()` | Based on memory address converted to int(usually) | Based on license plate (matches equals) |
