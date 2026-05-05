package com.solvd.autoservicerepair.parsers.xmltojavaobject;

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

public class InsuranceXml {

    private String provider;
    private String policyNumber;
    private LocalDate expiryDate;
    private BigDecimal monthlyPremium;
    private String tier;           // maps to InsuranceTier enum

    /**
     * Derived — mirrors Insurance.isExpired() in your domain model.
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    @Override
    public String toString() {
        return "Insurance{provider='" + provider + "', tier='" + tier +
                "', expired=" + isExpired() + "}";
    }

}


