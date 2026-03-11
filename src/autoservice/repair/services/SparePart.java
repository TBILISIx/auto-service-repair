package autoservice.repair.services;

import java.math.BigDecimal;

public class SparePart {

    private final String partName;
    private final String partNumber;
    private BigDecimal unitPrice;
    private int quantity;

    public SparePart(String partName, String partNumber, BigDecimal unitPrice, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative for part: " + partName);
        }
        this.partName = partName;
        this.partNumber = partNumber;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public BigDecimal getTotalCost() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    public void useOne() {
        if (!isInStock()) {
            throw new IllegalStateException("Part out of stock: " + partName);
        }
        quantity--;
    }

    public String getPartName() {
        return partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
