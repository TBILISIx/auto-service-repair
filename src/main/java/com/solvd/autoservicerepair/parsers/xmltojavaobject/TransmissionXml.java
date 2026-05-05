package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mirrors <transmission> — nested inside each vehicle
 */

@Getter
@Setter
@ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransmissionXml {

    @JsonProperty("type")
    private String type;

    @JsonProperty("gears")
    private int gears;

}