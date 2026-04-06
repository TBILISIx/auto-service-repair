package com.solvd.autoservicerepair.functional;

@FunctionalInterface
public interface ObjectFormatter<T> {

    String format(T obj);

}
