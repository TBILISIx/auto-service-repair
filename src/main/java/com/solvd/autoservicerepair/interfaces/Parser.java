package com.solvd.autoservicerepair.interfaces;

/**
 * Common interface for all parsers.
 *
 * T is the return type — in all three implementations it will be GarageXml.
 * Each implementation reads a file from the given path and returns
 * a fully populated GarageXml object.
 *
 * Three implementations:
 *   GarageStaxParser   — reads XML using StAX (manual, pull-based)
 *   GarageJaxbParser   — reads XML using JAXB (annotation-based, automatic)
 *   GarageJacksonParser — reads JSON using Jackson (annotation-based, automatic)
 */

public interface Parser<T> {
    T parse(String filePath) throws Exception;
}