import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Journey {
    private int journeyID;
    private String date;
    private int zone;
    private String passengerType;
    private String timeBand;
    private double fare;

    public Journey(int journeyID, String date, int zone, String passengerType, String timeBand) {
        this.journeyID = journeyID;
        this.date = date;
        this.zone = zone;
        this.passengerType = passengerType;
        this.timeBand = timeBand;
        this.fare = 0;
    }

    public int getJourneyID() { return journeyID; }
    public String getDate() { return date; }
    public int getZone() { return zone; }
    public String getPassengerType() { return passengerType; }
    public String getTimeBand() { return timeBand; }
    public double getFare() { return fare; }

    public void setFare(double fare) { this.fare = fare; }

    public void displayDetails() {
        System.out.println(
                "[" + journeyID + "] Date: " + date +
                        " | Zone: " + zone +
                        " | Passenger: " + passengerType +
                        " | Time: " + timeBand +
                        " | Fare: £" + String.format("%.2f", fare)
        );
    }
}

class FareCalculator {

    public double calculateFare(int zone, String passengerType, String timeBand) {
        double base = getBaseFareByZone(zone);

        if (timeBand.equalsIgnoreCase("Peak")) {
            base = base + 0.50;
        }

        base = applyDiscount(base, passengerType);
        return base;
    }

    private double getBaseFareByZone(int zone) {
        if (zone == 1) return 2.50;
        if (zone == 2) return 3.20;
        if (zone == 3) return 4.00;
        return 3.20;
    }

    public double applyDiscount(double fare, String passengerType) {
        if (passengerType.equalsIgnoreCase("Child")) {
            return fare * 0.50;
        } else if (passengerType.equalsIgnoreCase("Student")) {
            return fare * 0.80;
        } else if (passengerType.equalsIgnoreCase("Senior")) {
            return fare * 0.70;
        }
        return fare;
    }

    public double applyDailyCap(double totalCost, int zone) {
        double cap = getCapByZone(zone);
        if (totalCost > cap) return cap;
        return totalCost;
    }

    private double getCapByZone(int zone) {
        if (zone == 1) return 7.00;
        if (zone == 2) return 8.50;
        if (zone == 3) return 10.00;
        return 8.50;
    }
}

class DailySummary {
    private String date;
    private double totalCost;
    private double averageCost;
    private int totalJourneys;

    public DailySummary(String date, double totalCost, double averageCost, int totalJourneys) {
        this.date = date;
        this.totalCost = totalCost;
        this.averageCost = averageCost;
        this.totalJourneys = totalJourneys;
    }

    public void displaySummary() {
        System.out.println("\nDaily Summary for " + date);
        System.out.println("Total journeys: " + totalJourneys);
        System.out.println("Total cost: £" + String.format("%.2f", totalCost));
        System.out.println("Average cost: £" + String.format("%.2f", averageCost));
    }
}

class JourneyManager {
    private List<Journey> journeys = new ArrayList<>();
    private FareCalculator fareCalculator = new FareCalculator();
    private int nextJourneyID = 1;

    public void addJourney(String date, int zone, String passengerType, String timeBand) {
        Journey j = new Journey(nextJourneyID, date, zone, passengerType, timeBand);

        double fare = fareCalculator.calculateFare(zone, passengerType, timeBand);
        j.setFare(fare);

        journeys.add(j);
        System.out.println("Journey added successfully. Journey ID: " + nextJourneyID);
        nextJourneyID++;
    }

    public boolean removeJourney(int journeyID) {
        for (int i = 0; i < journeys.size(); i++) {
            if (journeys.get(i).getJourneyID() == journeyID) {
                journeys.remove(i);
                System.out.println("Journey removed successfully.");
                return true;
            }
        }
        System.out.println("Journey ID not found.");
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

    public void getDailySummary(String date) {
        double total = 0;
        int count = 0;

        int maxZoneForThatDay = 1;

        for (Journey j : journeys) {
            if (j.getDate().equalsIgnoreCase(date)) {
                total += j.getFare();
                count++;
                if (j.getZone() > maxZoneForThatDay) {
                    maxZoneForThatDay = j.getZone();
                }
            }
        }

        if (count == 0) {
            System.out.println("No journeys found for that date.");
            return;
        }

        total = fareCalculator.applyDailyCap(total, maxZoneForThatDay);

        double avg = total / count;
        DailySummary ds = new DailySummary(date, total, avg, count);
        ds.displaySummary();
    }
}

public class CityRideLiteApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        JourneyManager manager = new JourneyManager();
        int choice;

        do {
            System.out.println("\nCityRide Lite");
            System.out.println("1. Add Journey");
            System.out.println("2. Remove Journey");
            System.out.println("3. Daily Summary");
            System.out.println("4. List Journeys");
            System.out.println("5. Exit");
            System.out.println("Choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter date (e.g. 01/02/2026): ");
                    String date = sc.nextLine();

                    System.out.print("Enter zone (1-3): ");
                    int zone = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter passenger type (Adult, Child, Student, Senior): ");
                    String passengerType = sc.nextLine();

                    System.out.print("Enter time band (Peak/OffPeak): ");
                    String timeBand = sc.nextLine();

                    manager.addJourney(date, zone, passengerType, timeBand);
                    break;

                case 2:
                    System.out.print("Enter Journey ID to remove: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    manager.removeJourney(id);
                    break;

                case 3:
                    System.out.print("Enter date for summary (e.g. 01/02/2026): ");
                    String summaryDate = sc.nextLine();
                    manager.getDailySummary(summaryDate);
                    break;

                case 4:
                    manager.listJourneys();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 5);

        sc.close();
    }
}
