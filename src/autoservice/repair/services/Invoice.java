package autoservice.repair.services;

import autoservice.repair.model.Customer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Invoice extends Document {

    private final Customer customer;
    private final RepairOrder repairOrder;
    private BigDecimal discountPercent;
    private Payment payment;

    public Invoice(Integer id, Customer customer, RepairOrder repairOrder, BigDecimal discountPercent) {
        super(id);
        this.customer = customer;
        this.repairOrder = repairOrder;
        this.discountPercent = discountPercent;
        this.payment = null;
    }

    public BigDecimal calculateTotal() {
        BigDecimal basePrice = repairOrder.getService().getPrice();
        BigDecimal discountAmount = basePrice
                .multiply(discountPercent)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return basePrice.subtract(discountAmount);
    }

    public void addPayment(Payment payment) {
        this.payment = payment;
        payment.confirm();
    }

    public void generate() {
        System.out.println("=== INVOICE ===");

        // Invoice Info
        System.out.println("Issue Date           : " + getDate());

        // Customer Info
        System.out.println("Customer             : " + customer.getName());
        System.out.println("Registration Date    : " + customer.getRegistrationDate());
        System.out.println("Insurance Provider   : " + customer.getInsurance().getProvider());
        System.out.println("Policy Number        : " + customer.getInsurance().getPolicyNumber());
        System.out.println("Policy Expired       : " + customer.getInsurance().isExpired());
        System.out.println("Policy Expiry Date   : " + customer.getInsurance().getExpiryDate());
        System.out.println("------------------------------------------------------------------------------");

        // Vehicle Info
        System.out.println("Vehicle              : " + repairOrder.getVehicleBrand() + " " + repairOrder.getVehicleModel());
        System.out.println("Service              : " + repairOrder.getService().getServiceName());
        System.out.println("Service Description  : " + repairOrder.getService().getServiceDescription());
        System.out.println("------------------------------------------------------------------------------");

        // Pricing Info
        System.out.println("Base Price           : " + repairOrder.getService().getPrice() + " GEL");
        System.out.println("Discount             : " + discountPercent + "%");
        System.out.println("Total Due            : " + calculateTotal() + " GEL");

        // Payment Info
        if (payment != null) {
            System.out.println("Payment Method       : " + payment.getMethod());
            System.out.println("Payment Amount       : " + payment.getAmount() + " GEL");
            System.out.println("Payment Confirmed    : " + payment.isConfirmed());
        }

        System.out.println("===============");
    }

    // Getters and Setters
    public Customer getCustomer() {
        return customer;
    }

    public RepairOrder getRepairOrder() {
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
