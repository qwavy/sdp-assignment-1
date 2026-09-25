# Assignment 1 Report: Builder Pattern for Drone Mission Configuration System

**Course:** Software Design Patterns  
**Topic:** Pattern Builder — Design Under Changing Requirements  
**Instructor:** Associate Professor, PhD Makpal Zhartybayeva  
**Domain:** Drone Mission Configuration System (`DroneMission`)  

---

## 1. Domain & Individual Variant Description

The **Drone Mission Configuration System** is designed to assemble complex mission parameters for unmanned aerial vehicles (UAVs). A mission object (`DroneMission`) requires configuration across 10 properties to ensure flight safety, optics compatibility, battery endurance, and telemetry security.

### Individual Variant Specifications:
* **Product:** `DroneMission`
* **4 Mandatory Properties:**
  1. `missionId` (`String`)
  2. `droneId` (`String`)
  3. `missionType` (`MissionType` Enum: `RECON`, `SURVEILLANCE`, `SEARCH_AND_RESCUE`, `HIGH_ALTITUDE`)
  4. `targetArea` (`LocationCoordinates` Value Object: `latitude`, `longitude`, `radiusKm`)
* **6 Optional Properties:**
  1. `batteryCapacityMah` (`int`, default: 5000 mAh)
  2. `maxAltitudeMeters` (`int`, default: 1000 m)
  3. `gpsEnabled` (`boolean`, default: true)
  4. `nightVisionEnabled` (`boolean`, default: false)
  5. `encryptedTelemetry` (`boolean`, default: true)
  6. `payloadCameraType` (`String`, default: "Standard-HD")

* **Individual Constraint:**
  Missions of type `SURVEILLANCE` or `SEARCH_AND_RESCUE` **must** have `gpsEnabled == true` AND `batteryCapacityMah >= 5000`.

---

## 2. Part A — Initial Constructor-Based Solution & Design Problems

### Initial Code Implementation (`LegacyDroneMission.java`)
In the legacy implementation, object instantiation was attempted via a monolithic 10-parameter constructor:

```java
public LegacyDroneMission(String missionId, String droneId, MissionType missionType,
                          LocationCoordinates targetArea, int batteryCapacityMah,
                          int maxAltitudeMeters, boolean gpsEnabled, boolean nightVisionEnabled,
                          boolean encryptedTelemetry, String payloadCameraType) { ... }
```

Client invocation code:
```java
LegacyDroneMission legacyMission = new LegacyDroneMission(
    "M-101", "DRONE-ALPHA", MissionType.SURVEILLANCE, astanaArea,
    6000, 3000, true, true, false, "Standard-HD"
);
```

### Three Identified Architectural Design Problems:

1. **Parameter Ambiguity and Boolean Flag Soup:**
   Passing multiple consecutive boolean values (`true, true, false`) makes client code completely unreadable. A developer cannot determine without checking the constructor declaration which flag controls GPS, night vision, or encryption. Swapping two boolean arguments leads to critical runtime operational bugs without any compile-time error.

2. **Inability to Perform Clean Cross-Field Validation During Construction:**
   Constructors should initialize fields, not contain complex multi-branch business logic. When validation rules depend on multiple parameters (e.g., `SURVEILLANCE` requiring `gpsEnabled == true` and `batteryCapacityMah >= 5000`), performing validation inside constructors leads to bloated constructors or partially constructed objects if an exception is thrown after some fields are set.

3. **High Maintenance Cost & Combinatorial Explosion of Telescoping Constructors:**
   Adding a new optional parameter (e.g., `thermalSensorEnabled`) requires either updating every single constructor invocation across the codebase or creating another overloaded constructor. Adding $N$ optional parameters leads to $2^N$ potential constructor overloads.

---

## 3. Part B — Refactoring to Builder Pattern

The project was refactored by creating a static inner `Builder` class inside `DroneMission`.

### Key Structural Highlights:
* **Immutability:** All fields in `DroneMission` are `private final`, and no setter methods are provided.
* **Fluent API:** Builder methods return `this`, allowing method chaining with domain-oriented names.
* **Separation of Concerns:** Step-by-step state accumulation occurs inside `Builder`, while `DroneMission` represents the immutable final product.

```java
DroneMission mission = new DroneMission.Builder("M-300", "DRONE-EAGLE", MissionType.SEARCH_AND_RESCUE, targetArea)
        .withBatteryCapacity(10000)
        .withMaxAltitude(4000)
        .enableGps()
        .enableNightVision()
        .enableEncryptedTelemetry()
        .withPayloadCameraType("FLIR-Thermal-X")
        .build();
```

---

## 4. Part C — Validation Challenge

All validations are strictly executed inside the `build()` method prior to product instantiation.

### 3 Single-Field Validation Rules:
1. **Mandatory Identifier Check:** `missionId` and `droneId` must not be null or blank strings. Throws `IllegalArgumentException`.
2. **Battery Range Rule:** `batteryCapacityMah` must be $> 0$ and $\le 30000$ mAh. Throws `IllegalArgumentException`.
3. **Altitude Range Rule:** `maxAltitudeMeters` must be $> 0$ and $\le 10000$ meters. Throws `IllegalArgumentException`.

### 2 Cross-Field Validation Rules:
1. **Individual Constraint Rule:** If `missionType == SURVEILLANCE` or `missionType == SEARCH_AND_RESCUE`, then `gpsEnabled` **must** be `true` AND `batteryCapacityMah >= 5000`. Throws `IllegalStateException`.
2. **Optics Altitude Limitation:** If `nightVisionEnabled == true`, optical hardware limitations restrict `maxAltitudeMeters` to $\le 5000$ meters. Throws `IllegalStateException`.

---

## 5. Part D — Director & Preset Configurations

The `DroneMissionDirector` encapsulates construction sequences for common operational missions:

1. **`buildReconBasic()` (Basic Reconnaissance):**
   * Mission Type: `RECON`
   * Battery: 4000 mAh, Max Altitude: 2500 m, Camera: "Standard-HD-Zoom"
2. **`buildSurveillanceSafe()` (Safe Surveillance Patrol):**
   * Mission Type: `SURVEILLANCE`
   * Battery: 8500 mAh, Max Altitude: 4500 m, Night Vision: Enabled, Camera: "Thermal-4K-Zoom"
3. **`buildHighAltitudePerformance()` (Stratospheric Reconnaissance):**
   * Mission Type: `HIGH_ALTITUDE`
   * Battery: 15000 mAh, Max Altitude: 9500 m, Camera: "Multispectral-HD-Scanner"

---

## 6. Part E — Clean Code (Chapter 3) Refactoring: Before $\to$ After

### Example 1: Small Functions & Single Level of Abstraction
* **BEFORE:** A single 40-line monolithic `build()` method containing validation checks, range checks, string trimming, and cross-field logic mixed together.
* **AFTER:** Extracted small, dedicated helper methods (`validateSingleFieldRules()`, `validateCrossFieldRules()`, `validateIndividualSurveillanceConstraint()`, `validateOpticsNightVisionConstraint()`).
* **Principle Applied:** Small Functions & Do One Thing (Single Responsibility).
* **Rationale:** Each function operates at a single level of abstraction, making debugging and requirement updates effortless.

### Example 2: Avoiding Flag Arguments
* **BEFORE:** `builder.setGpsEnabled(true).setNightVision(false).setTelemetryEncrypted(true)`
* **AFTER:** `builder.enableGps().disableNightVision().enableEncryptedTelemetry()`
* **Principle Applied:** Avoid Flag Arguments in APIs.
* **Rationale:** Explicit method names eliminate boolean parameters, making code self-documenting and readable without IDE parameter hints.

### Example 3: Command-Query Separation (CQS) & Domain Naming
* **BEFORE:** `builder.setBattery(6000).setAlt(4000)`
* **AFTER:** `builder.withBatteryCapacity(6000).withMaxAltitude(4000)`
* **Principle Applied:** Descriptive Domain Naming & Intent-Revealing Interfaces.
* **Rationale:** Using domain terms (`withBatteryCapacity`, `withMaxAltitude`) rather than generic JavaBean mutators (`set...`) aligns code with domain language and intent.

---

## 7. Part F — Non-Trivial Design Decisions

### Decision 1: Validation Placement (`build()` vs Setter methods vs Product constructor)
* **Choice:** Enforce validation strictly inside `build()`.
* **Alternative:** Validating inside each Builder setter method (e.g., `withBatteryCapacity`).
* **Reasoning:** Cross-field validation relies on multiple parameters. Validating in setters fails when values are set out of order. Performing validation once in `build()` guarantees that the Builder accumulates state flexibly, and validation checks the complete configuration state right before instantiation.

### Decision 2: Product Immutability
* **Choice:** Make `DroneMission` completely immutable (`private final` fields, no setters).
* **Alternative:** Allow getters and setters on `DroneMission`.
* **Reasoning:** Drone missions operate in concurrent telemetry environments. Immutability guarantees thread safety, prevents accidental mutation during flight execution, and makes objects predictable.

### Decision 3: Including a Director Class
* **Choice:** Use `DroneMissionDirector` for standard preset missions.
* **Alternative:** Forcing client code to construct preset configurations manually.
* **Reasoning:** The Director encapsulates standard construction sequences, hiding repetitive setup from clients and ensuring presets adhere to complex safety regulations.

---

## 8. Part G — UML Diagram & Role Traceability

See full UML diagrams in `docs/UML.md`.

### Summary Role Mapping:
* **Product:** `DroneMission`
* **Builder:** `DroneMission.Builder` (static inner class)
* **Director:** `DroneMissionDirector`
* **Value Object:** `LocationCoordinates`
* **Client:** `Main`, `DroneMissionTest`

---

## 9. Part H — Automated Testing Results

The automated test suite (`DroneMissionTest.java`) executes 12 test cases:

1. `testValidBasicReconMission` — PASSED
2. `testValidSafeSurveillancePreset` — PASSED
3. `testValidHighAltitudeMission` — PASSED
4. `testInvalidEmptyMissionId` — PASSED
5. `testInvalidBatteryCapacity` — PASSED
6. `testInvalidMaxAltitude` — PASSED
7. `testBoundaryAltitudeNightVisionExactLimit` — PASSED
8. `testBoundarySurveillanceBatteryExactLimit` — PASSED
9. `testIndividualConstraintSurveillanceWithoutGps` — PASSED
10. `testIndividualConstraintSurveillanceWeakBattery` — PASSED
11. `testCrossFieldNightVisionHighAltitude` — PASSED
12. `testBuilderReuseProductIndependence` — PASSED

---

## 10. Verification Output

```text
=== EXECUTING MAIN APPLICATION ===
--- Part A: Legacy Constructor (Anti-Pattern) ---
Legacy Mission Created: LegacyDroneMission{missionId='M-101', droneId='DRONE-ALPHA', missionType=SURVEILLANCE, ...}

--- Part B & D: Builder Pattern & Director Presets ---
1. Building RECON_BASIC Preset:
Recon Mission: DroneMission{missionId='M-201', droneId='DRONE-BETA', ...}

2. Building SAFE_SURVEILLANCE Preset:
Preset SAFE_SURVEILLANCE successfully constructed: DroneMission{missionId='M-202', droneId='DRONE-GAMMA', missionType=SURVEILLANCE, targetArea=[Lat: 51,1694, Lon: 71,4491, Radius: 15,0 km], batteryCapacityMah=8500, maxAltitudeMeters=4500, gpsEnabled=true, nightVisionEnabled=true, encryptedTelemetry=true, payloadCameraType='Thermal-4K-Zoom'}

3. Building HIGH_ALTITUDE Preset:
High Altitude Mission: DroneMission{missionId='M-203', droneId='DRONE-DELTA', ...}

=== EXECUTING AUTOMATED TEST SUITE ===
[TEST] 1. Valid Basic Recon Mission ... PASSED
[TEST] 2. Valid Safe Surveillance Preset ... PASSED
...
TEST RESULTS: Passed: 12 | Failed: 0 | Total: 12
```
