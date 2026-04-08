package com.solvd.autoservicerepair.services;

import com.solvd.autoservicerepair.enums.PaymentMethod;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Holds the payment method and confirmation state.
 * The final amount is set by Invoice after it finishes calculating
 * discount + processing fee — so there is one single source of truth for pricing.
 */

@Slf4j
public class Payment extends Document {

    private BigDecimal amount;
    private final PaymentMethod method;
    private final LocalDateTime paymentDate;
    private boolean confirmed;

    public Payment(Integer id, PaymentMethod method) {
        super(id);
        this.method = method;
        this.paymentDate = LocalDateTime.now();
        this.confirmed = false;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void confirm() {
        this.confirmed = true;

        log.info("=== PAYMENT CONFIRMED ===");
        log.info("Method       : " + method.getDisplayName());
        log.info("Total Paid   : " + amount + " GEL");
        if (method.hasFee()) {
            log.info("Includes     : " + method.getProcessingFeePercent() + "% processing fee");
        }
        log.info("==========================");
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + getId() +
                ", amount=" + amount + " GEL" +
                ", method='" + method.getDisplayName() + '\'' +
                ", paymentDate=" + paymentDate +
                ", confirmed=" + confirmed +
                '}';
    }

}