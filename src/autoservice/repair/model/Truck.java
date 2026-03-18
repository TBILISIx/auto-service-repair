package autoservice.repair.model;

public class Truck extends Vehicle {

    private final Integer doors;
    private final Integer tires;
    private final Double engineSize;
    private final Double payloadCapacityTons;
    private final Boolean hasSleepingCabin;
    private final Transmission transmission;

    public Truck(String brand, String model, String vin, String licensePlate, Integer doors, Integer tires, Double engineSize,
                 Double payloadCapacityTons, Boolean hasSleepingCabin, Transmission transmission) {
        super(brand, model, vin, licensePlate);
        this.doors = doors;
        this.tires = tires;
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

    public Double getEngineSize() {
        return engineSize;
    }

    public Double getPayloadCapacityTons() {
        return payloadCapacityTons;
    }

    public Boolean hasSleepingCabin() {
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
