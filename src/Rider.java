/**
 * Represents a rider (normal user) in the CityRide system.
 * Extends User and adds a passenger type and a default payment option.
 *
 * The rider's profile (name, passenger type, payment) can be
 * saved to and loaded from a JSON file (FR4, FR5).
 */
public class Rider extends User {

    private PassengerType passengerType;
    private PaymentOption defaultPayment;

    public Rider(String name, PassengerType passengerType, PaymentOption defaultPayment) {
        super(name); // passes the name up to User
        this.passengerType = passengerType;
        this.defaultPayment = defaultPayment;
    }

    @Override
    public String getRole() {
        return "Rider";
    }

    /** Prints the rider's current profile to the console. */
    public void printProfile() {
        System.out.println("--- Rider Profile ---");
        System.out.println("Name:            " + getName());
        System.out.println("Passenger type:  " + passengerType.getDisplayName());
        System.out.println("Default payment: " + defaultPayment.getDisplayName());
    }

    public PassengerType getPassengerType()                       { return passengerType; }
    public void setPassengerType(PassengerType passengerType)     { this.passengerType = passengerType; }

    public PaymentOption getDefaultPayment()                      { return defaultPayment; }
    public void setDefaultPayment(PaymentOption defaultPayment)   { this.defaultPayment = defaultPayment; }
}
