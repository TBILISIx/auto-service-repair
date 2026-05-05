package com.solvd.autoservicerepair.parsers;

import com.solvd.autoservicerepair.interfaces.Parser;
import com.solvd.autoservicerepair.parsers.xmltojavaobject.GarageXml;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserDemo {

    private static final String XML_PATH  = "src/main/resources/garage.xml";
    private static final String JSON_PATH = "src/main/resources/garage.json";

    public static void main(String[] args) throws Exception {

        GarageXml garage;
        Parser parser;

        // ---- StAX ----
        parser = new GarageStaxParser();
        garage = parser.parse(XML_PATH);

        log.info("StAX    -> {}", garage); // This logging happens because of @ToString from lombok or manually overriding ToString method

        // ---- JAXB ----
        parser = new GarageJaxbParser();
        garage = parser.parse(XML_PATH);
        log.info("JAXB    -> {}", garage);

        // ---- Jackson ----
        parser = new GarageJacksonParser();
        garage = parser.parse(JSON_PATH);
        log.info("Jackson -> {}", garage);

        // ---- GarageXpathQueries ----
        parser = new GarageXPathQueries(XML_PATH);
        garage = parser.parse(XML_PATH);
        log.info("GarageXPathQueries -> {}", garage);

    }
}