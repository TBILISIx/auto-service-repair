package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString

public class MechanicXml {

    private String name;
    private String idNumber;
    private String phone;
    private String specialization;
    private int yearsOfExperience;
    private String level;           // maps to MechanicSeniorityLevel enum
    private BigDecimal hourlyRate;

}