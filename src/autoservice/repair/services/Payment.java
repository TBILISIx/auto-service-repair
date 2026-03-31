package autoservice.repair.services;

import autoservice.repair.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Holds the payment method and confirmation state.
 * The final amount is set by Invoice after it finishes calculating
 * discount + processing fee — so there is one single source of truth for pricing.
 */
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

        System.out.println("=== PAYMENT CONFIRMED ===");
        System.out.println("Method       : " + method.getDisplayName());
        System.out.println("Total Paid   : " + amount + " GEL");
        if (method.hasFee()) {
            System.out.println("Includes     : " + method.getProcessingFeePercent() + "% processing fee");
        }
        System.out.println("==========================");
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