package autoservice.repair.model;

public class Car extends Vehicle {

    private Integer doors;
    private String engineType;
    private Double engineSize;
    private Transmission transmission;  // your Transmission class

    public Car(String brand, String model, String vin, String licensePlate, Integer doors, String engineType,
               Double engineSize, Transmission transmission) {
        super(brand, model, vin, licensePlate);
        this.doors = doors;
        this.engineType = engineType;
        this.engineSize = engineSize;
        this.transmission = transmission;
    }

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

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", vin='" + getVin() + '\'' +
                ", licensePlate='" + getLicensePlate() + '\'' +
                ", doors=" + doors +
                ", engineType='" + engineType + '\'' +
                ", engineSize=" + engineSize +
                ", transmission=" + transmission +
                '}';
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
