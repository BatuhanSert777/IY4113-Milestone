/**
 * The payment method a rider uses for their journeys.
 * Stored in the rider's profile as their default payment option.
 */
public enum PaymentOption {

    CONTACTLESS("Contactless"),
    OYSTER     ("Oyster Card"),
    CASH       ("Cash");

    private final String displayName;

    PaymentOption(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    /**
     * Converts user text input to the correct PaymentOption.
     * Returns null if no match found, so the caller can show an error.
     */
    public static PaymentOption fromString(String input) {
        if (input == null) {
            return null;
        }
        String cleaned = input.trim().toLowerCase();
        switch (cleaned) {
            case "contactless":
                return CONTACTLESS;
            case "oyster":
            case "oyster card":
                return OYSTER;
            case "cash":
                return CASH;
            default:
                return null;
        }
    }
}
