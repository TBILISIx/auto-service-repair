package com.solvd.autoservicerepair.functional;

@FunctionalInterface
public interface DiscountStrategy {

    double apply(double price);

}