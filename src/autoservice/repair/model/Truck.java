package autoservice.repair.model;

import autoservice.repair.exceptions.AgeException;

public class Truck extends Vehicle implements Drivable, Maintainable, Inspectable, ValidAge {

    private final Integer doors;
    private final Integer tires;
    private final Double engineSize;
    private final Double payloadCapacityTons;
    private final Boolean hasSleepingCabin;
    private final Transmission transmission;

    private Boolean driven = false;
    private Boolean maintenanceDone = false;
    private Boolean inspected = false;

    public Truck(String brand, String model, String vin, Integer year, String licensePlate, Integer doors, Integer tires, Double engineSize, Double payloadCapacityTons, Boolean hasSleepingCabin, Transmission transmission) {
        super(brand, model, year, vin, licensePlate);
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
    public Transmission getTransmission() {
        return transmission;
    }

    public Boolean hasSleepingCabin() {
        return hasSleepingCabin;
    }

    @Override
    public String toString() {
        return "Truck{" + "brand='" + getBrand() + '\'' + ", model='" + getModel() + '\'' + ", vin='" + getVin() + '\'' + ", licensePlate='" + getLicensePlate() + '\'' + ", doors=" + doors + ", tires=" + tires + ", engineSize=" + engineSize + ", payloadCapacityTons=" + payloadCapacityTons + ", hasSleepingCabin=" + hasSleepingCabin + ", transmission=" + transmission + '}';
    }

    @Override
    public void drive() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Truck Customer is driving");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Brand: " + getBrand());
        System.out.println("Model: " + getModel());
        System.out.println("VIN: " + getVin());
        System.out.println("Year: " + getYear());
        System.out.println("Engine Size: " + getEngineSize() + "L");
        System.out.println("Payload Capacity: " + getPayloadCapacityTons() + " tons");
        System.out.println("Transmission: " + getTransmission().type());
        System.out.println("Sleeping Cabin: " + (hasSleepingCabin() ? "Yes" : "No"));
        this.driven = true;
    }

    @Override
    public void performMaintenance() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Performing maintenance on truck:");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Brand: " + getBrand());
        System.out.println("Model: " + getModel());
        System.out.println("VIN: " + getVin());
        System.out.println("Engine Size: " + getEngineSize() + "L");
        System.out.println("Payload Capacity: " + getPayloadCapacityTons() + " tons");
        System.out.println("Sleeping Cabin: " + (hasSleepingCabin() ? "Yes" : "No"));
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Maintenance Steps:");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("- Check oil and fluid levels.");
        System.out.println("- Inspect brakes and tires.");
        System.out.println("- Check transmission fluid.");
        System.out.println("- Inspect suspension and load-bearing parts.");
        this.maintenanceDone = true;
    }

    @Override
    public void performInspection() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Inspection report for car: " + getBrand() + " " + getModel());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("- Driven: " + (driven ? "Yes" : "No"));
        System.out.println("- Maintenance done: " + (maintenanceDone ? "Yes" : "No"));
        System.out.println("- Inspected before: " + (inspected ? "Yes" : "No"));

        inspected = true;

        System.out.println("- Inspected after: " + "Yes");
        System.out.println("------------------------------------------------------------------------------");
    }

    @Override
    public void validateAge(Customer customer) throws AgeException {
        if (customer.getAge() < 18) {
            throw new AgeException("According to Georgian law, customers under 18 cannot drive trucks!");
        }
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
