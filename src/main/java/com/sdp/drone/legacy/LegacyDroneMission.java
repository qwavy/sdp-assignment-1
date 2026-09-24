package com.sdp.drone.legacy;

import com.sdp.drone.model.LocationCoordinates;
import com.sdp.drone.model.MissionType;

/**
 * Legacy implementation of DroneMission without the Builder pattern.
 * <p>
 * Demonstrates three core architectural problems:
 * 1. Unreadable telescopic constructor with parameter ambiguity (boolean flag soup).
 * 2. Inability to enforce strict cross-field validation cleanly during object creation.
 * 3. High maintenance burden when adding or modifying optional properties.
 */
public class LegacyDroneMission {
    private String missionId;
    private String droneId;
    private MissionType missionType;
    private LocationCoordinates targetArea;

    private int batteryCapacityMah;
    private int maxAltitudeMeters;
    private boolean gpsEnabled;
    private boolean nightVisionEnabled;
    private boolean encryptedTelemetry;
    private String payloadCameraType;

    /**
     * Telescopic 10-parameter constructor representing the anti-pattern.
     */
    public LegacyDroneMission(String missionId, String droneId, MissionType missionType,
                              LocationCoordinates targetArea, int batteryCapacityMah,
                              int maxAltitudeMeters, boolean gpsEnabled, boolean nightVisionEnabled,
                              boolean encryptedTelemetry, String payloadCameraType) {
        this.missionId = missionId;
        this.droneId = droneId;
        this.missionType = missionType;
        this.targetArea = targetArea;
        this.batteryCapacityMah = batteryCapacityMah;
        this.maxAltitudeMeters = maxAltitudeMeters;
        this.gpsEnabled = gpsEnabled;
        this.nightVisionEnabled = nightVisionEnabled;
        this.encryptedTelemetry = encryptedTelemetry;
        this.payloadCameraType = payloadCameraType;
    }

    // Getters and setters (mutable object, vulnerable to unexpected modifications)

    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }

    public String getDroneId() { return droneId; }
    public void setDroneId(String droneId) { this.droneId = droneId; }

    public MissionType getMissionType() { return missionType; }
    public void setMissionType(MissionType missionType) { this.missionType = missionType; }

    public LocationCoordinates getTargetArea() { return targetArea; }
    public void setTargetArea(LocationCoordinates targetArea) { this.targetArea = targetArea; }

    public int getBatteryCapacityMah() { return batteryCapacityMah; }
    public void setBatteryCapacityMah(int batteryCapacityMah) { this.batteryCapacityMah = batteryCapacityMah; }

    public int getMaxAltitudeMeters() { return maxAltitudeMeters; }
    public void setMaxAltitudeMeters(int maxAltitudeMeters) { this.maxAltitudeMeters = maxAltitudeMeters; }

    public boolean isGpsEnabled() { return gpsEnabled; }
    public void setGpsEnabled(boolean gpsEnabled) { this.gpsEnabled = gpsEnabled; }

    public boolean isNightVisionEnabled() { return nightVisionEnabled; }
    public void setNightVisionEnabled(boolean nightVisionEnabled) { this.nightVisionEnabled = nightVisionEnabled; }

    public boolean isEncryptedTelemetry() { return encryptedTelemetry; }
    public void setEncryptedTelemetry(boolean encryptedTelemetry) { this.encryptedTelemetry = encryptedTelemetry; }

    public String getPayloadCameraType() { return payloadCameraType; }
    public void setPayloadCameraType(String payloadCameraType) { this.payloadCameraType = payloadCameraType; }

    @Override
    public String toString() {
        return "LegacyDroneMission{" +
                "missionId='" + missionId + '\'' +
                ", droneId='" + droneId + '\'' +
                ", missionType=" + missionType +
                ", targetArea=" + targetArea +
                ", batteryCapacityMah=" + batteryCapacityMah +
                ", maxAltitudeMeters=" + maxAltitudeMeters +
                ", gpsEnabled=" + gpsEnabled +
                ", nightVisionEnabled=" + nightVisionEnabled +
                ", encryptedTelemetry=" + encryptedTelemetry +
                ", payloadCameraType='" + payloadCameraType + '\'' +
                '}';
    }
}
