package autoservice.repair.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Insurance(String provider, String policyNumber, LocalDate expiryDate, BigDecimal monthlyPremium) {

    public Boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

}
