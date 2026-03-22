| Assessment Details | Please Complete All Details |

| ------------------ | --------------------------- |

| Group              | B |

| Module Title       | Applied Software Engineering using Object-Orientated Programming |

| Assessment Type    | Practical assignment part 2: Java programming with inheritance and file handling |

| Module Tutor Name  | Jonathan Shore |

| Student ID Number  | P0460817 |

| Date of Submission | 22/03/2026 |

| Word Count         | TBC after final PDF export |

| GitHub Link        | https://github.com/BatuhanSert777/IY4113-Milestone |

- [x] *I confirm that this assignment is my own work. Where I have referred to academic sources, I have provided in-text citations and included the sources in the final reference list.*

- [x] *Where I have used AI, I have cited and referenced appropriately.*

---

## Purpose of the Program

The purpose of this program is to extend the original **CityRide Lite** application from Part 1 into a more realistic transport fare companion system. In Part 2, the program must support both **Rider** and **Admin** roles, use **inheritance** in its object-oriented design, and handle **persistent storage** through **JSON** and **CSV** files.

For a rider, the system should allow the user to create or load a profile, store details such as name, passenger type and default payment option, and manage journeys for the current day. The rider must be able to add, edit and delete journeys, import journeys from a CSV file, export current journeys to CSV, and generate an end-of-day summary. The system must calculate fares using the same core fare rules from Part 1, including base fares, discounts and daily caps.

For an administrator, the system must provide a password-protected menu to manage the active fare configuration. This includes viewing, adding, updating and deleting base fares, passenger discounts, daily caps and peak time windows. Any updates made by the admin must be validated before they are saved.

Overall, the program is designed to act as a menu-driven Java console application that gives riders accurate journey cost tracking and summaries, while also allowing administrators to maintain the configuration that controls the fare system.

### Core Program Functionality

- Support two roles: Rider and Admin

- Load configuration data when the program starts

- Use safe default values if the configuration file is missing

- Create, load and save rider profiles using JSON

- Add, edit and delete journeys for the active day

- Import journeys from CSV and export current journeys to CSV

- Calculate fares using zones, time band, passenger type, discounts and daily caps

- Display running totals and end-of-day summaries

- Export summaries as CSV and human-readable text reports

- Allow admins to manage fare rules through a password-protected menu

- Validate all user input before saving data

### System Constraints

- The application is a Java console program and will be menu-driven rather than graphical.

- The program must use object-oriented design and include inheritance.

- The dataset file `CityRideDataset.java` contains rules and constraints that the fare logic must follow.

- The program must use JSON for configuration and rider profile data.

- The program must use CSV for journey imports/exports and summary reports.

- Invalid data must not be saved to file.

- All prompts should clearly show the expected format and example values.

- The solution should follow the NTIC Guide to Good Programming, including clear naming, functional decomposition, private fields where appropriate and readable code structure.

---
