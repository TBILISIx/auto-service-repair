package com.solvd.autoservicerepair.parsers.xmltojavaobject;

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

public class GarageXml {

    // setters
    // getters
    private String name;
    private String address;
    private int totalBays;
    private int occupiedBays;
    private List<MechanicXml> mechanics = new ArrayList<>();
    private List<CustomerXml> customers = new ArrayList<>();
    private List<VehicleXml> vehicles = new ArrayList<>();
    private List<AppointmentXml> appointments = new ArrayList<>();
    private List<SparePartXml> spareParts = new ArrayList<>();

}