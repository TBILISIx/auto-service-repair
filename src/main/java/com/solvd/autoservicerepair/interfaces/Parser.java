package com.solvd.autoservicerepair.interfaces;

import com.solvd.autoservicerepair.parsers.xmltojavaobject.GarageXml;

/**
 * Common interface for all parsers.
 *
 * Three implementations:
 *   GarageStaxParser    — reads XML using StAX (manual, pull-based)
 *   GarageJaxbParser    — reads XML using JAXB (annotation-based, automatic)
 *   GarageJacksonParser — reads JSON using Jackson (annotation-based, automatic)
 *
 */
public interface Parser {

    GarageXml parse(String filePath) throws Exception;

}