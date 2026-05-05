package com.solvd.autoservicerepair.parsers.xmltojavaobject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Mirrors <sparePart>
 */

@Getter
@Setter


public class SparePartXml {

    private String productName;
    private String productNumber;
    private BigDecimal unitPrice;
    private int quantity;

    public String getProductName() {
        return productName;
    }
    public String getProductNumber() {
        return productNumber;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setProductName(String v) {
        productName = v;
    }
    public void setProductNumber(String v) {
        productNumber = v;
    }
    public void setUnitPrice(BigDecimal v) {
        unitPrice = v;
    }
    public void setQuantity(int v) {
        quantity = v;
    }

    /**
     * Derived — mirrors SparePart.isInStock()
     */
    public boolean isInStock() {
        return quantity > 0;
    }

    @Override
    public String toString() {
        return "SparePart{name='" + productName + "', qty=" + quantity +
                ", inStock=" + isInStock() + "}";
    }

}