package autoservice.repair.model;

public class Motorcycle {

    private final String brand;
    private final String model;
    private final int engineCapacity;  // in cc
    private final String bikeType;     // Sport / Cruiser / Off-road / Scooter

    public Motorcycle(String brand, String model, int engineCapacity, String bikeType) {
        this.brand = brand;
        this.model = model;
        this.engineCapacity = engineCapacity;
        this.bikeType = bikeType;

    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public String getBikeType() {
        return bikeType;
    }

}
