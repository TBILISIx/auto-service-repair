package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mirrors <transmission> — nested inside each vehicle
 */

@Getter
@Setter
@ToString

public class TransmissionXml {

    private String type;   // maps to TransmissionType enum
    private int gears;

}