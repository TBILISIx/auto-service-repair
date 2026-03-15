package autoservice.repair.model;

import java.util.Objects;

public class Truck extends Vehicle {

    private final int doors;
    private final int tires;
    private final double engineSize;
    private final double payloadCapacityTons;
    private final boolean hasSleepingCabin;
    private final Transmission transmission;

    public Truck(String brand, String model, String vin, String licensePlate, int doors, int tires, double engineSize,
                 double payloadCapacityTons, boolean hasSleepingCabin, Transmission transmission) {
        super(brand, model, vin, licensePlate);
        this.doors = doors;
        this.tires = tires;
        this.engineSize = engineSize;
        this.payloadCapacityTons = payloadCapacityTons;
        this.hasSleepingCabin = hasSleepingCabin;
        this.transmission = transmission;
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

    @Override
    public String toString() {
        return "Truck{" +
                "brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", vin='" + getVin() + '\'' +
                ", licensePlate='" + getLicensePlate() + '\'' +
                ", doors=" + doors +
                ", tires=" + tires +
                ", engineSize=" + engineSize +
                ", payloadCapacityTons=" + payloadCapacityTons +
                ", hasSleepingCabin=" + hasSleepingCabin +
                ", transmission=" + transmission +
                '}';
    }

}

//    @Override
//    public boolean equals(Object o) {
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
