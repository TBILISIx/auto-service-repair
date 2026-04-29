package com.solvd.autoservicerepair.parser;

import com.solvd.autoservicerepair.interfaces.Parser;
import com.solvd.autoservicerepair.parser.xmltojavaobject.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GarageStaxParser implements Parser<GarageXml> {

    private static final String CTX_GARAGE = "garage";
    private static final String CTX_MECHANIC = "mechanic";
    private static final String CTX_CUSTOMER = "customer";
    private static final String CTX_INSURANCE = "insurance";
    private static final String CTX_CAR = "car";
    private static final String CTX_MOTORCYCLE = "motorcycle";
    private static final String CTX_TRUCK = "truck";
    private static final String CTX_TRANSMISSION = "transmission";
    private static final String CTX_APPOINTMENT = "appointment";
    private static final String CTX_SPARE_PART = "sparePart";

    @Override
    public GarageXml parse(String filePath) throws Exception {

        XMLStreamReader reader = XMLInputFactory
                .newInstance()
                .createXMLStreamReader(new FileInputStream(filePath));

        GarageXml garage = new GarageXml();
        List<MechanicXml> mechanics = new ArrayList<>();
        List<CustomerXml> customers = new ArrayList<>();
        List<VehicleXml> vehicles = new ArrayList<>();
        List<AppointmentXml> appointments = new ArrayList<>();
        List<SparePartXml> spareParts = new ArrayList<>();

        MechanicXml currentMechanic = null;
        CustomerXml currentCustomer = null;
        InsuranceXml currentInsurance = null;
        VehicleXml currentVehicle = null;
        TransmissionXml currentTransmission = null;
        AppointmentXml currentAppointment = null;
        SparePartXml currentSparePart = null;

        Deque<String> context = new ArrayDeque<>();

        while (reader.hasNext()) {

            int event = reader.next();  // ask StAX for the next event

            if (event == XMLStreamConstants.START_ELEMENT) {

                String tag = reader.getLocalName();

                switch (tag) {

                    case CTX_GARAGE:
                        context.push(CTX_GARAGE);
                        break;

                    case "mechanics":
                    case "customers":
                    case "vehicles":
                    case "appointments":
                    case "spareParts":

                        context.push(tag);
                        break;

                    case CTX_MECHANIC:
                        currentMechanic = new MechanicXml();
                        context.push(CTX_MECHANIC);
                        break;

                    case CTX_CUSTOMER:
                        currentCustomer = new CustomerXml();
                        context.push(CTX_CUSTOMER);
                        break;

                    case CTX_INSURANCE:
                        currentInsurance = new InsuranceXml();
                        context.push(CTX_INSURANCE);
                        break;

                    case CTX_CAR:
                        currentVehicle = new VehicleXml();
                        currentVehicle.setType("car");
                        context.push(CTX_CAR);
                        break;

                    case CTX_MOTORCYCLE:
                        currentVehicle = new VehicleXml();
                        currentVehicle.setType("motorcycle");
                        context.push(CTX_MOTORCYCLE);
                        break;

                    case CTX_TRUCK:
                        currentVehicle = new VehicleXml();
                        currentVehicle.setType("truck");
                        context.push(CTX_TRUCK);
                        break;

                    case CTX_TRANSMISSION:
                        currentTransmission = new TransmissionXml();
                        context.push(CTX_TRANSMISSION);
                        break;

                    case CTX_APPOINTMENT:
                        currentAppointment = new AppointmentXml();
                        context.push(CTX_APPOINTMENT);
                        break;

                    case CTX_SPARE_PART:
                        currentSparePart = new SparePartXml();
                        context.push(CTX_SPARE_PART);
                        break;

                    case "name":
                        String nameValue = reader.getElementText();
                        switch (context.peek()) {
                            case CTX_GARAGE -> garage.setName(nameValue);
                            case CTX_MECHANIC -> currentMechanic.setName(nameValue);
                            case CTX_CUSTOMER -> currentCustomer.setName(nameValue);
                        }
                        break;

                    case "address":
                        garage.setAddress(reader.getElementText());
                        break;

                    case "totalBays":
                        garage.setTotalBays(Integer.parseInt(reader.getElementText()));
                        break;

                    case "occupiedBays":
                        garage.setOccupiedBays(Integer.parseInt(reader.getElementText()));
                        break;

                    case "idNumber":
                        String idValue = reader.getElementText();
                        switch (context.peek()) {
                            case CTX_MECHANIC -> currentMechanic.setIdNumber(idValue);
                            case CTX_CUSTOMER -> currentCustomer.setIdNumber(idValue);
                        }
                        break;

                    case "phone":
                        String phoneValue = reader.getElementText();
                        switch (context.peek()) {
                            case CTX_MECHANIC -> currentMechanic.setPhone(phoneValue);
                            case CTX_CUSTOMER -> currentCustomer.setPhone(phoneValue);
                        }
                        break;

                    case "specialization":
                        currentMechanic.setSpecialization(reader.getElementText());
                        break;

                    case "yearsOfExperience":
                        currentMechanic.setYearsOfExperience(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "level":
                        currentMechanic.setLevel(reader.getElementText());
                        break;

                    case "hourlyRate":
                        currentMechanic.setHourlyRate(
                                new BigDecimal(reader.getElementText()));
                        break;

                    case "age":
                        currentCustomer.setAge(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "loyaltyPoints":
                        currentCustomer.setLoyaltyPoints(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "email":
                        currentCustomer.setEmail(reader.getElementText());
                        break;

                    case "provider":
                        currentInsurance.setProvider(reader.getElementText());
                        break;

                    case "policyNumber":
                        currentInsurance.setPolicyNumber(reader.getElementText());
                        break;

                    case "expiryDate":

                        currentInsurance.setExpiryDate(
                                LocalDate.parse(reader.getElementText()));
                        break;

                    case "monthlyPremium":
                        currentInsurance.setMonthlyPremium(
                                new BigDecimal(reader.getElementText()));
                        break;

                    case "tier":
                        currentInsurance.setTier(reader.getElementText());
                        break;

                    case "brand":
                        currentVehicle.setBrand(reader.getElementText());
                        break;

                    case "model":
                        currentVehicle.setModel(reader.getElementText());
                        break;

                    case "year":
                        currentVehicle.setYear(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "vin":
                        currentVehicle.setVin(reader.getElementText());
                        break;

                    case "licensePlate":
                        currentVehicle.setLicensePlate(reader.getElementText());
                        break;

                    case "doors":
                        currentVehicle.setDoors(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "engineType":
                        currentVehicle.setEngineType(reader.getElementText());
                        break;

                    case "engineSize":
                        currentVehicle.setEngineSize(
                                Double.parseDouble(reader.getElementText()));
                        break;

                    case "engineCapacity":
                        currentVehicle.setEngineCapacity(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "bikeType":
                        currentVehicle.setBikeType(reader.getElementText());
                        break;

                    case "tires":
                        currentVehicle.setTires(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "payloadCapacityTons":
                        currentVehicle.setPayloadCapacityTons(
                                Double.parseDouble(reader.getElementText()));
                        break;

                    case "hasSleepingCabin":
                        currentVehicle.setHasSleepingCabin(
                                Boolean.parseBoolean(reader.getElementText()));
                        break;

                    case "type":
                        currentTransmission.setType(reader.getElementText());
                        break;

                    case "gears":
                        currentTransmission.setGears(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "id":
                        currentAppointment.setId(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "scheduledTime":
                        currentAppointment.setScheduledTime(
                                LocalDateTime.parse(reader.getElementText()));
                        break;

                    case "status":
                        currentAppointment.setStatus(reader.getElementText());
                        break;

                    case "customerRef":
                        currentAppointment.setCustomerRef(reader.getElementText());
                        break;

                    case "mechanicRef":
                        currentAppointment.setMechanicRef(reader.getElementText());
                        break;

                    case "vehicleRef":
                        currentAppointment.setVehicleRef(reader.getElementText());
                        break;

                    // ── spare part scalars ────────────────────────────────────
                    case "productName":
                        currentSparePart.setProductName(reader.getElementText());
                        break;

                    case "productNumber":
                        currentSparePart.setProductNumber(reader.getElementText());
                        break;

                    case "unitPrice":
                        currentSparePart.setUnitPrice(
                                new BigDecimal(reader.getElementText()));
                        break;

                    case "quantity":
                        currentSparePart.setQuantity(
                                Integer.parseInt(reader.getElementText()));
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {

                String tag = reader.getLocalName();

                switch (tag) {

                    case CTX_TRANSMISSION:

                        if (currentVehicle != null) {
                            currentVehicle.setTransmission(currentTransmission);
                        }
                        currentTransmission = null;
                        context.pop();
                        break;

                    case CTX_INSURANCE:

                        if (currentCustomer != null) {
                            currentCustomer.setInsurance(currentInsurance);
                        }
                        currentInsurance = null;
                        context.pop();
                        break;

                    case CTX_MECHANIC:
                        mechanics.add(currentMechanic);
                        currentMechanic = null;
                        context.pop();
                        break;

                    case CTX_CUSTOMER:
                        customers.add(currentCustomer);
                        currentCustomer = null;
                        context.pop();
                        break;

                    case CTX_CAR:
                    case CTX_MOTORCYCLE:
                    case CTX_TRUCK:
                        vehicles.add(currentVehicle);
                        currentVehicle = null;
                        context.pop();
                        break;

                    case CTX_APPOINTMENT:
                        appointments.add(currentAppointment);
                        currentAppointment = null;
                        context.pop();
                        break;

                    case CTX_SPARE_PART:
                        spareParts.add(currentSparePart);
                        currentSparePart = null;
                        context.pop();
                        break;

                    case "mechanics":
                    case "customers":
                    case "vehicles":
                    case "appointments":
                    case "spareParts":
                    case CTX_GARAGE:
                        context.pop();
                        break;

                }
            }
        }

        reader.close();

        garage.setMechanics(mechanics);
        garage.setCustomers(customers);
        garage.setVehicles(vehicles);
        garage.setAppointments(appointments);
        garage.setSpareParts(spareParts);

        return garage;
    }

}
