package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import java.util.ArrayList;
import java.util.List;

/**
 * ROOT Class with a fully populated object which is returned with to string method in GarageStaxParser.
 * Mirrors <garage> in the XML file.
 */

public class GarageXml {

    private String name;
    private String address;
    private int totalBays;
    private int occupiedBays;
    private List<MechanicXml> mechanics = new ArrayList<>();
    private List<CustomerXml> customers = new ArrayList<>();
    private List<VehicleXml> vehicles = new ArrayList<>();
    private List<AppointmentXml> appointments = new ArrayList<>();
    private List<SparePartXml> spareParts = new ArrayList<>();

    // getters
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public int getTotalBays() {
        return totalBays;
    }
    public int getOccupiedBays() {
        return occupiedBays;
    }
    public List<MechanicXml> getMechanics() {
        return mechanics;
    }
    public List<CustomerXml> getCustomers() {
        return customers;
    }
    public List<VehicleXml> getVehicles() {
        return vehicles;
    }
    public List<AppointmentXml> getAppointments() {
        return appointments;
    }
    public List<SparePartXml> getSpareParts() {
        return spareParts;
    }

    // setters
    public void setName(String v) {
        name = v;
    }
    public void setAddress(String v) {
        address = v;
    }
    public void setTotalBays(int v) {
        totalBays = v;
    }
    public void setOccupiedBays(int v) {
        occupiedBays = v;
    }
    public void setMechanics(List<MechanicXml> v) {
        mechanics = v;
    }
    public void setCustomers(List<CustomerXml> v) {
        customers = v;
    }
    public void setVehicles(List<VehicleXml> v) {
        vehicles = v;
    }
    public void setAppointments(List<AppointmentXml> v) {
        appointments = v;
    }
    public void setSpareParts(List<SparePartXml> v) {
        spareParts = v;
    }

    @Override
    public String toString() {
        return "Garage{name='" + name + "', address='" + address +
                "', totalBays=" + totalBays + ", occupiedBays=" + occupiedBays +
                ", mechanics=" + mechanics.size() +
                ", customers=" + customers.size() +
                ", vehicles=" + vehicles.size() +
                ", appointments=" + appointments.size() +
                ", spareParts=" + spareParts.size() + "}";
    }

}