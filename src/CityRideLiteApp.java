import java.util.*;
import java.util.regex.Pattern;

public class CityRideLiteApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        JourneyManager manager = new JourneyManager();

        while (true) {
            System.out.println("\nCityRide Lite");
            System.out.println("1. Add Journey");
            System.out.println("2. Remove Journey");
            System.out.println("3. List Journeys");
            System.out.println("4. Filter Journeys");
            System.out.println("5. Daily Summary");
            System.out.println("6. Totals by Passenger Type");
            System.out.println("7. Exit");
            System.out.print("Choice: ");

            String choiceStr = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice!");
                continue;
            }

            switch (choice) {
                case 1 -> manager.addJourneyInteractive(sc);
                case 2 -> manager.removeJourneyInteractive(sc);
                case 3 -> manager.listJourneys();
                case 4 -> manager.filterJourneysInteractive(sc);
                case 5 -> manager.dailySummary();
                case 6 -> manager.totalsByPassengerType();
                case 7 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    enum PassengerType {
        ADULT(0.00, 8.00, "Adult"),
        STUDENT(0.25, 6.00, "Student"),
        CHILD(0.50, 4.00, "Child"),
        SENIOR_CITIZEN(0.30, 7.00, "Senior Citizen");

        private final double discountRate;
        private final double dailyCap;
        private final String displayName;

        PassengerType(double discountRate, double dailyCap, String displayName) {
            this.discountRate = discountRate;
            this.dailyCap = dailyCap;
            this.displayName = displayName;
        }

        public double discountRate() {
            return discountRate;
        }

        public double dailyCap() {
            return dailyCap;
        }

        public String displayName() {
            return displayName;
        }

        public static PassengerType parse(String input) {
            if (input == null) return null;
            String s = input.trim().toLowerCase();
            return switch (s) {
                case "adult" -> ADULT;
                case "student" -> STUDENT;
                case "child" -> CHILD;
                case "senior", "senior citizen", "senior_citizen" -> SENIOR_CITIZEN;
                default -> null;
            };
        }
    }

    enum TimeBand {
        PEAK("Peak"),
        OFF_PEAK("Off-peak");

        private final String displayName;

        TimeBand(String displayName) {
            this.displayName = displayName;
        }

        public String displayName() {
            return displayName;
        }

        public static TimeBand parse(String input) {
            if (input == null) return null;
            String s = input.trim().toLowerCase();
            return switch (s) {
                case "peak" -> PEAK;
                case "offpeak", "off-peak", "off_peak" -> OFF_PEAK;
                default -> null;
            };
        }
    }

    static final class CityRideDataset {
        private static final Map<String, Double> BASE_FARES = new HashMap<>();

        static {
            BASE_FARES.put("1:OFF_PEAK", 2.50);
            BASE_FARES.put("1:PEAK", 3.00);

            BASE_FARES.put("2:OFF_PEAK", 3.20);
            BASE_FARES.put("2:PEAK", 3.70);

            BASE_FARES.put("3:OFF_PEAK", 4.00);
            BASE_FARES.put("3:PEAK", 4.50);

            BASE_FARES.put("4:OFF_PEAK", 4.80);
            BASE_FARES.put("4:PEAK", 5.30);

            BASE_FARES.put("5:OFF_PEAK", 5.60);
            BASE_FARES.put("5:PEAK", 6.10);
        }

        private CityRideDataset() {
        }

        public static double getBaseFare(int fromZone, int toZone, TimeBand timeBand) {
            int zonesCrossed = Math.abs(toZone - fromZone) + 1;
            String key = zonesCrossed + ":" + timeBand.name();
            Double fare = BASE_FARES.get(key);
            if (fare != null) return fare;

            double base = 2.50 + (zonesCrossed - 1) * 0.70;
            return timeBand == TimeBand.PEAK ? base + 0.50 : base;
        }
    }

    static final class FareService {
        private final EnumMap<PassengerType, Double> runningTotals = new EnumMap<>(PassengerType.class);

        public FareService() {
            resetTotals();
        }

        public void resetTotals() {
            for (PassengerType pt : PassengerType.values()) {
                runningTotals.put(pt, 0.0);
            }
        }

        public FareResult calculateAndApplyFare(int fromZone, int toZone, PassengerType passengerType, TimeBand timeBand) {
            double baseFare = CityRideDataset.getBaseFare(fromZone, toZone, timeBand);
            double discountedFare = baseFare * (1.0 - passengerType.discountRate());

            double currentTotal = runningTotals.get(passengerType);
            double cap = passengerType.dailyCap();

            double chargedFare;
            if (currentTotal >= cap) {
                chargedFare = 0.0;
            } else if (currentTotal + discountedFare > cap) {
                chargedFare = cap - currentTotal;
            } else {
                chargedFare = discountedFare;
            }

            runningTotals.put(passengerType, currentTotal + chargedFare);
            return new FareResult(baseFare, discountedFare, chargedFare);
        }

        public EnumMap<PassengerType, Double> getRunningTotals() {
            return runningTotals;
        }

        public record FareResult(double baseFare, double discountedFare, double chargedFare) {
        }
    }

    static final class Journey {
        private final int journeyId;
        private final String date;
        private final int fromZone;
        private final int toZone;
        private final int zonesCrossed;
        private final PassengerType passengerType;
        private final TimeBand timeBand;

        private double baseFare;
        private double discountedFare;
        private double chargedFare;

        public Journey(int journeyId, String date, int fromZone, int toZone, PassengerType passengerType, TimeBand timeBand) {
            this.journeyId = journeyId;
            this.date = date;
            this.fromZone = fromZone;
            this.toZone = toZone;
            this.zonesCrossed = Math.abs(toZone - fromZone) + 1;
            this.passengerType = passengerType;
            this.timeBand = timeBand;
        }

        public int getJourneyId() {
            return journeyId;
        }

        public String getDate() {
            return date;
        }

        public int getFromZone() {
            return fromZone;
        }

        public int getToZone() {
            return toZone;
        }

        public int getZonesCrossed() {
            return zonesCrossed;
        }

        public PassengerType getPassengerType() {
            return passengerType;
        }

        public TimeBand getTimeBand() {
            return timeBand;
        }

        public double getBaseFare() {
            return baseFare;
        }

        public double getDiscountedFare() {
            return discountedFare;
        }

        public double getChargedFare() {
            return chargedFare;
        }

        public void setFareBreakdown(double baseFare, double discountedFare, double chargedFare) {
            this.baseFare = baseFare;
            this.discountedFare = discountedFare;
            this.chargedFare = chargedFare;
        }

        public void displayDetails() {
            System.out.println(
                    "[" + journeyId + "] Date: " + date +
                            " | From: " + fromZone +
                            " | To: " + toZone +
                            " | Zones: " + zonesCrossed +
                            " | Passenger: " + passengerType.displayName() +
                            " | Time: " + timeBand.displayName() +
                            " | Base: £" + String.format("%.2f", baseFare) +
                            " | Discounted: £" + String.format("%.2f", discountedFare) +
                            " | Charged: £" + String.format("%.2f", chargedFare)
            );
        }
    }

    static final class JourneyManager {
        private final List<Journey> journeys = new ArrayList<>();
        private final FareService fareService = new FareService();
        private int nextJourneyId = 1;

        private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");

        public void addJourneyInteractive(Scanner sc) {
            String date = readValidDate(sc);
            int fromZone = readValidZone(sc, "Enter fromZone (1-5): ");
            int toZone = readValidZone(sc, "Enter toZone (1-5): ");
            TimeBand timeBand = readValidTimeBand(sc);
            PassengerType passengerType = readValidPassengerType(sc);

            Journey j = new Journey(nextJourneyId, date, fromZone, toZone, passengerType, timeBand);
            FareService.FareResult res = fareService.calculateAndApplyFare(fromZone, toZone, passengerType, timeBand);
            j.setFareBreakdown(res.baseFare(), res.discountedFare(), res.chargedFare());

            journeys.add(j);
            System.out.println("Journey added successfully. Journey ID: " + nextJourneyId +
                    " | Charged: £" + String.format("%.2f", res.chargedFare()));
            nextJourneyId++;
        }

        public void removeJourneyInteractive(Scanner sc) {
            if (journeys.isEmpty()) {
                System.out.println("No journeys recorded.");
                return;
            }

            Integer id = readInt(sc, "Enter Journey ID to remove: ");
            if (id == null) {
                System.out.println("Invalid input.");
                return;
            }

            boolean removed = removeJourneyById(id);
            if (removed) {
                System.out.println("Journey removed successfully.");
            } else {
                System.out.println("Journey ID not found.");
            }
        }

        public boolean removeJourneyById(int journeyId) {
            for (int i = 0; i < journeys.size(); i++) {
                if (journeys.get(i).getJourneyId() == journeyId) {
                    journeys.remove(i);
                    recomputeAllFares();
                    return true;
                }
            }
            return false;
        }

        public void listJourneys() {
            if (journeys.isEmpty()) {
                System.out.println("No journeys recorded.");
                return;
            }
            System.out.println("\nAll Journeys:");
            for (Journey j : journeys) {
                j.displayDetails();
            }
        }

        public void filterJourneysInteractive(Scanner sc) {
            if (journeys.isEmpty()) {
                System.out.println("No journeys recorded.");
                return;
            }

            System.out.println("\nFilter Journeys");
            System.out.println("1. Passenger Type");
            System.out.println("2. Time Band");
            System.out.println("3. Zone (From/To)");
            System.out.println("4. Date");
            System.out.print("Choice: ");

            String c = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(c);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option.");
                return;
            }

            List<Journey> results = new ArrayList<>();

            switch (choice) {
                case 1 -> {
                    PassengerType pt = readValidPassengerType(sc);
                    for (Journey j : journeys) {
                        if (j.getPassengerType() == pt) results.add(j);
                    }
                }
                case 2 -> {
                    TimeBand tb = readValidTimeBand(sc);
                    for (Journey j : journeys) {
                        if (j.getTimeBand() == tb) results.add(j);
                    }
                }
                case 3 -> {
                    Integer zone = readInt(sc, "Enter zone (1-5): ");
                    if (zone == null || zone < 1 || zone > 5) {
                        System.out.println("Invalid zone.");
                        return;
                    }
                    System.out.print("Match (1) fromZone or (2) toZone? ");
                    String which = sc.nextLine().trim();
                    if (!which.equals("1") && !which.equals("2")) {
                        System.out.println("Invalid option.");
                        return;
                    }
                    for (Journey j : journeys) {
                        if (which.equals("1") && j.getFromZone() == zone) results.add(j);
                        if (which.equals("2") && j.getToZone() == zone) results.add(j);
                    }
                }
                case 4 -> {
                    String date = readValidDate(sc);
                    for (Journey j : journeys) {
                        if (j.getDate().equals(date)) results.add(j);
                    }
                }
                default -> {
                    System.out.println("Invalid option.");
                    return;
                }
            }

            if (results.isEmpty()) {
                System.out.println("No journeys found.");
                return;
            }

            System.out.println("\nFiltered Journeys:");
            for (Journey j : results) {
                j.displayDetails();
            }
        }

        public void dailySummary() {
            if (journeys.isEmpty()) {
                System.out.println("No journeys recorded.");
                return;
            }

            int totalJourneys = journeys.size();
            double totalCost = 0.0;

            Journey mostExpensive = null;
            double maxCharge = -1.0;

            int peakCount = 0;
            int offPeakCount = 0;

            Map<Integer, Integer> zonesCrossedCounts = new HashMap<>();
            for (int i = 1; i <= 5; i++) zonesCrossedCounts.put(i, 0);

            for (Journey j : journeys) {
                totalCost += j.getChargedFare();

                if (j.getTimeBand() == TimeBand.PEAK) peakCount++;
                else offPeakCount++;

                zonesCrossedCounts.put(j.getZonesCrossed(),
                        zonesCrossedCounts.getOrDefault(j.getZonesCrossed(), 0) + 1);

                if (j.getChargedFare() > maxCharge) {
                    maxCharge = j.getChargedFare();
                    mostExpensive = j;
                }
            }

            double averageCost = totalCost / totalJourneys;

            System.out.println("\nDaily Summary");
            System.out.println("Total journeys: " + totalJourneys);
            System.out.println("Total cost charged: £" + String.format("%.2f", totalCost));
            System.out.println("Average cost per journey: £" + String.format("%.2f", averageCost));

            if (mostExpensive != null) {
                System.out.println("Most expensive journey ID: " + mostExpensive.getJourneyId() +
                        " (£" + String.format("%.2f", mostExpensive.getChargedFare()) + ")");
            }

            System.out.println("Peak journeys: " + peakCount);
            System.out.println("Off-peak journeys: " + offPeakCount);

            System.out.println("Zone statistics (zones crossed):");
            for (int z = 1; z <= 5; z++) {
                System.out.println("  " + z + " zone(s): " + zonesCrossedCounts.getOrDefault(z, 0));
            }
        }

        public void totalsByPassengerType() {
            if (journeys.isEmpty()) {
                System.out.println("No journeys recorded.");
                return;
            }

            EnumMap<PassengerType, Integer> counts = new EnumMap<>(PassengerType.class);
            EnumMap<PassengerType, Double> baseTotals = new EnumMap<>(PassengerType.class);
            EnumMap<PassengerType, Double> discountedTotals = new EnumMap<>(PassengerType.class);
            EnumMap<PassengerType, Double> chargedTotals = new EnumMap<>(PassengerType.class);

            for (PassengerType pt : PassengerType.values()) {
                counts.put(pt, 0);
                baseTotals.put(pt, 0.0);
                discountedTotals.put(pt, 0.0);
                chargedTotals.put(pt, 0.0);
            }

            for (Journey j : journeys) {
                PassengerType pt = j.getPassengerType();
                counts.put(pt, counts.get(pt) + 1);
                baseTotals.put(pt, baseTotals.get(pt) + j.getBaseFare());
                discountedTotals.put(pt, discountedTotals.get(pt) + j.getDiscountedFare());
                chargedTotals.put(pt, chargedTotals.get(pt) + j.getChargedFare());
            }

            System.out.println("\nTotals by Passenger Type");
            for (PassengerType pt : PassengerType.values()) {
                double cap = pt.dailyCap();
                double charged = chargedTotals.get(pt);
                String capReached = charged >= cap ? "Yes" : "No";

                System.out.println("\n" + pt.displayName());
                System.out.println("Journeys: " + counts.get(pt));
                System.out.println("Pre-discount total: £" + String.format("%.2f", baseTotals.get(pt)));
                System.out.println("Discounted total: £" + String.format("%.2f", discountedTotals.get(pt)));
                System.out.println("Charged total: £" + String.format("%.2f", charged));
                System.out.println("Daily cap: £" + String.format("%.2f", cap));
                System.out.println("Cap reached: " + capReached);
            }
        }

        private void recomputeAllFares() {
            fareService.resetTotals();
            for (Journey j : journeys) {
                FareService.FareResult res = fareService.calculateAndApplyFare(
                        j.getFromZone(), j.getToZone(), j.getPassengerType(), j.getTimeBand()
                );
                j.setFareBreakdown(res.baseFare(), res.discountedFare(), res.chargedFare());
            }
        }

        private String readValidDate(Scanner sc) {
            while (true) {
                System.out.print("Enter date (dd/MM/yyyy): ");
                String date = sc.nextLine().trim();
                if (DATE_PATTERN.matcher(date).matches()) {
                    return date;
                }
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }

        private int readValidZone(Scanner sc, String prompt) {
            while (true) {
                Integer z = readInt(sc, prompt);
                if (z != null && z >= 1 && z <= 5) return z;
                System.out.println("Invalid zone. Please enter a number between 1 and 5.");
            }
        }

        private TimeBand readValidTimeBand(Scanner sc) {
            while (true) {
                System.out.print("Enter time band (Peak/Off-peak): ");
                String s = sc.nextLine();
                TimeBand tb = TimeBand.parse(s);
                if (tb != null) return tb;
                System.out.println("Invalid time band. Please enter Peak or Off-peak.");
            }
        }

        private PassengerType readValidPassengerType(Scanner sc) {
            while (true) {
                System.out.print("Enter passenger type (Adult, Student, Child, Senior Citizen): ");
                String s = sc.nextLine();
                PassengerType pt = PassengerType.parse(s);
                if (pt != null) return pt;
                System.out.println("Invalid passenger type. Please enter Adult, Student, Child, or Senior Citizen.");
            }
        }

        private Integer readInt(Scanner sc, String prompt) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}