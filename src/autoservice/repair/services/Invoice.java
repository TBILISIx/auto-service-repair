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

    public Invoice(int id, Customer customer, RepairOrder repairOrder, BigDecimal discountPercent) {
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
        System.out.println("Issue Date   : " + getDate());
        System.out.println("Customer     : " + customer.getName());
        System.out.println("Insurance    : " + customer.getInsurance().getProvider()
                + " | Policy: " + customer.getInsurance().getPolicyNumber()
                + " | Expired: " + customer.getInsurance().isExpired());
        System.out.println("Registration Date: " + customer.getRegistrationDate());
        System.out.println("Expiry Date: " + customer.getInsurance().getExpiryDate());
        System.out.println("Vehicle      : " + repairOrder.getVehicleBrand()
                + " " + repairOrder.getVehicleModel());
        System.out.println("Service      : " + repairOrder.getService().getServiceName());
        System.out.println("*****");
        System.out.println("Service Description: " + repairOrder.getService().getServiceDescription());
        System.out.println("*****");
        System.out.println("Base Price   : " + repairOrder.getService().getPrice() + " GEL");
        System.out.println("Discount     : " + discountPercent + "%");
        System.out.println("Total Due    : " + calculateTotal() + " GEL");
        if (payment != null) {
            System.out.println("Payment      : " + payment.getMethod()
                    + " | " + payment.getAmount() + " GEL"
                    + " | Confirmed: " + payment.isConfirmed());
        }
        System.out.println("===============");
    }

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
