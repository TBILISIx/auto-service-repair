package autoservice.repair.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Payment extends Document {

    private final BigDecimal amount;
    private final String method;   // CASH / CARD / TRANSFER
    private final LocalDateTime paymentDate;
    private boolean confirmed;

    public Payment(int id, BigDecimal amount, String method) {
        super(id);
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
