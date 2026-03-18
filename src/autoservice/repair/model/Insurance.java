package autoservice.repair.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Insurance {

    private final String provider;
    private final String policyNumber;
    private final LocalDate expiryDate;
    private BigDecimal monthlyPremium;

    public Insurance(String provider, String policyNumber, LocalDate expiryDate, BigDecimal monthlyPremium) {
        this.provider = provider;
        this.policyNumber = policyNumber;
        this.expiryDate = expiryDate;
        this.monthlyPremium = monthlyPremium;
    }

    public Boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public String getProvider() {
        return provider;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public BigDecimal getMonthlyPremium() {
        return monthlyPremium;
    }

}
