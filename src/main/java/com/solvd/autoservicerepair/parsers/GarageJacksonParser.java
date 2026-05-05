package com.solvd.autoservicerepair.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.solvd.autoservicerepair.interfaces.Parser;
import com.solvd.autoservicerepair.parsers.xmltojavaobject.GarageXml;

import java.io.File;
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
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.readValue(new File(filePath), GarageXml.class);
    }
}