# Drone Mission Configuration System (Builder Pattern)

Assignment 1 for the Software Design Patterns course.  
Instructor: PhD Makpal Zhartybayeva.

## Overview
This repository contains a Java implementation of the Builder Design Pattern for a Drone Mission Configuration System (`DroneMission`).

The project demonstrates how to refactor a constructor-based anti-pattern into a clean, fluent Builder pattern with immutability, step-by-step construction, single-field validation, and cross-field domain rules.

## Project Structure
```text
.
├── src/
│   ├── main/java/com/sdp/drone/
│   │   ├── Main.java                        # Demonstration of legacy vs Builder pattern & presets
│   │   ├── director/
│   │   │   └── DroneMissionDirector.java   # Presets (Recon, Safe Surveillance, High Altitude)
│   │   ├── legacy/
│   │   │   └── LegacyDroneMission.java      # Part A: Constructor anti-pattern code
│   │   └── model/
│   │       ├── DroneMission.java            # Product & static inner Builder
│   │       ├── LocationCoordinates.java    # Value Object for target area
│   │       └── MissionType.java            # Mission type enumeration
│   └── test/java/com/sdp/drone/
│       └── DroneMissionTest.java            # Automated test suite (12 test cases)
├── docs/
│   └── UML.md                              # Mermaid & PlantUML diagrams and traceability table
├── pom.xml                                 # Maven configuration (JDK 17+)
├── report.md                               # Complete assignment report
├── run.sh                                  # Shell script to compile and run main & tests
└── README.md                               # Project readme
```

## Domain Rules & Individual Constraint
- **Product properties:** 4 required fields (`missionId`, `droneId`, `missionType`, `targetArea`) and 6 optional fields (`batteryCapacityMah`, `maxAltitudeMeters`, `gpsEnabled`, `nightVisionEnabled`, `encryptedTelemetry`, `payloadCameraType`).
- **Individual Constraint:** Missions of type `SURVEILLANCE` or `SEARCH_AND_RESCUE` require `gpsEnabled == true` and `batteryCapacityMah >= 5000`.
- **Optics Rule:** Night vision enabled missions cannot exceed 5000 meters in altitude.

## Building and Running

### Prerequisites
- JDK 17 or higher installed on your machine.

### Quick Start (Shell Script)
Run the automated script to compile and execute both the main application and test suite:
```bash
./run.sh
```

### Manual Compilation and Execution
```bash
mkdir -p bin
javac -d bin src/main/java/com/sdp/drone/model/*.java \
             src/main/java/com/sdp/drone/legacy/*.java \
             src/main/java/com/sdp/drone/director/*.java \
             src/main/java/com/sdp/drone/*.java \
             src/test/java/com/sdp/drone/*.java

# Run main demo
java -cp bin com.sdp.drone.Main

# Run automated tests
java -cp bin com.sdp.drone.DroneMissionTest
```

### Running with Maven (Optional)
If you have Maven installed:
```bash
mvn clean compile
```
