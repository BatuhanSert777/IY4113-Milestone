import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles saving and loading a Rider's profile as a JSON file (FR4, FR5).
 * Each rider's profile is stored in: data/profiles/<RiderName>.json
 *
 * Enums are saved as plain strings (e.g. "STUDENT") and converted back
 * when loading, to keep the JSON handling simple and reliable.
 */
public class ProfileManager {

    private static final String PROFILES_FOLDER = "data/profiles/";

    private final Gson gson;

    public ProfileManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Saves the rider's profile to a JSON file.
     * The filename uses the rider's name with spaces replaced by underscores.
     * Returns true if saved successfully, false if an error occurred.
     */
    public boolean saveProfile(Rider rider) {
        String filePath = buildFilePath(rider.getName());

        // Use a plain data holder so Gson does not need to handle enums directly
        ProfileData data = new ProfileData();
        data.name           = rider.getName();
        data.passengerType  = rider.getPassengerType().name();
        data.defaultPayment = rider.getDefaultPayment().name();

        boolean saved;
        try {
            FileWriter writer = new FileWriter(filePath);
            gson.toJson(data, writer);
            writer.close();
            System.out.println("Profile saved to: " + filePath);
            saved = true;
        } catch (IOException e) {
            System.out.println("Error: Could not save profile. " + e.getMessage());
            saved = false;
        }
        return saved;
    }

    /**
     * Loads a rider's profile from their JSON file.
     * Returns the Rider object if found, or null if the file does not exist.
     */
    public Rider loadProfile(String riderName) {
        String filePath = buildFilePath(riderName);
        Rider rider = null;

        try {
            FileReader reader = new FileReader(filePath);
            ProfileData data = gson.fromJson(reader, ProfileData.class);
            reader.close();

            if (data == null) {
                System.out.println("Profile file was empty.");
            } else {
                // Convert the stored strings back to enums
                PassengerType type    = PassengerType.valueOf(data.passengerType);
                PaymentOption payment = PaymentOption.valueOf(data.defaultPayment);
                rider = new Rider(data.name, type, payment);
                System.out.println("Profile loaded for: " + data.name);
            }

        } catch (IOException e) {
            System.out.println("No saved profile found for: " + riderName);
        } catch (IllegalArgumentException e) {
            // valueOf() failed — the JSON file contains an invalid enum name
            System.out.println("Error: Profile file contains invalid data and could not be loaded.");
        }

        return rider;
    }

    /**
     * Builds the full file path for a rider's JSON profile.
     * Spaces in the name are replaced with underscores for a safe filename.
     */
    private String buildFilePath(String riderName) {
        String safeName = riderName.trim().replace(" ", "_");
        return PROFILES_FOLDER + safeName + ".json";
    }

    /**
     * Private helper used only for JSON serialisation.
     * Holds the rider's data as plain strings so Gson handles it cleanly.
     */
    private static class ProfileData {
        String name;
        String passengerType;
        String defaultPayment;
    }
}
