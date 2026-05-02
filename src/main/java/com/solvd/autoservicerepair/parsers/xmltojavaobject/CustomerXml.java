package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mirrors <customer>
 */

@Getter
@Setter
@ToString

public class CustomerXml {

    private String name;
    private String idNumber;
    private String phone;
    private int age;
    private int loyaltyPoints;
    private String email;
    private InsuranceXml insurance;

}