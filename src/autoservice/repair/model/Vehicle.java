package autoservice.repair.model;

public abstract class Vehicle {

    private final String brand;
    private final String model;



    public  Vehicle(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }
}
