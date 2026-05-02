package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mirrors <car>, <motorcycle>, <truck>.
 * One class covers all three — the "type" field ("car"/"motorcycle"/"truck")
 * tells you which fields are populated.
 * Fields not relevant to a type are left at their default (0, null, false).
 */

@Getter
@Setter
@ToString

public class VehicleXml {

    // shared (BaseVehicleType)
    private String type;           // "car" | "motorcycle" | "truck"
    private String brand;
    private String model;
    private int year;
    private String vin;
    private String licensePlate;
    private TransmissionXml transmission;

    // car + truck
    private int doors;
    private String engineType;     // maps to EngineType enum
    private double engineSize;

    // motorcycle only
    private int engineCapacity;
    private String bikeType;       // maps to BikeType enum

    // truck only
    private int tires;
    private double payloadCapacityTons;
    private boolean hasSleepingCabin;  // THE boolean from the assignment

}
