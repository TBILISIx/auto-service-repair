package com.solvd.autoservicerepair.parsers;

import com.solvd.autoservicerepair.interfaces.Parser;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import com.solvd.autoservicerepair.parsers.xmltojavaobject.*;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JAXB parser for garage.xml.
 *
 * HOW JAXB WORKS:
 *   StAX — you write every single case manually in a while loop.
 *   JAXB — you put annotations on your classes and JAXB does ALL the
 *           reading automatically. You write almost no parsing logic.
 *
 *   "Unmarshal" = read XML and produce Java objects (what we do here).
 *   "Marshal"= take Java objects and write XML(the opposite).
 *
 * THREE STEPS — that is the entire parser logic:
 *   1. JAXBContext.newInstance(JaxbGarageXml.class)
 *      Tells JAXB which class is the root and discovers all nested classes
 *      automatically through the field types.
 *
 *   2. context.createUnmarshaller()
 *      Creates the Unmarshaller — the object that does the actual reading.
 *
 *   3. unmarshaller.unmarshal(new File(filePath))
 *      Reads the entire XML file and returns a fully populated object.
 *      This single line replaces the entire while loop in GarageStaxParser.
 *
 * WHY INNER CLASSES:
 *    My POJO (plain old java object) classes I created for StaX parser have no JAXB annotations.
 *   Rather than modifying them (mixing StAX and JAXB concerns), its better to keep a
 *   self-contained set of JAXB-annotated classes right here inside this parser.
 *
 * WHY THREE VEHICLE SUBCLASSES (JaxbCarXml, JaxbMotorcycleXml, JaxbTruckXml):
 *   JAXB maps XML element *content* to Java fields — it has no mechanism to inject
 *   the element *name itself* (<car>, <motorcycle>, <truck>) into a field on a single
 *   flat class. In GarageStaxParser this was done manually: on START_ELEMENT "car" we
 *   called currentVehicle.setType("car"). JAXB cannot do that automatically.
 *   The fix: give each element name its own class. The class identity IS the type.
 *   @XmlElements maps each element name to its dedicated subclass, so after unmarshal
 *   you call instanceof (or getType() on the base) to know which vehicle it is.
 *
 * JAXB ANNOTATIONS EXPLAINED:
 *
 *   @XmlRootElement(name = "garage")
 *     Marks this class as the root XML element.
 *     The name must match the root tag in the XML file exactly.
 *     Only the root class needs this annotation.
 *
 *   @XmlAccessorType(XmlAccessType.FIELD)
 *     Tells JAXB to read annotations from FIELDS, not from getters.
 *     Without this, JAXB looks for getters — which Lombok generates at
 *     compile time and JAXB cannot see at annotation-scan time.
 *
 *   @XmlElement(name = "mechanic")
 *     Maps this field to an XML element with that exact name.
 *
 *   @XmlElementWrapper(name = "mechanics")
 *     Maps the outer wrapper element — the <mechanics> tag.
 *     Always used together with @XmlElement below it.
 *     @XmlElementWrapper = the container tag.
 *     @XmlElement        = each item inside the container.
 *
 *   @XmlElements({...})
 *     Used when one list can hold multiple different element names.
 *     Our <vehicles> wrapper contains <car>, <motorcycle>, <truck>.
 *     Each @XmlElement maps one element name to its own dedicated Java class,
 *     so JAXB knows exactly which class to instantiate for each element name.
 *
 *   @XmlJavaTypeAdapter(LocalDateAdapter.class)
 *     JAXB predates Java 8 and does not know LocalDate or LocalDateTime.
 *     The adapter is a converter — it tells JAXB:
 *     "take the String from the XML, call unmarshal(), get a LocalDate back."
 */

public class GarageJaxbParser implements Parser {

    @Override
    public GarageXml parse(String filePath) throws Exception {

        // Step 1 — create context with the root class
        JAXBContext context = JAXBContext.newInstance(JaxbGarageXml.class);

        // Step 2 — create the unmarshaller
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Step 3 — let JAXB unmarshal into the private inner class
        JaxbGarageXml jaxb = (JaxbGarageXml) unmarshaller.unmarshal(new File(filePath));

        // Step 4 — map the JAXB inner class into the shared GarageXml
        // so the return type matches the Parser interface (same as StAX and Jackson).
        return toGarageXml(jaxb);
    }

    // =========================================================================
    // Mapping — converts JAXB inner objects into the shared xmltojavaobject types.
    // This keeps the JAXB annotations isolated inside this class while still
    // returning the same GarageXml that StAX and Jackson return.
    // =========================================================================

    private GarageXml toGarageXml(JaxbGarageXml j) {
        GarageXml g = new GarageXml();
        g.setName(j.name);
        g.setAddress(j.address);
        g.setTotalBays(j.totalBays);
        g.setOccupiedBays(j.occupiedBays);

        // mechanics
        List<MechanicXml> mechanics = new ArrayList<>();
        if (j.mechanics != null) {
            for (JaxbMechanicXml m : j.mechanics) {
                MechanicXml mx = new MechanicXml();
                mx.setName(m.name);
                mx.setIdNumber(m.idNumber);
                mx.setPhone(m.phone);
                mx.setSpecialization(m.specialization);
                mx.setYearsOfExperience(m.yearsOfExperience);
                mx.setLevel(m.level);
                mx.setHourlyRate(m.hourlyRate);
                mechanics.add(mx);
            }
        }
        g.setMechanics(mechanics);

        // customers
        List<CustomerXml> customers = new ArrayList<>();
        if (j.customers != null) {
            for (JaxbCustomerXml c : j.customers) {
                CustomerXml cx = new CustomerXml();
                cx.setName(c.name);
                cx.setIdNumber(c.idNumber);
                cx.setPhone(c.phone);
                cx.setAge(c.age);
                cx.setLoyaltyPoints(c.loyaltyPoints);
                cx.setEmail(c.email);
                if (c.insurance != null) {
                    InsuranceXml ix = new InsuranceXml();
                    ix.setProvider(c.insurance.provider);
                    ix.setPolicyNumber(c.insurance.policyNumber);
                    ix.setExpiryDate(c.insurance.expiryDate);
                    ix.setMonthlyPremium(c.insurance.monthlyPremium);
                    ix.setTier(c.insurance.tier);
                    cx.setInsurance(ix);
                }
                customers.add(cx);
            }
        }
        g.setCustomers(customers);

        // vehicles — use instanceof to recover the type string since JAXB
        // cannot inject the element name into a field automatically.
        List<VehicleXml> vehicles = new ArrayList<>();
        if (j.vehicles != null) {
            for (JaxbBaseVehicleXml v : j.vehicles) {
                VehicleXml vx = new VehicleXml();
                vx.setBrand(v.brand);
                vx.setModel(v.model);
                vx.setYear(v.year);
                vx.setVin(v.vin);
                vx.setLicensePlate(v.licensePlate);
                if (v.transmission != null) {
                    TransmissionXml tx = new TransmissionXml();
                    tx.setType(v.transmission.type);
                    tx.setGears(v.transmission.gears);
                    vx.setTransmission(tx);
                }
                if (v instanceof JaxbCarXml car) {
                    vx.setType("car");
                    vx.setDoors(car.doors);
                    vx.setEngineType(car.engineType);
                    vx.setEngineSize(car.engineSize);
                } else if (v instanceof JaxbMotorcycleXml moto) {
                    vx.setType("motorcycle");
                    vx.setEngineCapacity(moto.engineCapacity);
                    vx.setEngineType(moto.engineType);
                    vx.setBikeType(moto.bikeType);
                } else if (v instanceof JaxbTruckXml truck) {
                    vx.setType("truck");
                    vx.setDoors(truck.doors);
                    vx.setTires(truck.tires);
                    vx.setEngineType(truck.engineType);
                    vx.setEngineSize(truck.engineSize);
                    vx.setPayloadCapacityTons(truck.payloadCapacityTons);
                    vx.setHasSleepingCabin(truck.hasSleepingCabin);
                }
                vehicles.add(vx);
            }
        }
        g.setVehicles(vehicles);

        // appointments
        List<AppointmentXml> appointments = new ArrayList<>();
        if (j.appointments != null) {
            for (JaxbAppointmentXml a : j.appointments) {
                AppointmentXml ax = new AppointmentXml();
                ax.setId(a.id);
                ax.setScheduledTime(a.scheduledTime);
                ax.setStatus(a.status);
                ax.setCustomerRef(a.customerRef);
                ax.setMechanicRef(a.mechanicRef);
                ax.setVehicleRef(a.vehicleRef);
                appointments.add(ax);
            }
        }
        g.setAppointments(appointments);

        // spare parts
        List<SparePartXml> spareParts = new ArrayList<>();
        if (j.spareParts != null) {
            for (JaxbSparePartXml s : j.spareParts) {
                SparePartXml sx = new SparePartXml();
                sx.setProductName(s.productName);
                sx.setProductNumber(s.productNumber);
                sx.setUnitPrice(s.unitPrice);
                sx.setQuantity(s.quantity);
                spareParts.add(sx);
            }
        }
        g.setSpareParts(spareParts);

        return g;
    }

    // =========================================================================
    // DATE/TIME ADAPTERS
    // JAXB does not support Java 8 date types natively.
    // XmlAdapter<String, TargetType> converts between the XML string and the type.
    // =========================================================================

    public static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
        @Override
        public LocalDate unmarshal(String localDate) {
            // "2026-12-31" -> LocalDate (ISO format, no formatter needed)
            return LocalDate.parse(localDate);
        }
        @Override
        public String marshal(LocalDate localDate) {
            return localDate.toString();
        }
    }

    public static class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
        @Override
        public LocalDateTime unmarshal(String localDateTime) {
            // "2026-04-23T10:00:00" -> LocalDateTime
            return LocalDateTime.parse(localDateTime);
        }
        @Override
        public String marshal(LocalDateTime localDateTime) {
            return localDateTime.toString();
        }
    }

    // =========================================================================
    // JAXB INNER CLASSES
    // Private to this parser — the outside world only sees GarageXml.
    // =========================================================================

    @XmlRootElement(name = "garage")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbGarageXml {
        @XmlElement(name = "name")         String name;
        @XmlElement(name = "address")      String address;
        @XmlElement(name = "totalBays")    int    totalBays;
        @XmlElement(name = "occupiedBays") int    occupiedBays;

        @XmlElementWrapper(name = "mechanics")
        @XmlElement(name = "mechanic")
        List<JaxbMechanicXml> mechanics;

        @XmlElementWrapper(name = "customers")
        @XmlElement(name = "customer")
        List<JaxbCustomerXml> customers;

        /*
         * @XmlElements — each element name maps to its own dedicated class.
         * JAXB instantiates JaxbCarXml for <car>, JaxbMotorcycleXml for <motorcycle>,
         * and JaxbTruckXml for <truck>. The class identity replaces the "type" String
         * field that GarageStaxParser set manually. We recover the type string in
         * toGarageXml() using instanceof.
         */
        @XmlElementWrapper(name = "vehicles")
        @XmlElements({
                @XmlElement(name = "car",        type = JaxbCarXml.class),
                @XmlElement(name = "motorcycle", type = JaxbMotorcycleXml.class),
                @XmlElement(name = "truck",      type = JaxbTruckXml.class)
        })
        List<JaxbBaseVehicleXml> vehicles;

        @XmlElementWrapper(name = "appointments")
        @XmlElement(name = "appointment")
        List<JaxbAppointmentXml> appointments;

        @XmlElementWrapper(name = "spareParts")
        @XmlElement(name = "sparePart")
        List<JaxbSparePartXml> spareParts;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbMechanicXml {
        @XmlElement(name = "name")              String     name;
        @XmlElement(name = "idNumber")          String     idNumber;
        @XmlElement(name = "phone")             String     phone;
        @XmlElement(name = "specialization")    String     specialization;
        @XmlElement(name = "yearsOfExperience") int        yearsOfExperience;
        @XmlElement(name = "level")             String     level;
        @XmlElement(name = "hourlyRate")        BigDecimal hourlyRate;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbInsuranceXml {
        @XmlElement(name = "provider")       String     provider;
        @XmlElement(name = "policyNumber")   String     policyNumber;
        @XmlElement(name = "expiryDate")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        LocalDate  expiryDate;
        @XmlElement(name = "monthlyPremium") BigDecimal monthlyPremium;
        @XmlElement(name = "tier")           String     tier;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbCustomerXml {
        @XmlElement(name = "name")          String          name;
        @XmlElement(name = "idNumber")      String          idNumber;
        @XmlElement(name = "phone")         String          phone;
        @XmlElement(name = "age")           int             age;
        @XmlElement(name = "loyaltyPoints") int             loyaltyPoints;
        @XmlElement(name = "email")         String          email;
        @XmlElement(name = "insurance")     JaxbInsuranceXml insurance;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbTransmissionXml {
        @XmlElement(name = "type")  String type;
        @XmlElement(name = "gears") int    gears;
    }

    // Base vehicle — shared fields only
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbBaseVehicleXml {
        @XmlElement(name = "brand")        String              brand;
        @XmlElement(name = "model")        String              model;
        @XmlElement(name = "year")         int                 year;
        @XmlElement(name = "vin")          String              vin;
        @XmlElement(name = "licensePlate") String              licensePlate;
        @XmlElement(name = "transmission") JaxbTransmissionXml transmission;
    }

    // Car extends base with car-specific fields
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbCarXml extends JaxbBaseVehicleXml {
        @XmlElement(name = "doors")      int    doors;
        @XmlElement(name = "engineType") String engineType;
        @XmlElement(name = "engineSize") double engineSize;
    }

    // Motorcycle extends base with motorcycle-specific fields
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbMotorcycleXml extends JaxbBaseVehicleXml {
        @XmlElement(name = "engineCapacity") int    engineCapacity;
        @XmlElement(name = "engineType")     String engineType;
        @XmlElement(name = "bikeType")       String bikeType;
    }

    // Truck extends base with truck-specific fields
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbTruckXml extends JaxbBaseVehicleXml {
        @XmlElement(name = "doors")               int     doors;
        @XmlElement(name = "tires")               int     tires;
        @XmlElement(name = "engineType")          String  engineType;
        @XmlElement(name = "engineSize")          double  engineSize;
        @XmlElement(name = "payloadCapacityTons") double  payloadCapacityTons;
        @XmlElement(name = "hasSleepingCabin")    boolean hasSleepingCabin;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbAppointmentXml {
        @XmlElement(name = "id")           int           id;
        @XmlElement(name = "scheduledTime")
        @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
        LocalDateTime scheduledTime;
        @XmlElement(name = "status")       String        status;
        @XmlElement(name = "customerRef")  String        customerRef;
        @XmlElement(name = "mechanicRef")  String        mechanicRef;
        @XmlElement(name = "vehicleRef")   String        vehicleRef;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class JaxbSparePartXml {
        @XmlElement(name = "productName")   String     productName;
        @XmlElement(name = "productNumber") String     productNumber;
        @XmlElement(name = "unitPrice")     BigDecimal unitPrice;
        @XmlElement(name = "quantity")      int        quantity;
    }

}