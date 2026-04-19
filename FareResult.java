/**
 * Holds the three fare values produced by FareCalculator for one journey.
 *
 * baseFare       — the standard fare before any discount
 * discountedFare — the fare after the passenger discount is applied
 * chargedFare    — the final amount charged, after the daily cap is applied
 *
 * All fields are final because a FareResult should never change after creation.
 */
public class FareResult {

    private final double baseFare;
    private final double discountedFare;
    private final double chargedFare;

    public FareResult(double baseFare, double discountedFare, double chargedFare) {
        this.baseFare       = baseFare;
        this.discountedFare = discountedFare;
        this.chargedFare    = chargedFare;
    }

    public double getBaseFare()       { return baseFare; }
    public double getDiscountedFare() { return discountedFare; }
    public double getChargedFare()    { return chargedFare; }
}
