package com.solvd.auto.service.repair.model;

import java.math.BigDecimal;

public abstract class Product {

    private final String productName;
    private final String productNumber;
    private final BigDecimal unitPrice;

    public Product(String productName, String productNumber, BigDecimal unitPrice) {
        this.productName = productName;
        this.productNumber = productNumber;
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

}



