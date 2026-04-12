/**
 * Whether a journey takes place during peak or off-peak hours.
 * Peak journeys cost more than off-peak journeys.
 */
public enum TimeBand {

    PEAK    ("Peak"),
    OFF_PEAK("Off-Peak");

    private final String displayName;

    TimeBand(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    /**
     * Converts user text input to the correct TimeBand.
     * Returns null if the input does not match, so the caller can show an error.
     */
    public static TimeBand fromString(String input) {
        if (input == null) {
            return null;
        }
        String cleaned = input.trim().toLowerCase();
        switch (cleaned) {
            case "peak":
                return PEAK;
            case "off-peak":
            case "off peak":
            case "offpeak":
            case "off_peak":
                return OFF_PEAK;
            default:
                return null;
        }
    }
}
