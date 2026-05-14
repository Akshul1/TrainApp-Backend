# TrainApp — CLI Train Ticket Booking System

A command-line Java application that simulates core features of a train ticket reservation system (inspired by IRCTC). Built with Java 21, Gradle, Jackson for JSON-based persistence, BCrypt for password hashing, and Lombok for boilerplate reduction.

> **Learning context:** This project was built to practise layered Java application design, JSON serialisation/deserialisation with Jackson, BCrypt password security, and Gradle-based dependency management — moving beyond JDBC/in-memory storage toward file-backed persistence with real security primitives.

---

## Table of Contents

- [Features](#features)
- [Architecture & Design](#architecture--design)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Schema — JSON Files](#database-schema--json-files)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Setup & Run](#setup--run)
- [Usage — CLI Flow](#usage--cli-flow)
- [Security](#security)
- [Known Issues & Incomplete Features](#known-issues--incomplete-features)
- [What I Learned](#what-i-learned)
- [Future Improvements](#future-improvements)

---

## Features

| Feature | Status | Description |
|---|---|---|
| User sign-up | ✅ Complete | Register with username + BCrypt-hashed password, saved to `user.json` |
| User login | ✅ Complete | Authenticate against stored BCrypt hash |
| Search trains | ✅ Complete | Find trains by source and destination with ordered station validation |
| View seat map | ✅ Complete | Display a 2D grid of available (0) and booked (1) seats |
| Book a seat | ✅ Complete | Select seat by row/column, persisted to `train.json` |
| Fetch bookings | ✅ Complete | Print all tickets for the logged-in user |
| Cancel booking | ⚠️ Stub | Method exists, returns `false` — not yet implemented |

---

## Architecture & Design

The application follows a **3-layer architecture**:

```
App.java  (Entry point / CLI router)
    └── UserBookingService  (Business logic — user operations, seat booking)
         └── TrainService   (Business logic — train search, seat updates)
              └── local_db/ (JSON file persistence layer)
                   ├── user.json
                   └── train.json
```

### Key design decisions

**JSON as a lightweight database**
Rather than JDBC/MySQL, train and user data is stored in JSON files and loaded into memory on startup via Jackson's `ObjectMapper`. Changes are written back to the file immediately after each mutation — a simple write-through persistence pattern.

**BCrypt password hashing**
Passwords are never stored in plain text. On sign-up, `UserServiceUtil.hashPassword()` generates a salted BCrypt hash. On login, `UserServiceUtil.checkPassword()` verifies the plain-text input against the stored hash using `BCrypt.checkpw()`. This is the same algorithm used in production Spring Security applications.

**Snake-case JSON mapping**
`@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)` on `User` and `Train` maps Java camelCase fields (e.g. `trainId`) to JSON snake_case keys (e.g. `train_id`) automatically — no manual `@JsonProperty` annotations needed on every field.

**Station order validation**
`TrainService.validTrain()` checks that both source and destination exist in the train's ordered station list, AND that `sourceIndex < destinationIndex` — correctly rejecting searches where the user's source comes after their destination on that route.

**Seat representation**
Seats are stored as a `List<List<Integer>>` 2D grid. `0` = available, `1` = booked. This makes seat-map display and booking logic straightforward without a separate seats table.

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Build tool | Gradle | 8.13 |
| JSON parsing | Jackson Databind | 2.15.0 |
| Password hashing | jBCrypt | 0.4 |
| Boilerplate reduction | Lombok | 1.18.24 |
| Google utilities | Guava | 33.3.1-jre |
| Testing | JUnit | 4.13.2 |
| IDE | IntelliJ IDEA | — |

---

## Project Structure

```
ticket-booking/
├── app/
│   ├── build.gradle                        ← dependencies and main class config
│   └── src/
│       ├── main/
│       │   └── java/ticket/booking/
│       │       ├── App.java                ← entry point, CLI menu loop
│       │       ├── entities/
│       │       │   ├── User.java           ← user model (name, password, tickets)
│       │       │   ├── Train.java          ← train model (stations, seats, times)
│       │       │   └── Ticket.java         ← ticket model (source, dest, date, train)
│       │       ├── servises/
│       │       │   ├── UserBookingService.java  ← login, signup, booking, cancellation
│       │       │   └── TrainService.java         ← train search, seat updates
│       │       ├── util/
│       │       │   └── UserServiceUtil.java ← BCrypt hash + verify helpers
│       │       └── local_db/
│       │           ├── user.json           ← persistent user + ticket store
│       │           └── train.json          ← persistent train + seat store
│       └── test/
│           └── java/ticket/booking/
│               └── AppTest.java
├── gradle/
│   ├── libs.versions.toml                  ← centralised dependency versions
│   └── wrapper/gradle-wrapper.properties
├── gradlew                                 ← Unix build script
├── gradlew.bat                             ← Windows build script
├── settings.gradle
└── README.md
```

---

## Database Schema — JSON Files

### `user.json`

```json
[
  {
    "user_id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "john_doe",
    "hashed_password": "$2a$10$...",
    "tickets_booked": [
      {
        "ticket_id": "TKT-001",
        "user_id": "550e8400-...",
        "source": "Bangalore",
        "destination": "Delhi",
        "date_of_travel": "2026-07-25T18:30:00Z",
        "train": { ... }
      }
    ]
  }
]
```

### `train.json`

```json
[
  {
    "train_id": "EXP-101",
    "train_no": 12345,
    "seats": [
      [0, 0, 0, 0, 0, 0],
      [0, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0]
    ],
    "stations": {
      "bangalore": "08:00:00",
      "jaipur":    "14:30:00",
      "delhi":     "20:00:00"
    },
    "stations_times": ["Bangalore", "Jaipur", "Delhi"]
  }
]
```

> `seats[row][col] = 0` means available. `1` means booked.
> `stations` holds arrival times. `stations_times` defines the ordered route — both must be present.

---

## Getting Started

### Prerequisites

| Requirement | Version |
|---|---|
| Java JDK | 21 |
| Gradle (via wrapper) | 8.13 — no manual install needed |

### Setup & Run

**1. Clone the repository**
```bash
git clone https://github.com/Akshul1/ticket-booking.git
cd ticket-booking
```

**2. Build the project**
```bash
# Linux / macOS
./gradlew build

# Windows
gradlew.bat build
```

**3. Run the application**
```bash
# Linux / macOS
./gradlew run

# Windows
gradlew.bat run
```

The application reads and writes directly to the JSON files in `app/src/main/java/ticket/booking/local_db/`. No external database setup is required.

---

## Usage — CLI Flow

On launch:

```
Running train Booking System
Choose one option
1). Sign Up
2). Login
3). Fetch Bookings
4). Search trains
5). Book a seat
6). Cancel my Booking
7). Exit the App
```

### Typical booking session

```
# Step 1 — Sign up
Select: 1
Enter username: akshul
Enter password: mypassword123
→ Account created. Password stored as BCrypt hash.

# Step 2 — Login
Select: 2
Enter username: akshul
Enter password: mypassword123
→ Authenticated against BCrypt hash.

# Step 3 — Search trains
Select: 4
Type your source station: Bangalore
Type your destination station: Delhi
→ Lists trains where Bangalore appears before Delhi in the route.

# Step 4 — View and select seat
Select: 5
→ Displays 2D seat grid (0 = free, 1 = booked)
  0 0 0 0 0 0
  0 1 0 0 0 0
  0 0 0 0 0 0
Enter row: 0
Enter column: 0
→ "Booked! Enjoy your journey"

# Step 5 — View bookings
Select: 3
→ Prints all tickets for the logged-in user.
```

---

## Security

| Concern | Implementation |
|---|---|
| Password storage | BCrypt with random salt via `BCrypt.gensalt()` — never stored in plain text |
| Password verification | `BCrypt.checkpw(plain, hash)` — constant-time comparison, resistant to timing attacks |
| Input handling | Basic Scanner input — no injection risk (no SQL used in this project) |

> BCrypt is the same algorithm used by Spring Security's `BCryptPasswordEncoder`. Understanding it here directly maps to how production Spring Boot applications handle authentication.

---

## Known Issues & Incomplete Features

These are open issues identified during development:

| Issue | Location | Fix |
|---|---|---|
| `cancelBooking()` always returns `false` — not implemented | `UserBookingService.java` | Implement: find ticket by ID in `user.ticketsBooked`, remove it, update seat in `train.json` back to `0`, save both files |
| `trainSelectedForBooking` initialised to `new Train()` before search — if user skips case 4 and goes to case 5, it books against an empty train | `App.java` | Add a null/empty check before case 5 proceeds; throw a clear error if no train is selected |
| `fetchBookings()` called in App.java but method in service is `fetchBooking()` (no 's') | `App.java` + `UserBookingService.java` | Rename to match — currently causes a compile error |
| Class name typo: `TrianService` instead of `TrainService` | `TrainService.java` | Rename class and file |
| JSON field mismatch: `train.json` uses `"stations"` for the time map and `"stations_times"` for the ordered list, but `Train.java` maps `stationTimes` → `station_times` and `stations` → `stations` | `Train.java` + `train.json` | Align JSON keys with `@JsonNaming` output or add explicit `@JsonProperty` annotations |
| `user.json` has `"hashed_password"` as a key but `User.java` field is `hashPassword` which serialises to `hash_password` | `User.java` | Add `@JsonProperty("hashed_password")` on the `hashPassword` field |
| `USERS_PATH` and `Trian_Path` use dot notation (`ticket.booking`) instead of slash (`ticket/booking`) in the file path string | Both service files | Change `"ticket.booking/local_db/..."` to `"ticket/booking/local_db/..."` |
| `App.java` references `scanner` (undefined) instead of `sc` in case 4 | `App.java` | Replace `scanner.nextInt()` with `sc.nextInt()` |
| `App.java` has missing imports (`ArrayList`, `UUID`, `List`, `Map`) | `App.java` | Add the missing import statements |
| Duplicate `train_id: "bacs"` entries in `train.json` | `train.json` | Use unique IDs per train |

---

## What I Learned

Working through this project taught:

**Jackson ObjectMapper**
- Deserialising a JSON array into `List<EntityType>` using `TypeReference`
- `@JsonNaming` for automatic snake_case ↔ camelCase conversion
- `@JsonIgnoreProperties(ignoreUnknown = true)` for tolerant deserialisation
- Write-through persistence: calling `objectMapper.writeValue(file, list)` after every mutation

**BCrypt password security**
- Why plain-text password storage is catastrophic
- How BCrypt's random salt prevents rainbow table attacks
- The difference between `hashpw()` (one-way, salted) and `checkpw()` (verification only)

**Gradle build system**
- `build.gradle` dependency declarations
- `libs.versions.toml` for centralised version management
- `./gradlew run` vs `./gradlew build`
- Lombok annotation processing configuration in Gradle

**Layered application design**
- Separating entry point (App), service layer (UserBookingService, TrainService), entity models, and utilities
- Why putting all logic in `main()` becomes unmaintainable
- Package naming conventions in Java

---

## Future Improvements

| Improvement | Why |
|---|---|
| Replace JSON file storage with SQLite or MySQL via JDBC/JPA | Files are not concurrent-safe — two simultaneous writes corrupt data |
| Implement `cancelBooking()` fully | Core feature currently stubbed |
| Add input validation for all Scanner inputs | Entering a String where an int is expected currently throws an uncaught exception |
| Add a proper login session — currently login creates a new `UserBookingService` but doesn't persist the session across menu iterations | Users have to re-authenticate for each action |
| Migrate to Spring Boot REST API | Natural next step — all the service-layer logic maps directly to `@RestController` endpoints |
| Add proper unit tests | `AppTest.java` tests a `getGreeting()` method that doesn't exist — replace with real tests for `TrainService.searchTrains()` and `UserServiceUtil` |
| Fix all known issues listed above before adding new features | Several bugs currently prevent the app from compiling and running cleanly |

---

## License

This project is open source and available under the [MIT License](LICENSE).
