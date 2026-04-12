import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads the rider's journey list to a JSON file (FR5).
 * Each rider's journeys are stored in: data/journeys/<RiderName>.json
 *
 * A plain JourneyData holder is used so Gson does not need to reconstruct
 * the Journey object directly (Journey has a final journeyId field).
 * Fares stored in the file are the already-calculated values — no
 * recalculation is done on load.
 */
public class JourneyStore {

    private static final String JOURNEYS_FOLDER = "data/journeys/";

    private final Gson gson;

    public JourneyStore() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Saves the rider's journeys to their JSON file.
     * Creates the data/journeys folder if it does not exist.
     * Returns true if saved successfully.
     */
    public boolean saveJourneys(List<Journey> journeys, String riderName) {
        new File(JOURNEYS_FOLDER).mkdirs();
        String filePath = buildFilePath(riderName);

        List<JourneyData> dataList = new ArrayList<>();
        for (Journey j : journeys) {
            JourneyData d      = new JourneyData();
            d.journeyId        = j.getJourneyId();
            d.date             = j.getDate();
            d.time             = j.getTime();
            d.fromZone         = j.getFromZone();
            d.toZone           = j.getToZone();
            d.passengerType    = j.getPassengerType().name();
            d.timeBand         = j.getTimeBand().name();
            d.paymentOption    = j.getPaymentOption().name();
            d.baseFare         = j.getBaseFare();
            d.discountedFare   = j.getDiscountedFare();
            d.chargedFare      = j.getChargedFare();
            dataList.add(d);
        }

        boolean saved;
        try {
            FileWriter writer = new FileWriter(filePath);
            gson.toJson(dataList, writer);
            writer.close();
            saved = true;
        } catch (IOException e) {
            System.out.println("Error: Could not save journeys. " + e.getMessage());
            saved = false;
        }
        return saved;
    }

    /**
     * Loads the rider's saved journeys from their JSON file.
     * Returns an empty list if the file does not exist or cannot be read.
     */
    public List<Journey> loadJourneys(String riderName) {
        String filePath = buildFilePath(riderName);
        List<Journey> journeys = new ArrayList<>();

        try {
            FileReader reader = new FileReader(filePath);
            Type listType = new TypeToken<List<JourneyData>>() {}.getType();
            List<JourneyData> dataList = gson.fromJson(reader, listType);
            reader.close();

            if (dataList != null) {
                for (JourneyData d : dataList) {
                    PassengerType pt = PassengerType.valueOf(d.passengerType);
                    TimeBand      tb = TimeBand.valueOf(d.timeBand);
                    PaymentOption po = PaymentOption.valueOf(d.paymentOption);

                    Journey j = new Journey(d.journeyId, d.date, d.time,
                            d.fromZone, d.toZone, pt, tb, po);
                    j.setFares(new FareResult(d.baseFare, d.discountedFare, d.chargedFare));
                    journeys.add(j);
                }
            }

        } catch (IOException e) {
            // No saved file — normal for a new rider
        } catch (Exception e) {
            System.out.println("Warning: Could not load saved journeys. " + e.getMessage());
        }

        return journeys;
    }

    /** Builds the file path for a rider's journey JSON file. */
    private String buildFilePath(String riderName) {
        String safeName = riderName.trim().replace(" ", "_");
        return JOURNEYS_FOLDER + safeName + ".json";
    }

    /**
     * Plain data holder used for JSON serialisation.
     * Avoids Gson issues with final fields in Journey.
     */
    private static class JourneyData {
        int    journeyId;
        String date;
        String time;
        int    fromZone;
        int    toZone;
        String passengerType;
        String timeBand;
        String paymentOption;
        double baseFare;
        double discountedFare;
        double chargedFare;
    }
}
