package com.solvd.autoservicerepair.parsers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.solvd.autoservicerepair.interfaces.Parser;
import com.solvd.autoservicerepair.parsers.xmltojavaobject.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Jackson parser for garage.json.
 *
 * HOW JACKSON WORKS:
 *   Jackson is a JSON serialization/deserialization library.
 *   Like JAXB for XML, Jackson reads annotations on your classes and
 *   maps JSON fields to Java fields automatically.
 *
 *   "Deserialization" = reading JSON and producing Java objects (what we do here).
 *   "Serialization" = taking Java objects and writing JSON (the opposite).
 *
 *   The central class is ObjectMapper. It does everything in one line:
 *     mapper.readValue(file, GarageJson.class)
 *   That single call reads the entire JSON file and returns a populated object.
 *   Compare this to StAX where you write a full while loop with every case manually.
 *
 * THREE STEPS TO PARSE WITH JACKSON:
 *
 *   1. new ObjectMapper()
 *      Creates the mapper with default settings.
 *
 *   2. mapper.registerModule(new JavaTimeModule())
 *      Jackson (com.fasterxml.jackson version 2.x) does not support
 *      Java 8 date types (LocalDate, LocalDateTime) out of the box.
 *      JavaTimeModule adds that support. Without it, Jackson throws
 *      an error when it encounters those fields.
 *
 *   3. mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
 *      By default Jackson writes/reads dates as numbers (timestamps).
 *      Disabling this tells Jackson to use ISO strings like
 *      "2026-04-23T10:00:00" instead — which matches our JSON file.
 *
 * JACKSON ANNOTATIONS EXPLAINED:
 *
 *   @JsonProperty("name")
 *     Maps a JSON key to a Java field.
 *     When Jackson reads "name": "AutoFix Tbilisi" in the JSON file,
 *     it sets the 'name' field on the Java object to "AutoFix Tbilisi".
 *     If the Java field name already matches the JSON key exactly,
 *     this annotation is optional — but we write it everywhere explicitly
 *     so the mapping is obvious when reading the code.
 *
 *   @JsonIgnoreProperties(ignoreUnknown = true)
 *     If the JSON file contains a field that the Java class does not have,
 *     Jackson ignores it instead of throwing an error.
 *     Makes the parser robust against extra or unexpected fields in the JSON.
 *
 *   @JsonDeserialize(using = LocalDateDeserializer.class)
 *     Points Jackson to a custom deserializer for a specific field.
 *     Jackson does not know how to convert "2026-12-31" into a LocalDate
 *     on its own. The deserializer class does that conversion:
 *     it receives the raw String from the JSON and returns a LocalDate.
 *     JavaTimeModule does this automatically once registered on the mapper,
 *     but we also write explicit deserializers to make the logic visible.
 */

public class GarageJacksonParser implements Parser {

    @Override
    public GarageXml parse(String filePath) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // One line — read the file, map to GarageJson, then convert to GarageXml.
        GarageJson json = mapper.readValue(new File(filePath), GarageJson.class);
        return toGarageXml(json);
    }

    // =========================================================================
    // Mapping — converts Jackson inner objects into the shared xmltojavaobject types.
    // This keeps the Jackson annotations isolated inside this class while still
    // returning the same GarageXml that StAX and JAXB return.
    // =========================================================================

    private GarageXml toGarageXml(GarageJson j) {
        GarageXml g = new GarageXml();
        g.setName(j.name);
        g.setAddress(j.address);
        g.setTotalBays(j.totalBays);
        g.setOccupiedBays(j.occupiedBays);

        // mechanics
        List<MechanicXml> mechanics = new ArrayList<>();
        if (j.mechanics != null) {
            for (MechanicJson m : j.mechanics) {
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
            for (CustomerJson c : j.customers) {
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

        // vehicles — "type" field comes directly from the JSON string
        List<VehicleXml> vehicles = new ArrayList<>();
        if (j.vehicles != null) {
            for (VehicleJson v : j.vehicles) {
                VehicleXml vx = new VehicleXml();
                vx.setType(v.type);
                vx.setBrand(v.brand);
                vx.setModel(v.model);
                vx.setYear(v.year);
                vx.setVin(v.vin);
                vx.setLicensePlate(v.licensePlate);
                vx.setDoors(v.doors);
                vx.setEngineType(v.engineType);
                vx.setEngineSize(v.engineSize);
                vx.setEngineCapacity(v.engineCapacity);
                vx.setBikeType(v.bikeType);
                vx.setTires(v.tires);
                vx.setPayloadCapacityTons(v.payloadCapacityTons);
                vx.setHasSleepingCabin(v.hasSleepingCabin);
                if (v.transmission != null) {
                    TransmissionXml tx = new TransmissionXml();
                    tx.setType(v.transmission.type);
                    tx.setGears(v.transmission.gears);
                    vx.setTransmission(tx);
                }
                vehicles.add(vx);
            }
        }
        g.setVehicles(vehicles);

        // appointments
        List<AppointmentXml> appointments = new ArrayList<>();
        if (j.appointments != null) {
            for (AppointmentJson a : j.appointments) {
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
            for (SparePartJson s : j.spareParts) {
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
    // CUSTOM DESERIALIZERS for LocalDate and LocalDateTime
    // Jackson's JavaTimeModule handles these, but we write explicit ones
    // to make it crystal clear what is happening.
    // =========================================================================

    private static class LocalDateDeserializer extends StdDeserializer<LocalDate> {
        public LocalDateDeserializer() { super(LocalDate.class); }

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            // JSON value: "2026-12-31" → LocalDate.parse handles ISO format natively
            return LocalDate.parse(p.getText());
        }
    }

    private static class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GarageJson {
        @JsonProperty("name")          String              name;
        @JsonProperty("address")       String              address;
        @JsonProperty("totalBays")     int                 totalBays;
        @JsonProperty("occupiedBays")  int                 occupiedBays;
        @JsonProperty("mechanics")     List<MechanicJson>  mechanics;
        @JsonProperty("customers")     List<CustomerJson>  customers;
        @JsonProperty("vehicles")      List<VehicleJson>   vehicles;
        @JsonProperty("appointments")  List<AppointmentJson> appointments;
        @JsonProperty("spareParts")    List<SparePartJson> spareParts;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MechanicJson {
        @JsonProperty("name")               String     name;
        @JsonProperty("idNumber")           String     idNumber;
        @JsonProperty("phone")              String     phone;
        @JsonProperty("specialization")     String     specialization;
        @JsonProperty("yearsOfExperience")  int        yearsOfExperience;
        @JsonProperty("level")              String     level;
        @JsonProperty("hourlyRate")         BigDecimal hourlyRate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class InsuranceJson {
        @JsonProperty("provider")       String     provider;
        @JsonProperty("policyNumber")   String     policyNumber;
        @JsonProperty("expiryDate")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate  expiryDate;
        @JsonProperty("monthlyPremium") BigDecimal monthlyPremium;
        @JsonProperty("tier")           String     tier;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CustomerJson {
        @JsonProperty("name")           String       name;
        @JsonProperty("idNumber")       String       idNumber;
        @JsonProperty("phone")          String       phone;
        @JsonProperty("age")            int          age;
        @JsonProperty("loyaltyPoints")  int          loyaltyPoints;
        @JsonProperty("email")          String       email;
        @JsonProperty("insurance")      InsuranceJson insurance;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TransmissionJson {
        @JsonProperty("type")  String type;
        @JsonProperty("gears") int    gears;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class VehicleJson {
        @JsonProperty("type")               String          type;
        @JsonProperty("brand")              String          brand;
        @JsonProperty("model")              String          model;
        @JsonProperty("year")               int             year;
        @JsonProperty("vin")                String          vin;
        @JsonProperty("licensePlate")       String          licensePlate;
        @JsonProperty("transmission")       TransmissionJson transmission;
        @JsonProperty("doors")              int             doors;
        @JsonProperty("engineType")         String          engineType;
        @JsonProperty("engineSize")         double          engineSize;
        @JsonProperty("engineCapacity")     int             engineCapacity;
        @JsonProperty("bikeType")           String          bikeType;
        @JsonProperty("tires")              int             tires;
        @JsonProperty("payloadCapacityTons") double         payloadCapacityTons;
        @JsonProperty("hasSleepingCabin")   boolean         hasSleepingCabin;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class AppointmentJson {
        @JsonProperty("id")           int           id;
        @JsonProperty("scheduledTime")
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime scheduledTime;
        @JsonProperty("status")       String        status;
        @JsonProperty("customerRef")  String        customerRef;
        @JsonProperty("mechanicRef")  String        mechanicRef;
        @JsonProperty("vehicleRef")   String        vehicleRef;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class SparePartJson {
        @JsonProperty("productName")   String     productName;
        @JsonProperty("productNumber") String     productNumber;
        @JsonProperty("unitPrice")     BigDecimal unitPrice;
        @JsonProperty("quantity")      int        quantity;
    }

}