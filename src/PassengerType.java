/**
 * The four types of passenger supported by CityRide Lite.
 * Each type has a discount rate, a daily spending cap, and a display name.
 *
 * Discount rate: how much cheaper the fare is (0.25 = 25% off).
 * Daily cap: the most this passenger type will be charged in one day.
 */
public enum PassengerType {

    ADULT         (0.00, 8.00, "Adult"),
    STUDENT       (0.25, 6.00, "Student"),
    CHILD         (0.50, 4.00, "Child"),
    SENIOR_CITIZEN(0.30, 7.00, "Senior Citizen");

    private final double discountRate;
    private final double dailyCap;
    private final String displayName;

    PassengerType(double discountRate, double dailyCap, String displayName) {
        this.discountRate = discountRate;
        this.dailyCap     = dailyCap;
        this.displayName  = displayName;
    }

    public double getDiscountRate() { return discountRate; }
    public double getDailyCap()     { return dailyCap; }
    public String getDisplayName()  { return displayName; }

    /**
     * Converts user text input to the correct PassengerType.
     * Returns null if the input does not match any type.
     * The caller should check for null and show an error message.
     */
    public static PassengerType fromString(String input) {
        if (input == null) {
            return null;
        }
        String cleaned = input.trim().toLowerCase();
        switch (cleaned) {
            case "adult":
                return ADULT;
            case "student":
                return STUDENT;
            case "child":
                return CHILD;
            case "senior":
            case "senior citizen":
            case "senior_citizen":
                return SENIOR_CITIZEN;
            default:
                return null;
        }
    }
}
