package autoservice.repair.model;

public class Car extends Vehicle implements Drivable, Maintainable, Inspectable {

    private Integer doors;
    private String engineType;
    private Double engineSize;
    private Transmission transmission;

    // --- Dynamic states ---
    private Boolean driven = false;
    private Boolean maintenanceDone = false;
    private Boolean inspected = false;

    public Car(String brand, String model, String vin, String licensePlate, Integer year, Integer doors, String engineType, Double engineSize, Transmission transmission) {
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
    public String getEngineType() {
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
    public void setEngineType(String engineType) {
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

    // --- Drivable implementation ---
    @Override
    public void drive() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Car Customer is driving");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Brand: " + getBrand());
        System.out.println("Model: " + getModel());
        System.out.println("Engine Type: " + getEngineType());
        System.out.println("VIN: " + getVin());
        System.out.println("Year: " + getYear());

        driven = true;
    }

    // --- Maintainable implementation ---
    @Override
    public void performMaintenance() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Performing maintenance on the vehicle:");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Brand: " + getBrand());
        System.out.println("Model: " + getModel());
        System.out.println("VIN: " + getVin());
        System.out.println("Year: " + getYear());
        System.out.println("Engine Type: " + getEngineType());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Maintenance Steps:");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("- Check oil levels and change if necessary.");
        System.out.println("- Test brakes for wear and responsiveness.");
        System.out.println("- Inspect suspension and tires.");

        maintenanceDone = true;
    }

    // --- Inspectable implementation ---
    @Override
    public void performInspection() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Inspection report for car: " + getBrand() + " " + getModel());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("- Driven: " + (driven ? "Yes" : "No"));
        System.out.println("- Maintenance done: " + (maintenanceDone ? "Yes" : "No"));
        System.out.println("- Inspected before: " + (inspected ? "Yes" : "No"));

        inspected = true;

        System.out.println("- Inspected after: " + "Yes");
        System.out.println("------------------------------------------------------------------------------");
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
