/**
 * Abstract base class for all users of the CityRide system.
 * Rider and Admin both extend this class (inheritance — required by the brief).
 *
 * 'abstract' means: you cannot create a User object directly.
 * You can only create a Rider or Admin, which both ARE a User.
 *
 * getRole() is abstract — every subclass must provide its own version.
 */
public abstract class User {

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    /**
     * Returns the role label for this user.
     * Rider returns "Rider". Admin returns "Admin".
     * Each subclass must implement this — the compiler will reject
     * any subclass that forgets to.
     */
    public abstract String getRole();
}
