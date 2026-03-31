package autoservice.repair.enums;

/** Types of RepairService. Used in:OilChange, TireChange, BrakeRepair **/
public enum ServiceType {

    OIL_CHANGE("Oil Change", "Maintenance", false),
    TIRE_CHANGE("Tire Change", "Maintenance", true),
    BRAKE_REPAIR("Brake Repair", "Safety", true),
    ENGINE_DIAGNOSTIC("Engine Diagnostic", "Diagnostic", false),
    GENERAL_INSPECTION("General Inspection", "Inspection", true);

    private final String displayName;
    private final String category;
    private final boolean isSafetyRelated;

    ServiceType(String displayName, String category, boolean isSafetyRelated) {
        this.displayName = displayName;
        this.category = category;
        this.isSafetyRelated = isSafetyRelated;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategory() {
        return category;
    }

    public boolean isSafetyRelated() {
        return isSafetyRelated;
    }

}