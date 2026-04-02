package autoservice.repair.model;

import autoservice.repair.enums.BikeType;
import autoservice.repair.enums.EngineType;
import autoservice.repair.exceptions.AgeException;
import autoservice.repair.interfaces.Inspectable;
import autoservice.repair.interfaces.Maintainable;
import autoservice.repair.interfaces.Rideable;
import autoservice.repair.interfaces.ValidAge;

public class Motorcycle extends Vehicle implements Rideable, Maintainable, Inspectable, ValidAge {

    private final EngineType engineType;
    private final Integer engineCapacity;
    private final BikeType bikeType;
    private final Transmission transmission;

    private Boolean ridden = false;
    private Boolean maintenanceDone = false;
    private Boolean inspected = false;

    public Motorcycle(String brand, String model, Integer year, String vin, String licensePlate, EngineType engineType, Integer engineCapacity, BikeType bikeType, Transmission transmission) {
        super(brand, model, year, vin, licensePlate);
        this.engineType = engineType;
        this.engineCapacity = engineCapacity;
        this.bikeType = bikeType;
        this.transmission = transmission;
    }
    public EngineType getEngineType() {
        return engineType;
    }
    public Integer getEngineCapacity() {
        return engineCapacity;
    }
    public BikeType getBikeType() {
        return bikeType;
    }
    public Transmission getTransmission() {
        return transmission;
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

    @Override
    public void ride() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Motorcycle Customer is riding");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Brand: " + getBrand());
        System.out.println("Model: " + getModel());
        System.out.println("VIN: " + getVin());
        System.out.println("BikeType: " + getBikeType());
        System.out.println("Year: " + getYear());
        this.ridden = true;
    }

    @Override
    public void performMaintenance() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Performing maintenance on motorcycle:");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Brand: " + getBrand());
        System.out.println("Model: " + getModel());
        System.out.println("VIN: " + getVin());
        System.out.println("Year: " + getYear());
        System.out.println("Engine Capacity: " + getEngineCapacity() + "cc");
        System.out.println("Bike Type: " + getBikeType());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Maintenance Steps:");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("- Check oil levels and change if necessary.");
        System.out.println("- Test brakes for wear and responsiveness.");
        System.out.println("- Check tire pressure and tread.");
        maintenanceDone = true;
    }

    @Override
    public void performInspection() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Inspection report for motorcycle: " + getBrand() + " " + getModel());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("- Ridden: " + (ridden ? "Yes" : "No"));
        System.out.println("- Maintenance done: " + (maintenanceDone ? "Yes" : "No"));
        System.out.println("- Inspected before: " + (inspected ? "Yes" : "No"));

        inspected = true;

        System.out.println("- Inspected after: " + "Yes");
        System.out.println("------------------------------------------------------------------------------");
    }

    @Override
    public void validateAge(Customer customer) throws AgeException {
        if (engineCapacity > 50 && customer.getAge() < 16) {
            throw new AgeException("According to Georgian law, customers under 16 cannot drive motorcycles above 50cc!");
        }
        if (engineCapacity > 125 && customer.getAge() == 17) {
            throw new AgeException("According to Georgian law, customers of age 17 cannot drive motorcycles above 125cc!");
        }
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
