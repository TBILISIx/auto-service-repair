package com.solvd.autoservicerepair.parsers;

import com.solvd.autoservicerepair.interfaces.Parser;
import com.solvd.autoservicerepair.parsers.xmltojavaobject.*;

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

/**
 * StAX (Streaming API for XML) parser for garage.xml.
 * <p>
 * DOM — loads the entire file into memory as a tree. Simple to use but heavy.
 * SAX — event-driven like StAX but push-based: the parser calls YOUR methods.
 * StAX — event-driven and pull-based: YOU call reader.next() to ask for the
 * next event. This gives full control of the loop, which makes the
 * code easier to read and debug for structured documents.
 * <p>
 * HOW it works:
 * StAX starts three events we care about:
 * START_ELEMENT — opening elementNameTag, e.g. <mechanic>
 * CHARACTERS     — text content, e.g. Nika
 * END_ELEMENT    — closing elementNameTag,  e.g. </mechanic>
 * <p>
 * The problem: many elements share the same local name.
 * Both <garage> and <mechanic> and <customer> have a <name> child.
 * When StAX fires START_ELEMENT for "name", how do we know whose name it is?
 *
 * <p>
 * SOLUTION — the idea of stack (Deque<String>).  (Double-Ended Queue, An Array where you can add or remove items from both ends.)
 * Every time we open a structural element (mechanic, customer, insurance …),
 * we push its name onto the stack. (stack.push → add on top)
 * At any point, stack.peek() tells us exactly where we are. (peek → look at the top)
 * Every time we close one, we pop it. (stack.pop → remove from top)
 * <p>
 *
 * <p>
 * Who is on top of the stack?
 * You enter "garage"
 * Then "mechanics"
 * Then "mechanic"
 * <p>
 * If here is a name: Nika.
 * Logically, I'll know:
 * “Oh, I’m inside a mechanic → this must be the mechanic’s name"
 * <p>
 * Example — while reading Nika's name, the stack looks like:
 * TOP → "mechanic"
 * "mechanics"
 * "garage"
 * So when we see the "name" START_ELEMENT, we check stack.peek() == "mechanic"
 * and know with certainty this belongs to the current mechanic.
 */

public class GarageStaxParser implements Parser<GarageXml> {

/*  -------------------------------------------------------------------------
    These temporary(TMP) constants represent element names from the XML file.
    I push them onto a stack and compare them during parsing to understand
    where we are in the XML structure (garage, mechanic, customer, etc.).
    Using constants instead of raw strings prevents typos and makes the code
    safer — for example, "mechnaic" would cause a compile-time error instead of silently breaking logic.
 ------------------------------------------------------------------------- */
    private static final String TMP_GARAGE = "garage";
    private static final String TMP_MECHANIC = "mechanic";
    private static final String TMP_CUSTOMER = "customer";
    private static final String TMP_INSURANCE = "insurance";
    private static final String TMP_CAR = "car";
    private static final String TMP_MOTORCYCLE = "motorcycle";
    private static final String TMP_TRUCK = "truck";
    private static final String TMP_TRANSMISSION = "transmission";
    private static final String TMP_APPOINTMENT = "appointment";
    private static final String TMP_SPARE_PART = "sparePart";

    // -------------------------------------------------------------------------
    // parse() — the only public method.
    // Returns a fully populated GarageXml (a simple data-holder, see below).
    // Throws Exception so the caller (Main / test) decides how to handle errors.
    // -------------------------------------------------------------------------

    @Override
    public GarageXml parse(String filePath) throws Exception {

        // XMLInputFactory is the entry point for StAX.
        // createXMLStreamReader wraps the raw byte stream and gives us the reader.
        XMLStreamReader reader = XMLInputFactory
                .newInstance()
                .createXMLStreamReader(new FileInputStream(filePath));

        // ---- collect results ------------------------------------------
        GarageXml garage = new GarageXml();
        List<MechanicXml> mechanics = new ArrayList<>();
        List<CustomerXml> customers = new ArrayList<>();
        List<VehicleXml> vehicles = new ArrayList<>();
        List<AppointmentXml> appointments = new ArrayList<>();
        List<SparePartXml> spareParts = new ArrayList<>();

        // current-object pointers (null when not inside that element)
        MechanicXml currentMechanic = null;
        CustomerXml currentCustomer = null;
        InsuranceXml currentInsurance = null;
        VehicleXml currentVehicle = null;
        TransmissionXml currentTransmission = null;
        AppointmentXml currentAppointment = null;
        SparePartXml currentSparePart = null;

        // ---- The Temporary Constants (TMP_"elementNameFromXml") STACK ----------------

        // ArrayDeque (Array where you can add or remove items from both ends,
        // but in my case method to quickly add/remove from one side (top of stack))
        // push() adds to the front (top), peek() reads the front, pop() removes it.

        Deque<String> xmlElementStackInMemory = new ArrayDeque<>();

        // ---- main event loop ----------------------------------------------------------------------
        while (reader.hasNext()) {

            int event = reader.next();  // ask StAX for the next event

            // =================================================================
            // START_ELEMENT — an opening elementNameTag was found
            // =================================================================

            if (event == XMLStreamConstants.START_ELEMENT) {

                String elementNameTag = reader.getLocalName();
                // getLocalName() gives "name", "mechanic" etc.

                switch (elementNameTag) {

                    // ---- structural elements: push Temporar Constant, create object ------------

                    case TMP_GARAGE:
                        xmlElementStackInMemory.push(TMP_GARAGE);
                        break;

                    case "mechanics":
                    case "customers":
                    case "vehicles":
                    case "appointments":
                    case "spareParts":
                        xmlElementStackInMemory.push(elementNameTag);
                        break;

                    case TMP_MECHANIC:
                        currentMechanic = new MechanicXml();
                        xmlElementStackInMemory.push(TMP_MECHANIC);
                        break;

                    case TMP_CUSTOMER:
                        currentCustomer = new CustomerXml();
                        xmlElementStackInMemory.push(TMP_CUSTOMER);
                        break;

                    case TMP_INSURANCE:
                        currentInsurance = new InsuranceXml();
                        xmlElementStackInMemory.push(TMP_INSURANCE);
                        break;

                    case TMP_CAR:
                        currentVehicle = new VehicleXml();
                        currentVehicle.setType("car");
                        xmlElementStackInMemory.push(TMP_CAR);
                        break;

                    case TMP_MOTORCYCLE:
                        currentVehicle = new VehicleXml();
                        currentVehicle.setType("motorcycle");
                        xmlElementStackInMemory.push(TMP_MOTORCYCLE);
                        break;

                    case TMP_TRUCK:
                        currentVehicle = new VehicleXml();
                        currentVehicle.setType("truck");
                        xmlElementStackInMemory.push(TMP_TRUCK);
                        break;

                    case TMP_TRANSMISSION:
                        currentTransmission = new TransmissionXml();
                        xmlElementStackInMemory.push(TMP_TRANSMISSION);
                        break;

                    case TMP_APPOINTMENT:
                        currentAppointment = new AppointmentXml();
                        xmlElementStackInMemory.push(TMP_APPOINTMENT);
                        break;

                    case TMP_SPARE_PART:
                        currentSparePart = new SparePartXml();
                        xmlElementStackInMemory.push(TMP_SPARE_PART);
                        break;

                    /*
                     * "name" appears in three places: garage, mechanic, customer.
                     * with context.peek() we decide which object to populate.
                     *
                     * getElementText() is a StAX convenience method:
                     *   it reads the CHARACTERS event AND the END_ELEMENT event for us,
                     *   returning the trimmed text content as a String.
                     * Example:
                     * <name> Nika </name>
                     * becomes:
                     * "Nika"
                     *
                     *   After calling it, the reader is positioned AFTER the closing elementNameTag,
                     *   so we must NOT handle this elementNameTag's END_ELEMENT separately.
                     */

                    case "name":
                        String nameValue = reader.getElementText();
                        switch (xmlElementStackInMemory.peek()) {
                            case TMP_GARAGE -> garage.setName(nameValue);
                            case TMP_MECHANIC -> currentMechanic.setName(nameValue);
                            case TMP_CUSTOMER -> currentCustomer.setName(nameValue);
                        }
                        break;

                    // ---- garage fields --------------------------------------------------
                    case "address":
                        garage.setAddress(reader.getElementText());
                        break;

                    case "totalBays":
                        garage.setTotalBays(Integer.parseInt(reader.getElementText()));
                        break;

                    case "occupiedBays":
                        garage.setOccupiedBays(Integer.parseInt(reader.getElementText()));
                        break;

                    // ---- mechanic fields ----------------------------------------------
                    /*
                     * "idNumber" also appears in both mechanic and customer.
                     * Same pattern: check context.peek().
                     */
                    case "idNumber":
                        String idValue = reader.getElementText();
                        switch (xmlElementStackInMemory.peek()) {
                            case TMP_MECHANIC -> currentMechanic.setIdNumber(idValue);
                            case TMP_CUSTOMER -> currentCustomer.setIdNumber(idValue);
                        }
                        break;

                    /*
                     * "phone" is the same situation.
                     */
                    case "phone":
                        String phoneValue = reader.getElementText();
                        switch (xmlElementStackInMemory.peek()) {
                            case TMP_MECHANIC -> currentMechanic.setPhone(phoneValue);
                            case TMP_CUSTOMER -> currentCustomer.setPhone(phoneValue);
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

                    // ---- customer fields ----------------------------------------------
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

                    // ---- insurance fields (we are already on TMP_INSURANCE) ----
                    case "provider":
                        currentInsurance.setProvider(reader.getElementText());
                        break;

                    case "policyNumber":
                        currentInsurance.setPolicyNumber(reader.getElementText());
                        break;

                    case "expiryDate":
                        // xs:date format is YYYY-MM-DD — LocalDate.parse handles this natively.
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

                    // ---- vehicle fields (shared by car / motorcycle / truck) ----
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

                    // ---- car-specific ------------------------------------------------------
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

                    // ---- motorcycle-specific --------------------------------------------------------------------
                    case "engineCapacity":
                        currentVehicle.setEngineCapacity(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "bikeType":
                        currentVehicle.setBikeType(reader.getElementText());
                        break;

                    // ---- truck-specific --------------------------------------------------
                    case "tires":
                        currentVehicle.setTires(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "payloadCapacityTons":
                        currentVehicle.setPayloadCapacityTons(
                                Double.parseDouble(reader.getElementText()));
                        break;

                    /*
                     * hasSleepingCabin — this is the Boolean field.
                     * In XML, it is stored as "true" / "false".
                     * Boolean.parseBoolean("true") returns true, anything else returns false.
                     */
                    case "hasSleepingCabin":
                        currentVehicle.setHasSleepingCabin(
                                Boolean.parseBoolean(reader.getElementText()));
                        break;

                    // ---- transmission fields ------------------------------------------
                    /*
                     * "type" is a child of <transmission>.
                     * context.peek() == TMP_TRANSMISSION at this point.
                     */
                    case "type":
                        currentTransmission.setType(reader.getElementText());
                        break;

                    case "gears":
                        currentTransmission.setGears(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    // ---- appointment scalars ------------------------------------------
                    case "id":
                        currentAppointment.setId(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    /*
                     * scheduledTime — this is the LocalDateTime field required by the assignment.
                     * Format in XML: 2026-04-23T10:00:00 (ISO 8601, xs:dateTime).
                     * LocalDateTime.parse() handles this format natively — no custom formatter is needed.
                     */
                    case "scheduledTime":
                        currentAppointment.setScheduledTime(
                                LocalDateTime.parse(reader.getElementText()));
                        break;

                    case "status":
                        currentAppointment.setStatus(reader.getElementText());
                        break;

                    /*
                     * Ref fields — these are foreign-key strings.
                     * After parsing everything, we resolve them to actual objects
                     * in the resolveRefs() method below.
                     */
                    case "customerRef":
                        currentAppointment.setCustomerRef(reader.getElementText());
                        break;

                    case "mechanicRef":
                        currentAppointment.setMechanicRef(reader.getElementText());
                        break;

                    case "vehicleRef":
                        currentAppointment.setVehicleRef(reader.getElementText());
                        break;

                    // ---- spare part scalars ------------------------------------------
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
            }

            // =================================================================
            // END_ELEMENT — a closing elementNameTag was found
            // =================================================================
            else if (event == XMLStreamConstants.END_ELEMENT) {

                String elementNameTag = reader.getLocalName();

                switch (elementNameTag) {

                    /*
                     * When we close a structural element:
                     *   1. Finalize the object (attach child objects, add to list).
                     *   2. Pop the context stack.
                     *   3. Null out the pointer so future leaf reads don't accidentally
                     *      write to a finished object.
                     */

                    case TMP_TRANSMISSION:
                        // Attach the finished transmission to whatever vehicle we're building.
                        if (currentVehicle != null) {
                            currentVehicle.setTransmission(currentTransmission);
                        }
                        currentTransmission = null;
                        xmlElementStackInMemory.pop();
                        break;

                    case TMP_INSURANCE:
                        // Attach the finished insurance to the customer we're building.
                        if (currentCustomer != null) {
                            currentCustomer.setInsurance(currentInsurance);
                        }
                        currentInsurance = null;
                        xmlElementStackInMemory.pop();
                        break;

                    case TMP_MECHANIC:
                        mechanics.add(currentMechanic);
                        currentMechanic = null;
                        xmlElementStackInMemory.pop();
                        break;

                    case TMP_CUSTOMER:
                        customers.add(currentCustomer);
                        currentCustomer = null;
                        xmlElementStackInMemory.pop();
                        break;

                    case TMP_CAR:
                    case TMP_MOTORCYCLE:
                    case TMP_TRUCK:
                        vehicles.add(currentVehicle);
                        currentVehicle = null;
                        xmlElementStackInMemory.pop();
                        break;

                    case TMP_APPOINTMENT:
                        appointments.add(currentAppointment);
                        currentAppointment = null;
                        xmlElementStackInMemory.pop();
                        break;

                    case TMP_SPARE_PART:
                        spareParts.add(currentSparePart);
                        currentSparePart = null;
                        xmlElementStackInMemory.pop();
                        break;

                    case "mechanics":
                    case "customers":
                    case "vehicles":
                    case "appointments":
                    case "spareParts":
                    case TMP_GARAGE:
                        xmlElementStackInMemory.pop();
                        break;
                }
            }
        }

        reader.close();

        // Attach all lists to the garage object.
        garage.setMechanics(mechanics);
        garage.setCustomers(customers);
        garage.setVehicles(vehicles);
        garage.setAppointments(appointments);
        garage.setSpareParts(spareParts);

        return garage;
    }

}