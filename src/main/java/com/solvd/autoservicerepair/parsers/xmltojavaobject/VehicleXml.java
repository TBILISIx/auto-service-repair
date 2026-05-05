package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleXml {

    @JsonProperty("type")
    private String type;          // "car" | "motorcycle" | "truck"

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model")
    private String model;

    @JsonProperty("year")
    private int year;

    @JsonProperty("vin")
    private String vin;

    @JsonProperty("licensePlate")
    private String licensePlate;

    @JsonProperty("transmission")
    private TransmissionXml transmission;

    @JsonProperty("doors")
    private int doors;

    @JsonProperty("engineType")
    private String engineType;              // maps to EngineType enum

    @JsonProperty("engineSize")
    private double engineSize;

    @JsonProperty("engineCapacity")
    private int engineCapacity;

    @JsonProperty("bikeType")
    private String bikeType;                    // maps to BikeType enum

    @JsonProperty("tires")
    private int tires;

    @JsonProperty("payloadCapacityTons")
    private double payloadCapacityTons;

    @JsonProperty("hasSleepingCabin")
    private boolean hasSleepingCabin;          // THE boolean from the assignment


}