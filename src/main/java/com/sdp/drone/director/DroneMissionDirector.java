package com.sdp.drone.director;

import com.sdp.drone.model.DroneMission;
import com.sdp.drone.model.LocationCoordinates;
import com.sdp.drone.model.MissionType;

/**
 * Director class responsible for organizing preset mission configurations.
 * Enforces standardized construction order for common operational scenarios.
 */
public class DroneMissionDirector {

    /**
     * Preset 1: Basic Reconnaissance Configuration.
     * Lightweight mission for short-range initial area scouting.
     */
    public DroneMission buildReconBasic(String missionId, String droneId, LocationCoordinates targetArea) {
        return new DroneMission.Builder(missionId, droneId, MissionType.RECON, targetArea)
                .withBatteryCapacity(4000)
                .withMaxAltitude(2500)
                .enableGps()
                .disableNightVision()
                .enableEncryptedTelemetry()
                .withPayloadCameraType("Standard-HD-Zoom")
                .build();
    }

    /**
     * Preset 2: Safe Surveillance Configuration (SAFE_SURVEILLANCE).
     * High-reliability configuration satisfying all individual constraints.
     */
    public DroneMission buildSurveillanceSafe(String missionId, String droneId, LocationCoordinates targetArea) {
        DroneMission mission = new DroneMission.Builder(missionId, droneId, MissionType.SURVEILLANCE, targetArea)
                .withBatteryCapacity(8500)
                .withMaxAltitude(4500)
                .enableGps()
                .enableNightVision()
                .enableEncryptedTelemetry()
                .withPayloadCameraType("Thermal-4K-Zoom")
                .build();

        System.out.println("Preset SAFE_SURVEILLANCE successfully constructed: " + mission);
        return mission;
    }

    /**
     * Preset 3: High Altitude Performance Configuration.
     * Long-range stratospheric observation at 9,500m altitude.
     */
    public DroneMission buildHighAltitudePerformance(String missionId, String droneId, LocationCoordinates targetArea) {
        return new DroneMission.Builder(missionId, droneId, MissionType.HIGH_ALTITUDE, targetArea)
                .withBatteryCapacity(15000)
                .withMaxAltitude(9500)
                .enableGps()
                .disableNightVision()
                .enableEncryptedTelemetry()
                .withPayloadCameraType("Multispectral-HD-Scanner")
                .build();
    }
}
