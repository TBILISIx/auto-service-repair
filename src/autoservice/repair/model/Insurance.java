package autoservice.repair.model;

import autoservice.repair.enums.InsuranceTier;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Insurance(String provider, String policyNumber,
                        LocalDate expiryDate, BigDecimal monthlyPremium,
                        InsuranceTier tier) {

    public Boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

}
