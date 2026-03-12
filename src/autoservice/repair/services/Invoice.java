package autoservice.repair.services;

import autoservice.repair.model.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {

    private final Customer customer;
    private final RepairOrder repairOrder;
    private final LocalDate issueDate;
    private Payment payment;

    public Invoice(Customer customer, RepairOrder repairOrder) {
        this.customer = customer;
        this.repairOrder = repairOrder;
        this.issueDate = LocalDate.now();
        this.payment = null;
    }

    public BigDecimal calculateTotal() {
        BigDecimal basePrice = repairOrder.getService().getPrice();
        return basePrice.subtract(basePrice);
    }

    public void addPayment(Payment payment) {
        this.payment = payment;
        payment.confirm();
    }

    public void generate() {
        System.out.println("=== INVOICE ===");
        System.out.println("Issue Date   : " + issueDate);
        System.out.println("Customer     : " + customer.getName());
        System.out.println("Insurance    : " + customer.getInsurance().getProvider()
                + " | Policy: " + customer.getInsurance().getPolicyNumber()
                + " | Expired: " + customer.getInsurance().isExpired());
        System.out.println("Registration Date: " + customer.getRegistrationDate());
        System.out.println("Expiry Date: " + customer.getInsurance().getExpiryDate());
        System.out.println("Vehicle      : " + repairOrder.getVehicleBrand()
                + " " + repairOrder.getVehicleModel());
        System.out.println("Service      : " + repairOrder.getService().getServiceName());
        System.out.println("Base Price   : " + repairOrder.getService().getPrice() + " GEL");
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
        return issueDate;
    }

    public Payment getPayment() {
        return payment;
    }

}
