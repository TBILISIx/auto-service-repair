package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class MechanicXml {

    @JsonProperty("name")
    private String name;

    @JsonProperty("idNumber")
    private String idNumber;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("specialization")
    private String specialization;

    @JsonProperty("yearsOfExperience")
    private int yearsOfExperience;

    @JsonProperty("level")
    private String level;

    @JsonProperty("hourlyRate")
    private BigDecimal hourlyRate;

}