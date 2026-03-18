package autoservice.repair.model;

public class Transmission {

    private final String type;   // Automatic / Manual / Semi-Automatic
    private final int gears;

    public Transmission(String type, int gears) {
        this.type = type;
        this.gears = gears;
    }

    public String getType() {
        return type;
    }

    public int getGears() {
        return gears;
    }
}

