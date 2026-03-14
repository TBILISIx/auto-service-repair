package autoservice.repair.model;

public class Motorcycle extends Vehicle {

    private final int engineCapacity;  // in cc
    private final String bikeType;     // Sport / Cruiser / Off-road / Scooter

    public Motorcycle(String brand, String model, int engineCapacity, String bikeType) {
        super(brand, model);
        this.engineCapacity = engineCapacity;
        this.bikeType = bikeType;

    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public String getBikeType() {
        return bikeType;
    }

}
