package com.solvd.auto.service.repair.model;

import com.solvd.auto.service.repair.exceptions.InvalidArgumentException;
import com.solvd.auto.service.repair.exceptions.OutOfStockException;
import com.solvd.auto.service.repair.interfaces.Sellable;

import java.math.BigDecimal;

public class SparePart extends Product implements Sellable {

    private Integer quantity;

    public SparePart(String productName, String productNumber, BigDecimal unitPrice, Integer quantity) {
        super(productName, productNumber, unitPrice);
        if (quantity < 0) {
            throw new InvalidArgumentException("Quantity cannot be negative for part: " + productName);
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

        return getUnitPrice().multiply(new BigDecimal("1.3"));

    }

}
