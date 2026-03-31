package autoservice.repair.services;

import autoservice.repair.model.Customer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Calculates: service price → apply discount → apply payment processing fee → final total.
 **/

public class Invoice extends Document {

    private final Customer customer;
    private final RepairOrder<?> repairOrder;
    private BigDecimal discountPercent;
    private Payment payment;

    public Invoice(Integer id, Customer customer, RepairOrder<?> repairOrder, BigDecimal discountPercent) {
        super(id);
        this.customer = customer;
        this.repairOrder = repairOrder;
        this.discountPercent = discountPercent;
        this.payment = null;
    }

    public BigDecimal calculateTotal() {
        BigDecimal basePrice = repairOrder.service().getPrice();

        //  apply discount
        BigDecimal discountAmount = basePrice
                .multiply(discountPercent)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal afterDiscount = basePrice.subtract(discountAmount);

        //  apply processing fee (0% for CASH, 1.5% for CARD, 0.5% for TRANSFER)
        BigDecimal feePercent = BigDecimal.valueOf(payment.getMethod().getProcessingFeePercent());
        BigDecimal feeAmount = afterDiscount
                .multiply(feePercent)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return afterDiscount.add(feeAmount);
    }

    public void addPayment(Payment payment) {
        this.payment = payment;
        BigDecimal finalTotal = calculateTotal();
        payment.setAmount(finalTotal);  // Invoice tells Payment what the amount is
        payment.confirm();
    }

    public void generate() {
        System.out.println("=== INVOICE ===");
        System.out.println("Issue Date           : " + getDate());

        System.out.println("Customer             : " + customer.getName());
        System.out.println("Registration Date    : " + customer.getRegistrationDate());
        System.out.println("Insurance Provider   : " + customer.getInsurance().provider());
        System.out.println("Insurance Tier       : " + customer.getInsurance().tier().getDisplayName());
        System.out.println("Coverage Percent     : " + customer.getInsurance().tier().getCoveragePercent() + "%");
        System.out.println("Roadside Assistance  : " + customer.getInsurance().tier().includesRoadsideAssistance());
        System.out.println("Policy Number        : " + customer.getInsurance().policyNumber());
        System.out.println("Policy Expired       : " + customer.getInsurance().isExpired());
        System.out.println("Policy Expiry Date   : " + customer.getInsurance().expiryDate());
        System.out.println("------------------------------------------------------------------------------");

        System.out.println("Vehicle              : " + repairOrder.getVehicleBrand() + " " + repairOrder.getVehicleModel());
        System.out.println("Service              : " + repairOrder.service().getServiceName());
        System.out.println("Service Description  : " + repairOrder.service().getServiceDescription());
        System.out.println("------------------------------------------------------------------------------");

        System.out.println("Base Price           : " + repairOrder.service().getPrice() + " GEL");
        System.out.println("Discount             : " + discountPercent + "%");

        if (payment != null && payment.getMethod().hasFee()) {
            System.out.println("Processing Fee       : " + payment.getMethod().getProcessingFeePercent() + "%");
        }

        System.out.println("Total Due            : " + payment.getAmount() + " GEL");

        if (payment != null) {
            System.out.println("Payment Method       : " + payment.getMethod().getDisplayName());
            System.out.println("Payment Confirmed    : " + payment.isConfirmed());
        }

        System.out.println("===============");
    }

    // Getters and Setters
    public Customer getCustomer() {
        return customer;
    }

    public RepairOrder<?> getRepairOrder() {
        return repairOrder;
    }

    public LocalDate getIssueDate() {
        return getDate();
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Payment getPayment() {
        return payment;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + getId() +
                ", issueDate=" + getDate() +
                ", customer=" + customer +
                ", repairOrder=" + repairOrder +
                ", discountPercent=" + discountPercent + "%" +
                ", payment=" + payment +
                '}';
    }

}