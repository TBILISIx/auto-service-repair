package com.solvd.autoservicerepair.threads;

import lombok.extern.slf4j.Slf4j;

/**
 * Thread created via Runnable interface.
 * Simulates periodic garage status reporting.
 */

@Slf4j

/* In Java, you cannot just write "run this code in a separate thread" directly.
You need to package the code into an object first. Runnable is a functional interface from java.lang that has exactly one method:
When you implement Runnable, basically - "this class is a task that can be run by a thread."
The class itself is NOT a thread — it's just the instructions. Think of it like a recipe.
Thread reads the recipe (Runnable) and does the work. */

public class GarageStatusRunnable implements Runnable {

    private final String garageName;
    private final int reportCount;

    public GarageStatusRunnable(String garageName, int reportCount) {
        this.garageName = garageName;
        this.reportCount = reportCount;
    }

    @Override
    /* Overriding Functional interface Runnable exactly one abstract method like other Functional interface it has one too */
    public void run() {
        String threadName = Thread.currentThread().getName(); /* gives name of the thread that will use this run method useful for logging */
        log.info("[{}] GarageStatusRunnable started for garage: {}", threadName, garageName);

        for (int i = 1; i <= reportCount; i++) {
            log.info("[{}] Status report #{} for '{}': All systems operational.", threadName, i, garageName);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[{}] GarageStatusRunnable interrupted.", threadName);
                return;
            }
        }

        log.info("[{}] GarageStatusRunnable finished for garage: {}", threadName, garageName);
    }

}

