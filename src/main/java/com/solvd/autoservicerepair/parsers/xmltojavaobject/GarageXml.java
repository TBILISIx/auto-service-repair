package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * ROOT Class with a fully populated object which is returned with to string method in GarageStaxParser.
 * Mirrors <garage> in the XML file.
 */

@Setter
@Getter
@ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class GarageXml {

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("totalBays")
    private int totalBays;

    @JsonProperty("occupiedBays")
    private int occupiedBays;

    @JsonProperty("mechanics")
    private List<MechanicXml> mechanics = new ArrayList<>();

    @JsonProperty("customers")
    private List<CustomerXml> customers = new ArrayList<>();

    @JsonProperty("vehicles")
    private List<VehicleXml> vehicles = new ArrayList<>();

    @JsonProperty("appointments")
    private List<AppointmentXml> appointments = new ArrayList<>();

    @JsonProperty("spareParts")
    private List<SparePartXml> spareParts = new ArrayList<>();

}