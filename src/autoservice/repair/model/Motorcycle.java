package autoservice.repair.model;

import java.util.Objects;

public class Motorcycle extends Vehicle {

    private final int engineCapacity;  // in cc
    private final String bikeType;     // Sport / Cruiser / Off-road / Scooter

    public Motorcycle(String brand, String model, String vin, String licensePlate, int engineCapacity, String bikeType) {
        super(brand, model, vin, licensePlate);
        this.engineCapacity = engineCapacity;
        this.bikeType = bikeType;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public String getBikeType() {
        return bikeType;
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

}


//    @Override
//    public boolean equals(Object o) {
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
