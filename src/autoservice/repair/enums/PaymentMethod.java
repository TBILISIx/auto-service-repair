package autoservice.repair.enums;

/**
 * Payment method used in a Payment. Used in: Payment class
 **/
public enum PaymentMethod {

    CASH("Cash", 0.0),
    CARD("Card", 1.5),
    TRANSFER("Bank Transfer", 0.5);

    private final String displayName;
    private final double processingFeePercent;

    PaymentMethod(String displayName, double processingFeePercent) {
        this.displayName = displayName;
        this.processingFeePercent = processingFeePercent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getProcessingFeePercent() {
        return processingFeePercent;
    }

    public boolean hasFee() {
        return processingFeePercent > 0;
    }

}