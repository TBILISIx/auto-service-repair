package autoservice.repair.model;

public class Motorcycle extends Vehicle {

    private final Integer engineCapacity;  // in cc
    private final String bikeType;     // Sport / Cruiser / Off-road / Scooter

    public Motorcycle(String brand, String model, String vin, String licensePlate, Integer engineCapacity, String bikeType) {
        super(brand, model, vin, licensePlate);
        this.engineCapacity = engineCapacity;
        this.bikeType = bikeType;
    }

    public Integer getEngineCapacity() {
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
