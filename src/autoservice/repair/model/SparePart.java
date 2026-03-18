package autoservice.repair.model;

import java.math.BigDecimal;

public class SparePart extends Product {

    private Integer quantity;

    public SparePart(String partName, String partNumber, BigDecimal unitPrice, Integer quantity) {
        super(partName, partNumber, unitPrice);
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative for part: " + partName);
        }

        this.quantity = quantity;
    }

    public BigDecimal getTotalCost() {
        return getUnitPrice().multiply(new BigDecimal(quantity));
    }

    public Boolean isInStock() {
        return quantity > 0;
    }

    public void useOne() {
        if (!isInStock()) {
            throw new IllegalStateException("Part out of stock: " + getProductName());
        }
        quantity--;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
