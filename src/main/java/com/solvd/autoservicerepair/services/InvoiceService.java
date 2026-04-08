package com.solvd.autoservicerepair.services;

import com.solvd.autoservicerepair.model.Customer;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Calculates: service price → apply discount → apply payment processing fee → final total.
 **/

@Slf4j
public class InvoiceService extends Document {

    private final Customer customer;
    private final RepairOrder<?> repairOrder;
    private BigDecimal discountPercent;
    private Payment payment;

    public InvoiceService(Integer id, Customer customer, RepairOrder<?> repairOrder, BigDecimal discountPercent) {
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
        log.info("=== INVOICE ===");
        log.info("Issue Date           : " + getDate());

        log.info("Customer             : " + customer.getName());
        log.info("Registration Date    : " + customer.getRegistrationDate());
        log.info("Insurance Provider   : " + customer.getInsurance().provider());
        log.info("Insurance Tier       : " + customer.getInsurance().tier().getDisplayName());
        log.info("Coverage Percent     : " + customer.getInsurance().tier().getCoveragePercent() + "%");
        log.info("Roadside Assistance  : " + customer.getInsurance().tier().includesRoadsideAssistance());
        log.info("Policy Number        : " + customer.getInsurance().policyNumber());
        log.info("Policy Expired       : " + customer.getInsurance().isExpired());
        log.info("Policy Expiry Date   : " + customer.getInsurance().expiryDate());
        log.info("------------------------------------------------------------------------------");

        log.info("Vehicle              : " + repairOrder.getVehicleBrand() + " " + repairOrder.getVehicleModel());
        log.info("Service              : " + repairOrder.service().getServiceName());
        log.info("Service Description  : " + repairOrder.service().getServiceDescription());
        log.info("------------------------------------------------------------------------------");

        log.info("Base Price           : " + repairOrder.service().getPrice() + " GEL");
        log.info("Discount             : " + discountPercent + "%");

        if (payment != null && payment.getMethod().hasFee()) {
            log.info("Processing Fee       : " + payment.getMethod().getProcessingFeePercent() + "%");
        }

        log.info("Total Due            : " + payment.getAmount() + " GEL");

        if (payment != null) {
            log.info("Payment Method       : " + payment.getMethod().getDisplayName());
            log.info("Payment Confirmed    : " + payment.isConfirmed());
        }

        log.info("===============");
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