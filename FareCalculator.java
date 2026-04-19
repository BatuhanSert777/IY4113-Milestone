import java.util.EnumMap;

/**
 * Calculates fares for journeys based on the active system configuration.
 * Tracks how much each passenger type has been charged today so that
 * daily caps are applied correctly across multiple journeys (FR9, FR10).
 *
 * One FareCalculator is created per session (one per day).
 * After any deletion or edit, resetTotals() is called and all
 * journeys are recalculated from scratch by JourneyManager.
 */
public class FareCalculator {

    // Tracks the total charged for each passenger type so far today
    private final EnumMap<PassengerType, Double> runningTotals;

    private final AppConfig config;

    public FareCalculator(AppConfig config) {
        this.config = config;
        this.runningTotals = new EnumMap<>(PassengerType.class);
        resetTotals();
    }

    /**
     * Sets all running totals back to zero.
     * Called at the start of a new day, or after a journey is deleted or edited,
     * so that fares can be recalculated correctly from scratch.
     */
    public void resetTotals() {
        for (PassengerType type : PassengerType.values()) {
            runningTotals.put(type, 0.0);
        }
    }

    /**
     * Calculates the fare for one journey and updates the running total.
     *
     * Step 1: get base fare from config using zones crossed and time band
     * Step 2: apply passenger discount  (e.g. Student gets 25% off)
     * Step 3: apply daily cap           (charge only what's left up to the cap)
     *
     * Returns a FareResult containing all three values.
     */
    public FareResult calculateFare(int fromZone, int toZone,
                                    PassengerType passengerType,
                                    TimeBand timeBand) {
        int zonesCrossed = Math.abs(toZone - fromZone) + 1;

        double baseFare       = config.getBaseFare(zonesCrossed, timeBand);
        double discountRate   = config.getDiscountRate(passengerType);
        double discountedFare = baseFare * (1.0 - discountRate);

        double dailyCap     = config.getDailyCap(passengerType);
        double currentTotal = runningTotals.get(passengerType);

        double chargedFare;
        if (currentTotal >= dailyCap) {
            // Cap already reached — this journey is free
            chargedFare = 0.0;
        } else if (currentTotal + discountedFare > dailyCap) {
            // Partial charge — only charge up to the cap
            chargedFare = dailyCap - currentTotal;
        } else {
            // Normal charge — full discounted fare applies
            chargedFare = discountedFare;
        }

        runningTotals.put(passengerType, currentTotal + chargedFare);

        return new FareResult(baseFare, discountedFare, chargedFare);
    }

    /** Returns today's running total for the given passenger type. */
    public double getRunningTotal(PassengerType passengerType) {
        return runningTotals.get(passengerType);
    }
}
