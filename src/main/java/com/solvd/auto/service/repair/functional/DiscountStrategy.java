package com.solvd.auto.service.repair.functional;

@FunctionalInterface
public interface DiscountStrategy {

    double apply(double price);

}