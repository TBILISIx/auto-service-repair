package com.solvd.autoservicerepair.enums;

/**
 * The status of an Appointment. Used in: Appointment class
 **/

public enum ServiceStatus {

    SCHEDULED("Scheduled", true),
    IN_PROGRESS("In Progress", false),
    DONE("Done", false),
    CANCELLED("Cancelled", false);

    private final String displayName;
    private final boolean canBeCancelled;

    ServiceStatus(String displayName, boolean canBeCancelled) {
        this.displayName = displayName;
        this.canBeCancelled = canBeCancelled;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canBeCancelled() {
        return canBeCancelled;
    }

}