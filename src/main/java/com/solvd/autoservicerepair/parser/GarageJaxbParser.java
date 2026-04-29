package com.solvd.autoservicerepair.parser;

import com.solvd.autoservicerepair.interfaces.Parser;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JAXB parser for garage.xml.
 *
 * HOW JAXB WORKS:
 *   StAX — you write every single case manually in a while loop.
 *   JAXB — you put annotations on your classes and JAXB does ALL the
 *           reading automatically. You write almost no parsing logic.
 *
 *   "Unmarshal" = read XML and produce Java objects  (what we do here).
 *   "Marshal"   = take Java objects and write XML    (the opposite).
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
 *   Your existing Xml classes in xmltojavaobject have no JAXB annotations.
 *   Rather than modifying them (mixing StAX and JAXB concerns), we keep a
 *   self-contained set of JAXB-annotated classes right here inside this parser.
 *   Same pattern as your original StAX parser had inner classes.
 *
 * JAXB ANNOTATIONS EXPLAINED:
 *
 *   @XmlRootElement(name = "garage")
 *     Marks this class as the root XML element.
 *     The name must match the root tag in the XML file exactly.
 *     Only the root class needs this annotation.
 *
 *   @XmlAccessorType(XmlAccessType.FIELD)
 *     Tells JAXB to read annotations from FIELDS not from getters.
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
 *     @XmlElements lists all three and maps them to the same Java class.
 *
 *   @XmlJavaTypeAdapter(LocalDateAdapter.class)
 *     JAXB predates Java 8 and does not know LocalDate or LocalDateTime.
 *     The adapter is a converter — it tells JAXB:
 *     "take the String from the XML, call unmarshal(), get a LocalDate back."
 */
public class GarageJaxbParser implements Parser<GarageJaxbParser.JaxbGarageXml> {

    @Override
    public JaxbGarageXml parse(String filePath) throws Exception {

        // Step 1 — create context with the root class
        JAXBContext context = JAXBContext.newInstance(JaxbGarageXml.class);

        // Step 2 — create the unmarshaller
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Step 3 — parse and return
        return (JaxbGarageXml) unmarshaller.unmarshal(new File(filePath));
    }

    // =========================================================================
    // DATE/TIME ADAPTERS
    // JAXB does not support Java 8 date types natively.
    // XmlAdapter<String, TargetType> converts between the XML string and the type.
    // =========================================================================

    public static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
        @Override
        public LocalDate unmarshal(String s) {
            // "2026-12-31" → LocalDate   (ISO format, no formatter needed)
            return LocalDate.parse(s);
        }
        @Override
        public String marshal(LocalDate d) {
            return d.toString();
        }
    }

    public static class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
        @Override
        public LocalDateTime unmarshal(String s) {
            // "2026-04-23T10:00:00" → LocalDateTime   (ISO format, no formatter needed)
            return LocalDateTime.parse(s);
        }
        @Override
        public String marshal(LocalDateTime dt) {
            return dt.toString();
        }
    }

    // =========================================================================
    // JAXB DATA CLASSES — one per XML structure, mirroring your xmltojavaobject
    // classes exactly in terms of fields, but with JAXB annotations added.
    // =========================================================================

    // ── Root ──────────────────────────────────────────────────────────────────
    @XmlRootElement(name = "garage")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JaxbGarageXml {

        @XmlElement(name = "name")
        private String name;

        @XmlElement(name = "address")
        private String address;

        @XmlElement(name = "totalBays")
        private int totalBays;

        @XmlElement(name = "occupiedBays")
        private int occupiedBays;

        @XmlElementWrapper(name = "mechanics")
        @XmlElement(name = "mechanic")
        private List<JaxbMechanicXml> mechanics;

        @XmlElementWrapper(name = "customers")
        @XmlElement(name = "customer")
        private List<JaxbCustomerXml> customers;

        /*
         * @XmlElements (plural) — handles the case where one list wrapper
         * contains elements with different names: <car>, <motorcycle>, <truck>.
         * Each @XmlElement inside maps one element name to a Java class.
         * Since your VehicleXml is a flat class covering all three vehicle types,
         * we map all three element names to JaxbVehicleXml.
         */
        @XmlElementWrapper(name = "vehicles")
        @XmlElements({
                @XmlElement(name = "car",        type = JaxbVehicleXml.class),
                @XmlElement(name = "motorcycle", type = JaxbVehicleXml.class),
                @XmlElement(name = "truck",      type = JaxbVehicleXml.class)
        })
        private List<JaxbVehicleXml> vehicles;

        @XmlElementWrapper(name = "appointments")
        @XmlElement(name = "appointment")
        private List<JaxbAppointmentXml> appointments;

        @XmlElementWrapper(name = "spareParts")
        @XmlElement(name = "sparePart")
        private List<JaxbSparePartXml> spareParts;

        public String getName()                           { return name; }
        public String getAddress()                        { return address; }
        public int getTotalBays()                         { return totalBays; }
        public int getOccupiedBays()                      { return occupiedBays; }
        public List<JaxbMechanicXml> getMechanics()      { return mechanics; }
        public List<JaxbCustomerXml> getCustomers()      { return customers; }
        public List<JaxbVehicleXml> getVehicles()        { return vehicles; }
        public List<JaxbAppointmentXml> getAppointments(){ return appointments; }
        public List<JaxbSparePartXml> getSpareParts()    { return spareParts; }

        @Override
        public String toString() {
            return "JaxbGarage{name='" + name + "', address='" + address +
                    "', totalBays=" + totalBays + ", occupiedBays=" + occupiedBays +
                    ", mechanics=" + (mechanics != null ? mechanics.size() : 0) +
                    ", customers=" + (customers != null ? customers.size() : 0) +
                    ", vehicles=" + (vehicles != null ? vehicles.size() : 0) +
                    ", appointments=" + (appointments != null ? appointments.size() : 0) +
                    ", spareParts=" + (spareParts != null ? spareParts.size() : 0) + "}";
        }
    }

    // ── Mechanic ──────────────────────────────────────────────────────────────
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JaxbMechanicXml {

        @XmlElement(name = "name")              private String     name;
        @XmlElement(name = "idNumber")          private String     idNumber;
        @XmlElement(name = "phone")             private String     phone;
        @XmlElement(name = "specialization")    private String     specialization;
        @XmlElement(name = "yearsOfExperience") private int        yearsOfExperience;
        @XmlElement(name = "level")             private String     level;
        @XmlElement(name = "hourlyRate")        private BigDecimal hourlyRate;

        public String getName()              { return name; }
        public String getIdNumber()          { return idNumber; }
        public String getPhone()             { return phone; }
        public String getSpecialization()    { return specialization; }
        public int getYearsOfExperience()    { return yearsOfExperience; }
        public String getLevel()             { return level; }
        public BigDecimal getHourlyRate()    { return hourlyRate; }

        @Override
        public String toString() {
            return "Mechanic{name='" + name + "', level='" + level +
                    "', hourlyRate=" + hourlyRate + "}";
        }
    }

    // ── Insurance ─────────────────────────────────────────────────────────────
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JaxbInsuranceXml {

        @XmlElement(name = "provider")       private String     provider;
        @XmlElement(name = "policyNumber")   private String     policyNumber;

        // LocalDate needs an adapter — JAXB does not support it natively
        @XmlElement(name = "expiryDate")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate expiryDate;

        @XmlElement(name = "monthlyPremium") private BigDecimal monthlyPremium;
        @XmlElement(name = "tier")           private String     tier;

        public String getProvider()           { return provider; }
        public String getPolicyNumber()       { return policyNumber; }
        public LocalDate getExpiryDate()      { return expiryDate; }
        public BigDecimal getMonthlyPremium() { return monthlyPremium; }
        public String getTier()               { return tier; }
        public boolean isExpired()            { return LocalDate.now().isAfter(expiryDate); }

        @Override
        public String toString() {
            return "Insurance{provider='" + provider + "', tier='" + tier +
                    "', expired=" + isExpired() + "}";
        }
    }

    // ── Customer ──────────────────────────────────────────────────────────────
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JaxbCustomerXml {

        @XmlElement(name = "name")          private String          name;
        @XmlElement(name = "idNumber")      private String          idNumber;
        @XmlElement(name = "phone")         private String          phone;
        @XmlElement(name = "age")           private int             age;
        @XmlElement(name = "loyaltyPoints") private int             loyaltyPoints;
        @XmlElement(name = "email")         private String          email;
        @XmlElement(name = "insurance")     private JaxbInsuranceXml insurance;

        public String getName()               { return name; }
        public String getIdNumber()           { return idNumber; }
        public String getPhone()              { return phone; }
        public int getAge()                   { return age; }
        public int getLoyaltyPoints()         { return loyaltyPoints; }
        public String getEmail()              { return email; }
        public JaxbInsuranceXml getInsurance(){ return insurance; }

        @Override
        public String toString() {
            return "Customer{name='" + name + "', email='" + email +
                    "', loyaltyPoints=" + loyaltyPoints + "}";
        }
    }

    // ── Transmission ──────────────────────────────────────────────────────────
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JaxbTransmissionXml {

        @XmlElement(name = "type")  private String type;
        @XmlElement(name = "gears") private int    gears;

        public String getType()  { return type; }
        public int getGears()    { return gears; }

        @Override
        public String toString() {
            return "Transmission{type='" + type + "', gears=" + gears + "}";
        }
    }

    // ── Vehicle — flat class mirroring your VehicleXml exactly ───────────────
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JaxbVehicleXml {

        // shared
        @XmlElement(name = "brand")               private String              brand;
        @XmlElement(name = "model")               private String              model;
        @XmlElement(name = "year")                private int                 year;
        @XmlElement(name = "vin")                 private String              vin;
        @XmlElement(name = "licensePlate")        private String              licensePlate;
        @XmlElement(name = "transmission")        private JaxbTransmissionXml transmission;

        // car + truck
        @XmlElement(name = "doors")               private int                 doors;
        @XmlElement(name = "engineType")          private String              engineType;
        @XmlElement(name = "engineSize")          private double              engineSize;

        // motorcycle only
        @XmlElement(name = "engineCapacity")      private int                 engineCapacity;
        @XmlElement(name = "bikeType")            private String              bikeType;

        // truck only
        @XmlElement(name = "tires")               private int                 tires;
        @XmlElement(name = "payloadCapacityTons") private double              payloadCapacityTons;

        // boolean — the hasSleepingCabin field required by the assignment
        @XmlElement(name = "hasSleepingCabin")    private boolean             hasSleepingCabin;

        public String getBrand()               { return brand; }
        public String getModel()               { return model; }
        public int getYear()                   { return year; }
        public String getVin()                 { return vin; }
        public String getLicensePlate()        { return licensePlate; }
        public JaxbTransmissionXml getTransmission() { return transmission; }
        public int getDoors()                  { return doors; }
        public String getEngineType()          { return engineType; }
        public double getEngineSize()          { return engineSize; }
        public int getEngineCapacity()         { return engineCapacity; }
        public String getBikeType()            { return bikeType; }
        public int getTires()                  { return tires; }
        public double getPayloadCapacityTons() { return payloadCapacityTons; }
        public boolean isHasSleepingCabin()    { return hasSleepingCabin; }

        @Override
        public String toString() {
            return "Vehicle{brand='" + brand + "', model='" + model +
                    "', vin='" + vin + "', hasSleepingCabin=" + hasSleepingCabin + "}";
        }
    }

    // ── Appointment ───────────────────────────────────────────────────────────
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JaxbAppointmentXml {

        @XmlElement(name = "id")          private int id;

        // LocalDateTime needs an adapter — same reason as LocalDate
        @XmlElement(name = "scheduledTime")
        @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
        private LocalDateTime scheduledTime;

        @XmlElement(name = "status")      private String status;
        @XmlElement(name = "customerRef") private String customerRef;
        @XmlElement(name = "mechanicRef") private String mechanicRef;
        @XmlElement(name = "vehicleRef")  private String vehicleRef;

        public int getId()                     { return id; }
        public LocalDateTime getScheduledTime(){ return scheduledTime; }
        public String getStatus()              { return status; }
        public String getCustomerRef()         { return customerRef; }
        public String getMechanicRef()         { return mechanicRef; }
        public String getVehicleRef()          { return vehicleRef; }

        @Override
        public String toString() {
            return "Appointment{id=" + id + ", status='" + status +
                    "', scheduledTime=" + scheduledTime + "}";
        }
    }

    // ── SparePart ─────────────────────────────────────────────────────────────
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JaxbSparePartXml {

        @XmlElement(name = "productName")   private String     productName;
        @XmlElement(name = "productNumber") private String     productNumber;
        @XmlElement(name = "unitPrice")     private BigDecimal unitPrice;
        @XmlElement(name = "quantity")      private int        quantity;

        public String getProductName()    { return productName; }
        public String getProductNumber()  { return productNumber; }
        public BigDecimal getUnitPrice()  { return unitPrice; }
        public int getQuantity()          { return quantity; }
        public boolean isInStock()        { return quantity > 0; }

        @Override
        public String toString() {
            return "SparePart{name='" + productName + "', qty=" + quantity +
                    ", inStock=" + isInStock() + "}";
        }
    }
}
