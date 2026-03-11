package autoservice.repair.model;

public class Truck {

    private final String brand;
    private final String model;
    private final int doors;
    private final int tires;
    private final double engineSize;
    private final double payloadCapacityTons;
    private final boolean hasSleepingCabin;
    private final Transmission transmission;

    public Truck(String brand, String model, int doors, int tires, double engineSize,
                 double payloadCapacityTons, boolean hasSleepingCabin, Transmission transmission) {
        this.brand = brand;
        this.model = model;
        this.doors = doors;
        this.tires = tires;
        this.engineSize = engineSize;
        this.payloadCapacityTons = payloadCapacityTons;
        this.hasSleepingCabin = hasSleepingCabin;
        this.transmission = transmission;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getDoors() {
        return doors;
    }

    public int getTires() {
        return tires;
    }

    public double getEngineSize() {
        return engineSize;
    }

    public double getPayloadCapacityTons() {
        return payloadCapacityTons;
    }

    public boolean hasSleepingCabin() {
        return hasSleepingCabin;
    }

    public Transmission getTransmission() {
        return transmission;
    }

}
