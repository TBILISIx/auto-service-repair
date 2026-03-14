package autoservice.repair.model;

import java.math.BigDecimal;

public class SparePart extends Product {

    private int quantity;

    public SparePart(String partName, String partNumber, BigDecimal unitPrice, int quantity) {
        super(partName, partNumber, unitPrice);
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative for part: " + partName);
        }

        this.quantity = quantity;
    }

    public BigDecimal getTotalCost() {
        return getUnitPrice().multiply(new BigDecimal(quantity));
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    public void useOne() {
        if (!isInStock()) {
            throw new IllegalStateException("Part out of stock: " + getProductName());
        }
        quantity--;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
