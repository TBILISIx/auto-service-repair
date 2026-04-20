package com.solvd.autoservicerepair.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class SleepUtils {

    private SleepUtils() {

    }

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
