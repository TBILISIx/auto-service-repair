package autoservice.repair.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private final BigDecimal amount;
    private final String method;   // CASH / CARD / TRANSFER
    private final LocalDateTime paymentDate;
    private boolean confirmed;

    public Payment(BigDecimal amount, String method) {
        this.amount = amount;
        this.method = method;
        this.paymentDate = LocalDateTime.now();
        this.confirmed = false;
    }

    public void confirm() {
        this.confirmed = true;
        System.out.println("Payment of " + amount + " GEL via " + method + " confirmed.");
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

}
