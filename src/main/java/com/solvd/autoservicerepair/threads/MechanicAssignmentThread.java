package com.solvd.autoservicerepair.threads;

import lombok.extern.slf4j.Slf4j;

/**
 * Thread created by extending Thread class.
 * Simulates a mechanic being assigned to a service bay.
 */

@Slf4j

/* Extending Thread directly.Thread is a class in java.lang.
    When you extend it, class becomes Thread and you override its run() method directly.
    though now you have thread and task together */

public class MechanicAssignmentThread extends Thread {

    private final String mechanicName;
    private final String serviceName;

    public MechanicAssignmentThread(String mechanicName, String serviceName) {

        super("MechanicThread-" + mechanicName); /* Thread class private field name, by doing this i name thread when its created */
        this.mechanicName = mechanicName;
        this.serviceName = serviceName;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        log.info("[{}] Mechanic '{}' is being assigned to service: '{}'", threadName, mechanicName, serviceName);

        try {
            log.info("[{}] Mechanic '{}' started working on '{}'", threadName, mechanicName, serviceName);
            Thread.sleep(500); // simulate work
            log.info("[{}] Mechanic '{}' completed service: '{}'", threadName, mechanicName, serviceName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[{}] MechanicAssignmentThread was interrupted.", threadName);
        }
    }

}
