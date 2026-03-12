package autoservice.repair.model;

public class Car {

    private String brand;
    private String model;
    private int doors;
    private String engineType;
    private double engineSize;
    private Transmission transmission;  // your Transmission class

    public Car(String brand, String model, int doors, int tires, String engineType,
               double engineSize, Transmission transmission) {
        this.brand = brand;
        this.model = model;
        this.doors = doors;
        this.engineType = engineType;
        this.engineSize = engineSize;
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

    public String getEngineType() {
        return engineType;
    }

    public double getEngineSize() {
        return engineSize;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }


    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }
}
