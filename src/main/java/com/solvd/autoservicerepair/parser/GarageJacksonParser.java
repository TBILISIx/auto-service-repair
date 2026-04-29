package com.solvd.autoservicerepair.parser;

import com.solvd.autoservicerepair.interfaces.Parser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Jackson parser for garage.json.
 *
 * HOW JACKSON WORKS:
 *   Jackson is a JSON serialization/deserialization library.
 *   Like JAXB for XML, Jackson reads annotations on your classes and
 *   maps JSON fields to Java fields automatically.
 *
 *   "Deserialization" = reading JSON and producing Java objects (what we do).
 *   "Serialization"   = taking Java objects and writing JSON (the opposite).
 *
 *   The central class is ObjectMapper (or JsonMapper in Jackson 3.x).
 *   One line does all the work:
 *     mapper.readValue(file, GarageJson.class)
 *
 * JACKSON 3.x vs 2.x:
 *   Your pom.xml uses groupId "tools.jackson" — that is Jackson 3.x.
 *   Jackson 3.x moved packages from "com.fasterxml.jackson" to "tools.jackson".
 *   The concepts are identical, only the import paths changed.
 *   Jackson 2.x: com.fasterxml.jackson.databind.ObjectMapper
 *   Jackson 3.x: tools.jackson.databind.ObjectMapper
 *
 * JACKSON ANNOTATIONS EXPLAINED:
 *
 *   @JsonProperty("name")
 *     Maps a JSON key to a Java field.
 *     When Jackson reads "name": "AutoFix Tbilisi" in JSON,
 *     it sets the 'name' field on the Java object.
 *     If the Java field name matches the JSON key, this is optional.
 *     We write it explicitly for clarity.
 *
 *   @JsonIgnoreProperties(ignoreUnknown = true)
 *     If the JSON has fields the Java class does not have, ignore them
 *     instead of throwing an error. Makes the parser more robust.
 *
 *   @JsonDeserialize(using = LocalDateDeserializer.class)
 *     Jackson 3.x handles LocalDate/LocalDateTime via the JavaTimeModule,
 *     but registering it requires a custom deserializer or module setup.
 *     We use custom deserializers here for maximum clarity.
 */
public class GarageJacksonParser implements Parser<GarageJacksonParser.GarageJson> {

    @Override
    public GarageJson parse(String filePath) throws Exception {

        /*
         * JsonMapper is the Jackson 3.x replacement for ObjectMapper.
         * builder() creates a builder that lets us configure the mapper
         * before building it — cleaner than calling methods after construction.
         *
         * addModule(new JavaTimeModule()) registers Java 8 date/time support.
         * Without this, Jackson cannot deserialize LocalDate and LocalDateTime.
         */

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // One line — read the file, map to GarageJson, return it.
        return mapper.readValue(new File(filePath), GarageJson.class);
    }

    // =========================================================================
    // CUSTOM DESERIALIZERS for LocalDate and LocalDateTime
    // Jackson's JavaTimeModule handles these, but we write explicit ones
    // to make it crystal clear what is happening.
    // =========================================================================

    public static class LocalDateDeserializer extends StdDeserializer<LocalDate> {
        public LocalDateDeserializer() { super(LocalDate.class); }

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            // JSON value: "2026-12-31" → LocalDate.parse handles ISO format natively
            return LocalDate.parse(p.getText());
        }
    }

    public static class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
        public LocalDateTimeDeserializer() { super(LocalDateTime.class); }

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            // JSON value: "2026-04-23T10:00:00" → LocalDateTime natively
            return LocalDateTime.parse(p.getText());
        }
    }

    // =========================================================================
    // JSON DATA CLASSES
    // Same concept as the Xml classes — simple data holders.
    // Fields match the JSON keys. Annotations tell Jackson the mapping.
    // =========================================================================

    // ── Root ──────────────────────────────────────────────────────────────────
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GarageJson {

        @JsonProperty("name")          private String              name;
        @JsonProperty("address")       private String              address;
        @JsonProperty("totalBays")     private int                 totalBays;
        @JsonProperty("occupiedBays")  private int                 occupiedBays;
        @JsonProperty("mechanics")     private List<MechanicJson>  mechanics;
        @JsonProperty("customers")     private List<CustomerJson>  customers;
        @JsonProperty("vehicles")      private List<VehicleJson>   vehicles;
        @JsonProperty("appointments")  private List<AppointmentJson> appointments;
        @JsonProperty("spareParts")    private List<SparePartJson> spareParts;

        public String getName()                            { return name; }
        public String getAddress()                         { return address; }
        public int getTotalBays()                          { return totalBays; }
        public int getOccupiedBays()                       { return occupiedBays; }
        public List<MechanicJson> getMechanics()           { return mechanics; }
        public List<CustomerJson> getCustomers()           { return customers; }
        public List<VehicleJson> getVehicles()             { return vehicles; }
        public List<AppointmentJson> getAppointments()     { return appointments; }
        public List<SparePartJson> getSpareParts()         { return spareParts; }

        @Override
        public String toString() {
            return "GarageJson{name='" + name + "', address='" + address +
                    "', totalBays=" + totalBays + ", occupiedBays=" + occupiedBays +
                    ", mechanics=" + (mechanics != null ? mechanics.size() : 0) +
                    ", customers=" + (customers != null ? customers.size() : 0) +
                    ", vehicles=" + (vehicles != null ? vehicles.size() : 0) +
                    ", appointments=" + (appointments != null ? appointments.size() : 0) +
                    ", spareParts=" + (spareParts != null ? spareParts.size() : 0) + "}";
        }
    }

    // ── Mechanic ──────────────────────────────────────────────────────────────
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MechanicJson {

        @JsonProperty("name")               private String     name;
        @JsonProperty("idNumber")           private String     idNumber;
        @JsonProperty("phone")              private String     phone;
        @JsonProperty("specialization")     private String     specialization;
        @JsonProperty("yearsOfExperience")  private int        yearsOfExperience;
        @JsonProperty("level")              private String     level;
        @JsonProperty("hourlyRate")         private BigDecimal hourlyRate;

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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InsuranceJson {

        @JsonProperty("provider")       private String     provider;
        @JsonProperty("policyNumber")   private String     policyNumber;

        /*
         * @JsonDeserialize tells Jackson to use our custom deserializer
         * for this specific field instead of the default behavior.
         * JavaTimeModule handles this automatically once registered,
         * but the annotation makes the intent explicit.
         */
        @JsonProperty("expiryDate")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        private LocalDate expiryDate;

        @JsonProperty("monthlyPremium") private BigDecimal monthlyPremium;
        @JsonProperty("tier")           private String     tier;

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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerJson {

        @JsonProperty("name")           private String       name;
        @JsonProperty("idNumber")       private String       idNumber;
        @JsonProperty("phone")          private String       phone;
        @JsonProperty("age")            private int          age;
        @JsonProperty("loyaltyPoints")  private int          loyaltyPoints;
        @JsonProperty("email")          private String       email;
        @JsonProperty("insurance")      private InsuranceJson insurance;

        public String getName()               { return name; }
        public String getIdNumber()           { return idNumber; }
        public String getPhone()              { return phone; }
        public int getAge()                   { return age; }
        public int getLoyaltyPoints()         { return loyaltyPoints; }
        public String getEmail()              { return email; }
        public InsuranceJson getInsurance()   { return insurance; }

        @Override
        public String toString() {
            return "Customer{name='" + name + "', email='" + email +
                    "', loyaltyPoints=" + loyaltyPoints + "}";
        }
    }

    // ── Transmission ──────────────────────────────────────────────────────────
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TransmissionJson {

        @JsonProperty("type")   private String type;
        @JsonProperty("gears")  private int    gears;

        public String getType()  { return type; }
        public int getGears()    { return gears; }

        @Override
        public String toString() {
            return "Transmission{type='" + type + "', gears=" + gears + "}";
        }
    }

    // ── Vehicle — flat class matching your VehicleXml exactly ─────────────────
    /*
     * JSON has "type": "car" / "motorcycle" / "truck" as a plain field.
     * Jackson reads it into the 'type' String field directly.
     * No polymorphism needed — same flat-class approach as your VehicleXml.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VehicleJson {

        // shared
        @JsonProperty("type")               private String          type;
        @JsonProperty("brand")              private String          brand;
        @JsonProperty("model")              private String          model;
        @JsonProperty("year")               private int             year;
        @JsonProperty("vin")                private String          vin;
        @JsonProperty("licensePlate")       private String          licensePlate;
        @JsonProperty("transmission")       private TransmissionJson transmission;

        // car + truck
        @JsonProperty("doors")              private int             doors;
        @JsonProperty("engineType")         private String          engineType;
        @JsonProperty("engineSize")         private double          engineSize;

        // motorcycle only
        @JsonProperty("engineCapacity")     private int             engineCapacity;
        @JsonProperty("bikeType")           private String          bikeType;

        // truck only
        @JsonProperty("tires")              private int             tires;
        @JsonProperty("payloadCapacityTons") private double         payloadCapacityTons;
        @JsonProperty("hasSleepingCabin")   private boolean         hasSleepingCabin;

        public String getType()                { return type; }
        public String getBrand()               { return brand; }
        public String getModel()               { return model; }
        public int getYear()                   { return year; }
        public String getVin()                 { return vin; }
        public String getLicensePlate()        { return licensePlate; }
        public TransmissionJson getTransmission() { return transmission; }
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
            return "Vehicle{type='" + type + "', brand='" + brand +
                    "', model='" + model + "', vin='" + vin +
                    "', hasSleepingCabin=" + hasSleepingCabin + "}";
        }
    }

    // ── Appointment ───────────────────────────────────────────────────────────
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AppointmentJson {

        @JsonProperty("id")           private int id;

        @JsonProperty("scheduledTime")
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime scheduledTime;

        @JsonProperty("status")       private String status;
        @JsonProperty("customerRef")  private String customerRef;
        @JsonProperty("mechanicRef")  private String mechanicRef;
        @JsonProperty("vehicleRef")   private String vehicleRef;

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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SparePartJson {

        @JsonProperty("productName")   private String     productName;
        @JsonProperty("productNumber") private String     productNumber;
        @JsonProperty("unitPrice")     private BigDecimal unitPrice;
        @JsonProperty("quantity")      private int        quantity;

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