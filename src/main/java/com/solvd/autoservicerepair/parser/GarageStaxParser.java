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

/**
 * StAX (Streaming API for XML) parser for garage.xml.
 * <p>
 * WHY StAX:
 * DOM — loads the entire file into memory as a tree. Simple to use but heavy.
 * SAX — event-driven like StAX but push-based: the parser calls YOUR methods.
 * StAX — event-driven and pull-based: YOU call reader.next() to ask for the
 * next event. This gives you full control of the loop, which makes the
 * code easier to read and debug for structured documents like ours.
 * <p>
 * HOW THE STATE MACHINE WORKS:
 * StAX fires three events we care about:
 * START_ELEMENT — opening tag, e.g. <mechanic>
 * CHARACTERS     — text content, e.g. Nika
 * END_ELEMENT    — closing tag,  e.g. </mechanic>
 * <p>
 * The problem: many elements share the same local name.
 * Both <garage> and <mechanic> and <customer> have a <name> child.
 * When StAX fires START_ELEMENT for "name", how do we know whose name it is?
 * <p>
 * SOLUTION — a context stack (Deque<String>).
 * Every time we open a structural element (mechanic, customer, insurance …),
 * we push its name onto the stack.
 * Every time we close one, we pop it.
 * At any point, stack.peek() tells us exactly where we are.
 * <p>
 * Example — while reading Nika's name, the stack looks like:
 * TOP → "mechanic"
 * "mechanics"
 * "garage"
 * So when we see the "name" START_ELEMENT, we check stack.peek() == "mechanic"
 * and know with certainty this belongs to the current mechanic.
 */
public class GarageStaxParser implements Parser<GarageXml> {

    // -------------------------------------------------------------------------
    // Context constants — string literals pushed onto / popped from the stack.
    // Using constants prevents typo bugs (the compiler catches "mechanic").
    // -------------------------------------------------------------------------
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

        // ── result collectors ────────────────────────────────────────────────
        GarageXml garage = new GarageXml();
        List<MechanicXml> mechanics = new ArrayList<>();
        List<CustomerXml> customers = new ArrayList<>();
        List<VehicleXml> vehicles = new ArrayList<>();
        List<AppointmentXml> appointments = new ArrayList<>();
        List<SparePartXml> spareParts = new ArrayList<>();

        // ── current-object pointers (null when not inside that element) ──────
        MechanicXml currentMechanic = null;
        CustomerXml currentCustomer = null;
        InsuranceXml currentInsurance = null;
        VehicleXml currentVehicle = null;
        TransmissionXml currentTransmission = null;
        AppointmentXml currentAppointment = null;
        SparePartXml currentSparePart = null;

        // ── THE CONTEXT STACK ────────────────────────────────────────────────
        // ArrayDeque is the recommended Deque implementation in Java.
        // push() adds to the front (top), peek() reads the front, pop() removes it.
        Deque<String> context = new ArrayDeque<>();

        // ── main event loop ──────────────────────────────────────────────────
        while (reader.hasNext()) {

            int event = reader.next();  // ask StAX for the next event

            // =================================================================
            // START_ELEMENT — an opening tag was found
            // =================================================================
            if (event == XMLStreamConstants.START_ELEMENT) {

                String tag = reader.getLocalName();
                // getLocalName() gives "name", "mechanic" etc. without a namespace prefix.

                switch (tag) {

                    // ── structural elements: push context, create object ──────

                    case CTX_GARAGE:
                        context.push(CTX_GARAGE);
                        break;

                    case "mechanics":
                    case "customers":
                    case "vehicles":
                    case "appointments":
                    case "spareParts":
                        // List wrapper elements — push so children know where they are,
                        // but we don't create any object for these themselves.
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

                    // ── leaf elements: read text and set on the correct object ─

                    /*
                     * "name" appears in three places: garage, mechanic, customer.
                     * We look at context.peek() to decide which object to populate.
                     *
                     * getElementText() is a StAX convenience method:
                     *   it reads the CHARACTERS event AND the END_ELEMENT event for us,
                     *   returning the trimmed text content as a String.
                     *   After calling it, the reader is positioned AFTER the closing tag,
                     *   so we must NOT handle this tag's END_ELEMENT separately.
                     */
                    case "name":
                        String nameValue = reader.getElementText();
                        switch (context.peek()) {
                            case CTX_GARAGE -> garage.setName(nameValue);
                            case CTX_MECHANIC -> currentMechanic.setName(nameValue);
                            case CTX_CUSTOMER -> currentCustomer.setName(nameValue);
                        }
                        break;

                    // ── garage scalars ────────────────────────────────────────
                    case "address":
                        garage.setAddress(reader.getElementText());
                        break;

                    case "totalBays":
                        garage.setTotalBays(Integer.parseInt(reader.getElementText()));
                        break;

                    case "occupiedBays":
                        garage.setOccupiedBays(Integer.parseInt(reader.getElementText()));
                        break;

                    // ── mechanic scalars ──────────────────────────────────────
                    /*
                     * "idNumber" also appears in both mechanic and customer.
                     * Same pattern: check context.peek().
                     */
                    case "idNumber":
                        String idValue = reader.getElementText();
                        switch (context.peek()) {
                            case CTX_MECHANIC -> currentMechanic.setIdNumber(idValue);
                            case CTX_CUSTOMER -> currentCustomer.setIdNumber(idValue);
                        }
                        break;

                    /*
                     * "phone" is the same situation.
                     */
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

                    // ── customer scalars ──────────────────────────────────────
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

                    // ── insurance scalars (context is already CTX_INSURANCE) ──
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

                    // ── vehicle scalars (shared by car / motorcycle / truck) ───
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

                    // ── car-specific ──────────────────────────────────────────
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

                    // ── motorcycle-specific ───────────────────────────────────
                    case "engineCapacity":
                        currentVehicle.setEngineCapacity(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "bikeType":
                        currentVehicle.setBikeType(reader.getElementText());
                        break;

                    // ── truck-specific ────────────────────────────────────────
                    case "tires":
                        currentVehicle.setTires(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    case "payloadCapacityTons":
                        currentVehicle.setPayloadCapacityTons(
                                Double.parseDouble(reader.getElementText()));
                        break;

                    /*
                     * hasSleepingCabin — this is the Boolean field required by the assignment.
                     * In XML, it is stored as "true" / "false".
                     * Boolean.parseBoolean("true") returns true, anything else returns false.
                     */
                    case "hasSleepingCabin":
                        currentVehicle.setHasSleepingCabin(
                                Boolean.parseBoolean(reader.getElementText()));
                        break;

                    // ── transmission scalars ──────────────────────────────────
                    /*
                     * "type" is a child of <transmission>.
                     * context.peek() == CTX_TRANSMISSION at this point.
                     * We don't need an extra check because "type" only exists inside transmission.
                     */
                    case "type":
                        currentTransmission.setType(reader.getElementText());
                        break;

                    case "gears":
                        currentTransmission.setGears(
                                Integer.parseInt(reader.getElementText()));
                        break;

                    // ── appointment scalars ───────────────────────────────────
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
            }

            // =================================================================
            // END_ELEMENT — a closing tag was found
            // =================================================================
            else if (event == XMLStreamConstants.END_ELEMENT) {

                String tag = reader.getLocalName();

                switch (tag) {

                    /*
                     * When we close a structural element:
                     *   1. Finalize the object (attach child objects, add to list).
                     *   2. Pop the context stack.
                     *   3. Null out the pointer so future leaf reads don't accidentally
                     *      write to a finished object.
                     */

                    case CTX_TRANSMISSION:
                        // Attach the finished transmission to whatever vehicle we're building.
                        if (currentVehicle != null) {
                            currentVehicle.setTransmission(currentTransmission);
                        }
                        currentTransmission = null;
                        context.pop();
                        break;

                    case CTX_INSURANCE:
                        // Attach the finished insurance to the customer we're building.
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

                    /*
                     * Leaf element closing tags (name, address, brand …) do NOT appear here.
                     * That is because we used getElementText() when we opened them.
                     * getElementText() consumes both the CHARACTERS and the END_ELEMENT events
                     * internally, so by the time our switch sees the next event, those closing
                     * tags are already gone.
                     */
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