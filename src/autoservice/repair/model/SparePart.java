package autoservice.repair.model;

import autoservice.repair.exceptions.InvalidArgumentException;
import autoservice.repair.exceptions.OutOfStockException;

import java.math.BigDecimal;

public class SparePart extends Product implements Sellable {

    private Integer quantity;

    public SparePart(String partName, String partNumber, BigDecimal unitPrice, Integer quantity) {
        super(partName, partNumber, unitPrice);
        if (quantity < 0) {
            throw new InvalidArgumentException("Quantity cannot be negative for part: " + partName);
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
            throw new OutOfStockException("Part out of stock: " + getProductName());
        }
        quantity--;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public BigDecimal getSellingPrice() {

        return getUnitPrice();

    }

}
