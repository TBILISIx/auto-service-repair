package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mirrors <insurance> — nested inside <customer>
 */

@Setter
@Getter
@ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class InsuranceXml {

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("policyNumber")
    private String policyNumber;

    @JsonProperty("expiryDate")
    private LocalDate expiryDate;

    @JsonProperty("monthlyPremium")
    private BigDecimal monthlyPremium;

    @JsonProperty("tier")
    private String tier;
}


