# UML Architecture & Traceability Matrix

## 1. Class Diagram (Mermaid)

```mermaid
classDiagram
    class DroneMission {
        -String missionId
        -String droneId
        -MissionType missionType
        -LocationCoordinates targetArea
        -int batteryCapacityMah
        -int maxAltitudeMeters
        -boolean gpsEnabled
        -boolean nightVisionEnabled
        -boolean encryptedTelemetry
        -String payloadCameraType
        -DroneMission(Builder builder)
        +getMissionId() String
        +getDroneId() String
        +getMissionType() MissionType
        +getTargetArea() LocationCoordinates
        +getBatteryCapacityMah() int
        +getMaxAltitudeMeters() int
        +isGpsEnabled() boolean
        +isNightVisionEnabled() boolean
        +isEncryptedTelemetry() boolean
        +getPayloadCameraType() String
    }

    class Builder {
        -String missionId
        -String droneId
        -MissionType missionType
        -LocationCoordinates targetArea
        -int batteryCapacityMah
        -int maxAltitudeMeters
        -boolean gpsEnabled
        -boolean nightVisionEnabled
        -boolean encryptedTelemetry
        -String payloadCameraType
        +Builder(String, String, MissionType, LocationCoordinates)
        +withBatteryCapacity(int mah) Builder
        +withMaxAltitude(int meters) Builder
        +enableGps() Builder
        +disableGps() Builder
        +enableNightVision() Builder
        +disableNightVision() Builder
        +enableEncryptedTelemetry() Builder
        +disableEncryptedTelemetry() Builder
        +withPayloadCameraType(String cameraType) Builder
        +build() DroneMission
        -validateSingleFieldRules() void
        -validateCrossFieldRules() void
    }

    class DroneMissionDirector {
        +buildReconBasic(String, String, LocationCoordinates) DroneMission
        +buildSurveillanceSafe(String, String, LocationCoordinates) DroneMission
        +buildHighAltitudePerformance(String, String, LocationCoordinates) DroneMission
    }

    class LocationCoordinates {
        -double latitude
        -double longitude
        -double radiusKm
        +LocationCoordinates(double, double, double)
        +getLatitude() double
        +getLongitude() double
        +getRadiusKm() double
    }

    class MissionType {
        <<enumeration>>
        RECON
        SURVEILLANCE
        SEARCH_AND_RESCUE
        HIGH_ALTITUDE
    }

    class Client {
        <<Main / DroneMissionTest>>
    }

    DroneMission *-- Builder : Static Nested Class
    DroneMission --> LocationCoordinates : contains Value Object
    DroneMission --> MissionType : uses
    DroneMissionDirector --> Builder : orchestrates
    DroneMissionDirector ..> DroneMission : produces
    Client ..> DroneMissionDirector : invokes
    Client ..> Builder : invokes directly
```

---

## 2. PlantUML Representation

```plantuml
@startuml
package "com.sdp.drone.model" {
    enum MissionType {
        RECON
        SURVEILLANCE
        SEARCH_AND_RESCUE
        HIGH_ALTITUDE
    }

    class LocationCoordinates <<ValueObject>> {
        - double latitude
        - double longitude
        - double radiusKm
        + LocationCoordinates(latitude: double, longitude: double, radiusKm: double)
        + getLatitude(): double
        + getLongitude(): double
        + getRadiusKm(): double
    }

    class DroneMission <<Product>> {
        - String missionId
        - String droneId
        - MissionType missionType
        - LocationCoordinates targetArea
        - int batteryCapacityMah
        - int maxAltitudeMeters
        - boolean gpsEnabled
        - boolean nightVisionEnabled
        - boolean encryptedTelemetry
        - String payloadCameraType
        - DroneMission(builder: Builder)
        + getMissionId(): String
        + getDroneId(): String
        + getMissionType(): MissionType
        + getTargetArea(): LocationCoordinates
        + getBatteryCapacityMah(): int
        + getMaxAltitudeMeters(): int
        + isGpsEnabled(): boolean
        + isNightVisionEnabled(): boolean
        + isEncryptedTelemetry(): boolean
        + getPayloadCameraType(): String
    }

    class "DroneMission.Builder" as Builder <<Builder>> {
        - String missionId
        - String droneId
        - MissionType missionType
        - LocationCoordinates targetArea
        - int batteryCapacityMah
        - int maxAltitudeMeters
        - boolean gpsEnabled
        - boolean nightVisionEnabled
        - boolean encryptedTelemetry
        - String payloadCameraType
        + Builder(missionId: String, droneId: String, missionType: MissionType, targetArea: LocationCoordinates)
        + withBatteryCapacity(mah: int): Builder
        + withMaxAltitude(meters: int): Builder
        + enableGps(): Builder
        + disableGps(): Builder
        + enableNightVision(): Builder
        + disableNightVision(): Builder
        + enableEncryptedTelemetry(): Builder
        + disableEncryptedTelemetry(): Builder
        + withPayloadCameraType(cameraType: String): Builder
        + build(): DroneMission
    }
}

package "com.sdp.drone.director" {
    class DroneMissionDirector <<Director>> {
        + buildReconBasic(missionId: String, droneId: String, targetArea: LocationCoordinates): DroneMission
        + buildSurveillanceSafe(missionId: String, droneId: String, targetArea: LocationCoordinates): DroneMission
        + buildHighAltitudePerformance(missionId: String, droneId: String, targetArea: LocationCoordinates): DroneMission
    }
}

class Main <<Client>>
class DroneMissionTest <<Client>>

DroneMission +-- Builder
DroneMission "1" o-- "1" LocationCoordinates
DroneMission "1" o-- "1" MissionType
DroneMissionDirector ..> Builder : uses
DroneMissionDirector ..> DroneMission : creates
Main ..> DroneMissionDirector
DroneMissionTest ..> Builder
@enduml
```

---

## 3. Pattern Role Traceability Table

| Pattern Role | Implementation Class | Responsibilities & Design Notes |
| :--- | :--- | :--- |
| **Product** | `DroneMission` | Complex target object. Immutable (`private final` fields, no setters). Created strictly via Builder constructor. |
| **Builder** | `DroneMission.Builder` | Static nested class. Provides fluent API, stores step-by-step state, defaults, and enforces single-field and cross-field validation rules in `build()`. |
| **Director** | `DroneMissionDirector` | Defines predefined mission configurations (`RECON_BASIC`, `SAFE_SURVEILLANCE`, `HIGH_ALTITUDE`). Enforces construction sequence. |
| **Value Object** | `LocationCoordinates` | Immutable nested domain object encapsulated within `DroneMission`. Validates coordinate boundaries upon creation. |
| **Client** | `Main` / `DroneMissionTest` | Instantiates products either directly using `DroneMission.Builder` or via `DroneMissionDirector` presets. |
