package com.solvd.autoservicerepair.interfaces;


public interface Parser<T> {
    T parse(String filePath) throws Exception;
}