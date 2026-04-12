/**
 * Represents an administrator in the CityRide system.
 * Extends User and adds password-based authentication (FR15).
 *
 * The Admin can view and update the system configuration.
 * Access is protected by a password stored in AppConfig.
 */
public class Admin extends User {

    public Admin() {
        super("Administrator"); // Admin always has this fixed name
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    /**
     * Checks whether the entered password matches the one stored in the config.
     * Returns true if correct, false otherwise.
     */
    public boolean authenticate(String enteredPassword, AppConfig config) {
        return config.isAdminPasswordCorrect(enteredPassword);
    }
}
