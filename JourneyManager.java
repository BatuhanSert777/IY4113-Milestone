import java.util.ArrayList;
import java.util.List;

/**
 * Manages the list of journeys for the current day.
 * Handles adding, editing, deleting, and listing journeys (FR6).
 *
 * After any edit or deletion, all fares are recalculated from scratch
 * so that daily caps are always applied correctly (FR6a, FR10).
 */
public class JourneyManager {

    private final List<Journey> journeys;
    private final FareCalculator fareCalculator;
    private int nextJourneyId;

    public JourneyManager(FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
        this.journeys = new ArrayList<>();
        this.nextJourneyId = 1;
    }

    /**
     * Creates a new journey, calculates its fare, and adds it to the list.
     * Returns the completed Journey so the menu can display its details.
     */
    public Journey addJourney(String date, String time,
                              int fromZone, int toZone,
                              PassengerType passengerType,
                              TimeBand timeBand,
                              PaymentOption paymentOption) {
        Journey journey = new Journey(
                nextJourneyId, date, time,
                fromZone, toZone,
                passengerType, timeBand, paymentOption);

        FareResult result = fareCalculator.calculateFare(
                fromZone, toZone, passengerType, timeBand);
        journey.setFares(result);

        journeys.add(journey);
        nextJourneyId++;

        return journey;
    }

    /**
     * Edits an existing journey by updating its fields using setters.
     * The journey ID stays the same — only the details change.
     * All fares are recalculated after the edit (FR6a).
     * Returns the updated Journey, or null if the ID was not found (FR6b).
     */
    public Journey editJourney(int journeyId, String date, String time,
                               int fromZone, int toZone,
                               PassengerType passengerType,
                               TimeBand timeBand,
                               PaymentOption paymentOption) {
        int index = findIndexById(journeyId);
        if (index == -1) {
            return null; // ID not found — caller will show an error
        }

        // Update the existing object in place — do not create a new Journey
        Journey journey = journeys.get(index);
        journey.setDate(date);
        journey.setTime(time);
        journey.setFromZone(fromZone);
        journey.setToZone(toZone);
        journey.setPassengerType(passengerType);
        journey.setTimeBand(timeBand);
        journey.setPaymentOption(paymentOption);

        recalculateAllFares();

        return journeys.get(index);
    }

    /**
     * Deletes a journey by its ID.
     * Recalculates all remaining fares after deletion (FR6a).
     * Returns true if deleted, false if the ID was not found (FR6b).
     */
    public boolean deleteJourney(int journeyId) {
        int index = findIndexById(journeyId);
        if (index == -1) {
            return false; // ID not found
        }
        journeys.remove(index);
        recalculateAllFares();
        return true;
    }

    /**
     * Replaces the current journey list with an imported list.
     * Used after importing from CSV. Recalculates all fares on load.
     */
    public void loadJourneys(List<Journey> imported) {
        journeys.clear();
        journeys.addAll(imported);

        // Move the ID counter past the highest existing ID
        int maxId = 0;
        for (Journey j : journeys) {
            if (j.getJourneyId() > maxId) {
                maxId = j.getJourneyId();
            }
        }
        nextJourneyId = maxId + 1;

        recalculateAllFares();
    }

    /** Prints all journeys to the console (FR11). */
    public void printAllJourneys() {
        if (journeys.isEmpty()) {
            System.out.println("No journeys recorded yet.");
            return;
        }
        System.out.println("\n--- All Journeys Today ---");
        for (Journey j : journeys) {
            j.printSummary();
        }
    }

    /** Returns a copy of the journey list so callers cannot modify the original. */
    public List<Journey> getAllJourneys() {
        return new ArrayList<>(journeys);
    }

    /** Returns true if no journeys have been recorded yet. */
    public boolean isEmpty() {
        return journeys.isEmpty();
    }

    /**
     * Resets all fare totals and recalculates every journey from scratch.
     * This ensures daily caps are applied correctly after any change.
     * Private — only called internally after edit or delete.
     */
    private void recalculateAllFares() {
        fareCalculator.resetTotals();
        for (Journey j : journeys) {
            FareResult result = fareCalculator.calculateFare(
                    j.getFromZone(), j.getToZone(),
                    j.getPassengerType(), j.getTimeBand());
            j.setFares(result);
        }
    }

    /**
     * Returns the index of a journey in the list by its ID.
     * Returns -1 if no journey with that ID exists.
     */
    private int findIndexById(int journeyId) {
        int foundIndex = -1;
        for (int i = 0; i < journeys.size(); i++) {
            if (journeys.get(i).getJourneyId() == journeyId) {
                foundIndex = i;
            }
        }
        return foundIndex;
    }
}
