import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles importing journeys from a CSV file and exporting them to a CSV file (FR8).
 * Each line in the CSV file represents one journey.
 *
 * Import: reads the file line by line, skips the header, parses each line.
 *         Invalid lines are skipped with a warning — the program does not crash.
 * Export: writes all journeys to a new CSV file with a header row.
 *
 * Expected CSV column order:
 * ID, Date, Time, FromZone, ToZone, PassengerType, TimeBand, Payment, BaseFare, DiscountedFare, ChargedFare
 */
public class CsvHandler {

    private static final String CSV_HEADER =
            "ID,Date,Time,FromZone,ToZone,PassengerType,TimeBand,Payment,BaseFare,DiscountedFare,ChargedFare";

    // Column index constants — make the parsing code easier to read
    private static final int COL_ID             = 0;
    private static final int COL_DATE           = 1;
    private static final int COL_TIME           = 2;
    private static final int COL_FROM_ZONE      = 3;
    private static final int COL_TO_ZONE        = 4;
    private static final int COL_PASSENGER_TYPE = 5;
    private static final int COL_TIME_BAND      = 6;
    private static final int COL_PAYMENT        = 7;

    /**
     * Imports journeys from a CSV file at the given path.
     * The first line (header) is skipped automatically.
     * Returns a list of successfully parsed journeys.
     * Returns an empty list if the file cannot be opened.
     */
    public List<Journey> importFromCsv(String filePath) {
        List<Journey> journeys = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine(); // read and discard the header row

            while ((line = reader.readLine()) != null) {
                Journey journey = parseLine(line);
                if (journey != null) {
                    journeys.add(journey);
                }
            }
            reader.close();
            System.out.println("Imported " + journeys.size() + " journey(s) from: " + filePath);

        } catch (IOException e) {
            System.out.println("Error: Could not open file '" + filePath + "'. " + e.getMessage());
        }

        return journeys;
    }

    /**
     * Exports all journeys to a CSV file at the given path.
     * Returns true if the export was successful, false if an error occurred.
     */
    public boolean exportToCsv(List<Journey> journeys, String filePath) {
        boolean success;
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(CSV_HEADER + "\n");

            for (Journey j : journeys) {
                writer.write(buildCsvLine(j) + "\n");
            }

            writer.close();
            System.out.println("Journeys exported to: " + filePath);
            success = true;

        } catch (IOException e) {
            System.out.println("Error: Could not write to file '" + filePath + "'. " + e.getMessage());
            success = false;
        }
        return success;
    }

    /**
     * Converts one Journey object into a single CSV line string.
     * Uses the enum's .name() to get a consistent string for saving.
     */
    private String buildCsvLine(Journey j) {
        return j.getJourneyId() + ","
                + j.getDate() + ","
                + j.getTime() + ","
                + j.getFromZone() + ","
                + j.getToZone() + ","
                + j.getPassengerType().name() + ","
                + j.getTimeBand().name() + ","
                + j.getPaymentOption().name() + ","
                + String.format("%.2f", j.getBaseFare()) + ","
                + String.format("%.2f", j.getDiscountedFare()) + ","
                + String.format("%.2f", j.getChargedFare());
    }

    /**
     * Tries to parse one CSV line into a Journey object.
     * Returns null if the line is missing fields or contains invalid data.
     * This allows bad lines to be skipped without stopping the whole import.
     */
    private Journey parseLine(String line) {
        Journey journey = null;
        try {
            String[] parts = line.split(",");

            int id                    = Integer.parseInt(parts[COL_ID].trim());
            String date               = parts[COL_DATE].trim();
            String time               = parts[COL_TIME].trim();
            int fromZone              = Integer.parseInt(parts[COL_FROM_ZONE].trim());
            int toZone                = Integer.parseInt(parts[COL_TO_ZONE].trim());
            PassengerType passengerType = PassengerType.valueOf(parts[COL_PASSENGER_TYPE].trim());
            TimeBand timeBand         = TimeBand.valueOf(parts[COL_TIME_BAND].trim());
            PaymentOption payment     = PaymentOption.valueOf(parts[COL_PAYMENT].trim());

            journey = new Journey(id, date, time, fromZone, toZone,
                    passengerType, timeBand, payment);

        } catch (Exception e) {
            // Catches missing fields, wrong types, and invalid enum names
            System.out.println("Warning: Skipping invalid CSV line: " + line);
        }
        return journey;
    }
}
