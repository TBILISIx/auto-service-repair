package autoservice.repair.enums;

/**
 * Represents a mechanic's seniority level. Used in: Mechanic class
 */

public enum MechanicSeniorityLevel {

    JUNIOR("Junior", 0, 2),
    MID("Mid-Level", 3, 9),
    SENIOR("Senior", 10, 24),
    MASTER("Master Technician", 25, 99);

    private final String displayName;
    private final int minYears;
    private final int maxYears;

    MechanicSeniorityLevel(String displayName, int minYears, int maxYears) {
        this.displayName = displayName;
        this.minYears = minYears;
        this.maxYears = maxYears;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinYears() {
        return minYears;
    }

    public int getMaxYears() {
        return maxYears;
    }

    public boolean isSeniorLevel() {
        return minYears >= 10;
    }

}