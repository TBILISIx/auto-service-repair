package autoservice.repair.model;

import java.util.Objects;

public abstract class Vehicle {

    private final String brand;
    private final String model;
    private final String vin;
    private final String licensePlate;

    public Vehicle(String brand, String model, String vin, String licensePlate) {
        this.brand = brand;
        this.model = model;
        this.vin = vin;
        this.licensePlate = licensePlate;
    }

    public String getVin() {
        return vin;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", vin='" + vin + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;

        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vin, vehicle.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }
}


