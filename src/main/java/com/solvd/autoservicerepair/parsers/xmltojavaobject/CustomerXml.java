package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mirrors <customer>
 */

@Getter
@Setter
@ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerXml {

    @JsonProperty("name")
    private String name;

    @JsonProperty("idNumber")
    private String idNumber;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("age")
    private int age;

    @JsonProperty("loyaltyPoints")
    private int loyaltyPoints;

    @JsonProperty("email")
    private String email;

    @JsonProperty("insurance")
    private InsuranceXml insurance;

}