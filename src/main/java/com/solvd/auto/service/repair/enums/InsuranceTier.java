package com.solvd.auto.service.repair.enums;

/**
 * Tiers of customers insurance policy, Used in : Insurance (record)
 **/

public enum InsuranceTier {

    BASIC("Basic", 20, false),
    STANDARD("Standard", 50, false),
    PREMIUM("Premium", 80, true);

    private final String displayName;
    private final int coveragePercent;
    private final boolean includesRoadsideAssistance;

    InsuranceTier(String displayName, int coveragePercent, boolean includesRoadsideAssistance) {
        this.displayName = displayName;
        this.coveragePercent = coveragePercent;
        this.includesRoadsideAssistance = includesRoadsideAssistance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCoveragePercent() {
        return coveragePercent;
    }

    public boolean includesRoadsideAssistance() {
        return includesRoadsideAssistance;
    }

}