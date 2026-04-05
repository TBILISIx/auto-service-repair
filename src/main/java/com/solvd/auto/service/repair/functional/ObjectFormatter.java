package com.solvd.auto.service.repair.functional;

@FunctionalInterface
public interface ObjectFormatter<T> {

    String format(T obj);

}
