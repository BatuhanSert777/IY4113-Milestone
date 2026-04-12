# CityRide Lite — Part 2

**Module:** IY4113 Applied Software Engineering using Object Orientated Programming
**Student ID:** P0460817

---

## What this program does

CityRide Lite is a console-based fare management system for a fictional urban transport network.

The program supports two roles:

**Rider**
- Create or load a saved profile (name, passenger type, default payment)
- Add, edit, and delete journeys for the day
- Passenger type is taken automatically from the rider's profile
- Time band (Peak or Off-Peak) is determined automatically from the journey time and configured peak windows
- Daily cap is applied across all journeys — fares are recalculated after any edit or deletion
- View an end-of-day summary (total charged, savings, peak/off-peak split, cap status)
- Export summary as a text file or CSV report
- Import and export journeys as CSV
- Session journeys are saved automatically and can be resumed on next login

**Admin**
- Password-protected access
- View the current fare configuration (base fares, discounts, daily caps, peak windows)
- Add, update, and delete base fares, passenger discounts, and daily caps
- Update morning and evening peak time windows
- Save configuration changes to file

---

## How to run

1. Open the project in **IntelliJ IDEA**
2. Add the Gson library (see below)
3. Set `Main` as the run configuration entry point
4. Click Run

The program uses the console for all input and output.

---

## Gson dependency

This project uses **Gson 2.10.1** for JSON file handling (profiles, journeys, configuration).

The JAR file is included in the `lib/` folder: `lib/gson-2.10.1.jar`

**To add it in IntelliJ:**
1. File → Project Structure → Libraries
2. Click `+` → Java
3. Navigate to `lib/gson-2.10.1.jar` and select it
4. Click OK and Apply

---

## Project structure

```
src/          Java source files (21 classes)
lib/          Gson library JAR
data/         Created automatically at runtime (profiles, journeys, config)
reports/      Created automatically at runtime (exported reports)
```

---

## Entry point

```
Main.java → CityRideApp.run()
```
