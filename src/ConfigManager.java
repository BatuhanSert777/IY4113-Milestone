import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles loading and saving the system configuration (AppConfig) as JSON.
 * Uses the Gson library to convert the AppConfig object to and from JSON text.
 *
 * If config.json does not exist, safe default values are used instead (FR2, FR3).
 * This class only deals with file operations — it has no fare logic.
 */
public class ConfigManager {

    private static final String CONFIG_PATH = "data/config.json";

    private final Gson gson;

    public ConfigManager() {
        // Pretty printing makes the JSON file readable if opened manually
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Loads the config from config.json.
     * If the file is missing or unreadable, returns a config with safe defaults.
     * The program should never crash because of a missing config file.
     */
    public AppConfig loadConfig() {
        AppConfig config;
        try {
            FileReader reader = new FileReader(CONFIG_PATH);
            config = gson.fromJson(reader, AppConfig.class);
            reader.close();

            if (config == null) {
                // File existed but was empty
                config = buildDefaultConfig();
            }
            System.out.println("Configuration loaded.");

        } catch (IOException e) {
            // File not found — this is normal on first run
            System.out.println("No config file found. Starting with default settings.");
            config = buildDefaultConfig();
        }
        return config;
    }

    /**
     * Saves the current config to config.json.
     * Returns true if saved successfully, false if an error occurred.
     */
    public boolean saveConfig(AppConfig config) {
        boolean saved;
        try {
            FileWriter writer = new FileWriter(CONFIG_PATH);
            gson.toJson(config, writer);
            writer.close();
            System.out.println("Configuration saved to " + CONFIG_PATH);
            saved = true;
        } catch (IOException e) {
            System.out.println("Error: Could not save configuration. " + e.getMessage());
            saved = false;
        }
        return saved;
    }

    /** Creates and returns an AppConfig filled with default values. */
    private AppConfig buildDefaultConfig() {
        AppConfig config = new AppConfig();
        config.loadDefaults();
        return config;
    }
}
