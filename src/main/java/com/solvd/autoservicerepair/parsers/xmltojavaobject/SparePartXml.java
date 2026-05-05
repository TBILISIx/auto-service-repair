package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Mirrors <sparePart>
 */

@Getter
@Setter
@ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class SparePartXml {

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("productNumber")
    private String productNumber;

    @JsonProperty("unitPrice")
    private BigDecimal unitPrice;

    @JsonProperty("quantity")
    private int quantity;

    // all existing getters/setters/isInStock() stay exactly the same
}