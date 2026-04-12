# IY4113 Milestone 1

| Assessment Details | Please Complete All Details                                      |
| ------------------ | ---------------------------------------------------------------- |
| Group              | 453 B                                                            |
| Module Title       | Applied Software Engineering using Object Orientated Programming |
| Assessment Type    | Java Fundamentals – Practical Assignment Part 1                  |
| Module Tutor Name  | Jonathan Shore                                                   |
| Student ID Number  | P0460817                                                         |
| Date of Submission | 25/01/2026                                                       |
| Word Count         | 1200                                                             |

- [x] I confirm that this assignment is my own work.
- [x] Where I have used AI, I have cited and referenced appropriately.

---

#### Purpose of the Program

In this project, I create a Java console application called CityRide Lite to help users record their daily public transport journeys. The program allows users to enter journey details such as zones, passenger type, and time band. It also calculates the fare and shows summaries of total and average travel costs.

### Core Program Functionality

- Allow the user to add journeys with validated inputs  
- Calculate fares automatically  
- Apply discounts and daily caps  
- Save journeys with unique IDs  
- List and filter journeys  
- Remove journeys  
- Show summaries to the user  

### System Constraints

- The application works using a text menu 
- All journeys are stored in memory only and cleared when the program ends.
- Zones limited to 1–5
- The fare dataset is fixed and must not be modified.
- Program designed to work for single day 
- User input must be validated to prevent incorrect values.

---

## Input Process Output Table

| Feature        | Inputs                                 | Processing                         | Outputs                  |
| -------------- | -------------------------------------- | ---------------------------------- | ------------------------ |
| Start program  | None                                   | setup journey lists and totals     | Main menu                |
| Add journey    | Date, zones, passenger type, time band | check input, calculate fare        | Confirmation message     |
| List journeys  | None                                   | Display all stored journeys        | Journey list             |
| Remove journey | Journey ID                             | Validate ID and remove the journey | Success or error message |
| Daily summary  | None                                   | Calculate totals and averages      | Summary report           |
| Exit program   | Menu option                            | Close the program                  | Exit message             |

---

## Gantt Chart

| Week   | Activity              |
| ------ | --------------------- |
| Week 1 | Analyse requirements  |
| Week 2 | IPO and planning      |
| Week 3 | Design and flowcharts |
| Week 4 | Coding core features  |
| Week 5 | Testing and debugging |
| Week 6 | Final documentation   |

![Screenshot 2026-01-25 at 18.01.59.png](/Users/batuhansert/Desktop/Screenshot%202026-01-25%20at%2018.01.59.png)

---

## Diary Entries

### 22/01/2026 – Diary Entry 1

I started by reading the assignment to understand exactly what the CityRide Lite program needs to do. I found the key rules about zones, passenger types, discounts, and daily caps because these are the most important parts of the system. 

---

### 24/01/2026 – Diary Entry 2

I created an Input, Process, Output table to understand how the program uses journey information. It showed how zones and passenger types produce fares and summaries.

---

### 25/01/2026 – Diary Entry 3

I checked all my planning and documentation to make sure they followed the assignment requirements. I rewrote parts of the purpose and system constraints to make them clearer. I also reviewed each section of Milestone 1 to confirm that everything was complete.
