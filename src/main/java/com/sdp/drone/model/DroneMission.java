package com.sdp.drone.model;

import java.util.Objects;

/**
 * Production-ready immutable Product class representing a Drone Mission Configuration.
 * Uses the Builder Pattern to ensure thread-safety, immutability, and validity.
 */
public final class DroneMission {
    // 4 Mandatory Properties
    private final String missionId;
    private final String droneId;
    private final MissionType missionType;
    private final LocationCoordinates targetArea;

    // 6 Optional Properties
    private final int batteryCapacityMah;
    private final int maxAltitudeMeters;
    private final boolean gpsEnabled;
    private final boolean nightVisionEnabled;
    private final boolean encryptedTelemetry;
    private final String payloadCameraType;

    /**
     * Private constructor called strictly by the Builder.
     */
    private DroneMission(Builder builder) {
        this.missionId = builder.missionId;
        this.droneId = builder.droneId;
        this.missionType = builder.missionType;
        this.targetArea = builder.targetArea;
        this.batteryCapacityMah = builder.batteryCapacityMah;
        this.maxAltitudeMeters = builder.maxAltitudeMeters;
        this.gpsEnabled = builder.gpsEnabled;
        this.nightVisionEnabled = builder.nightVisionEnabled;
        this.encryptedTelemetry = builder.encryptedTelemetry;
        this.payloadCameraType = builder.payloadCameraType;
    }

    // Getters only (Immutability guarantee)

    public String getMissionId() { return missionId; }
    public String getDroneId() { return droneId; }
    public MissionType getMissionType() { return missionType; }
    public LocationCoordinates getTargetArea() { return targetArea; }

    public int getBatteryCapacityMah() { return batteryCapacityMah; }
    public int getMaxAltitudeMeters() { return maxAltitudeMeters; }
    public boolean isGpsEnabled() { return gpsEnabled; }
    public boolean isNightVisionEnabled() { return nightVisionEnabled; }
    public boolean isEncryptedTelemetry() { return encryptedTelemetry; }
    public String getPayloadCameraType() { return payloadCameraType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroneMission mission = (DroneMission) o;
        return batteryCapacityMah == mission.batteryCapacityMah &&
                maxAltitudeMeters == mission.maxAltitudeMeters &&
                gpsEnabled == mission.gpsEnabled &&
                nightVisionEnabled == mission.nightVisionEnabled &&
                encryptedTelemetry == mission.encryptedTelemetry &&
                Objects.equals(missionId, mission.missionId) &&
                Objects.equals(droneId, mission.droneId) &&
                missionType == mission.missionType &&
                Objects.equals(targetArea, mission.targetArea) &&
                Objects.equals(payloadCameraType, mission.payloadCameraType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(missionId, droneId, missionType, targetArea, batteryCapacityMah,
                maxAltitudeMeters, gpsEnabled, nightVisionEnabled, encryptedTelemetry, payloadCameraType);
    }

    @Override
    public String toString() {
        return "DroneMission{" +
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

    /**
     * Builder class for step-by-step construction of DroneMission objects.
     */
    public static class Builder {
        // Mandatory fields
        private String missionId;
        private String droneId;
        private MissionType missionType;
        private LocationCoordinates targetArea;

        // Optional fields with sensible defaults
        private int batteryCapacityMah = 5000;
        private int maxAltitudeMeters = 1000;
        private boolean gpsEnabled = true;
        private boolean nightVisionEnabled = false;
        private boolean encryptedTelemetry = true;
        private String payloadCameraType = "Standard-HD";

        /**
         * Constructor for Builder with required mandatory properties.
         */
        public Builder(String missionId, String droneId, MissionType missionType, LocationCoordinates targetArea) {
            this.missionId = missionId;
            this.droneId = droneId;
            this.missionType = missionType;
            this.targetArea = targetArea;
        }

        // Domain-oriented Fluent API Methods

        public Builder withBatteryCapacity(int mah) {
            this.batteryCapacityMah = mah;
            return this;
        }

        public Builder withMaxAltitude(int meters) {
            this.maxAltitudeMeters = meters;
            return this;
        }

        public Builder enableGps() {
            this.gpsEnabled = true;
            return this;
        }

        public Builder disableGps() {
            this.gpsEnabled = false;
            return this;
        }

        public Builder enableNightVision() {
            this.nightVisionEnabled = true;
            return this;
        }

        public Builder disableNightVision() {
            this.nightVisionEnabled = false;
            return this;
        }

        public Builder enableEncryptedTelemetry() {
            this.encryptedTelemetry = true;
            return this;
        }

        public Builder disableEncryptedTelemetry() {
            this.encryptedTelemetry = false;
            return this;
        }

        public Builder withPayloadCameraType(String cameraType) {
            this.payloadCameraType = cameraType;
            return this;
        }

        // Methods to reconfigure mandatory fields if needed
        public Builder setMissionId(String missionId) {
            this.missionId = missionId;
            return this;
        }

        public Builder setDroneId(String droneId) {
            this.droneId = droneId;
            return this;
        }

        public Builder setMissionType(MissionType missionType) {
            this.missionType = missionType;
            return this;
        }

        public Builder setTargetArea(LocationCoordinates targetArea) {
            this.targetArea = targetArea;
            return this;
        }

        /**
         * Builds and validates the DroneMission product.
         * Enforces both single-field and cross-field validation rules before instantiating.
         *
         * @return A valid, immutable DroneMission object.
         * @throws IllegalArgumentException if single-field rules fail.
         * @throws IllegalStateException    if cross-field rules fail.
         */
        public DroneMission build() {
            validateSingleFieldRules();
            validateCrossFieldRules();
            return new DroneMission(this);
        }

        // Clean Code Chapter 3: Small helper functions with single level of abstraction

        private void validateSingleFieldRules() {
            if (missionId == null || missionId.trim().isEmpty()) {
                throw new IllegalArgumentException("Mission ID must not be null or empty.");
            }
            if (droneId == null || droneId.trim().isEmpty()) {
                throw new IllegalArgumentException("Drone ID must not be null or empty.");
            }
            if (missionType == null) {
                throw new IllegalArgumentException("Mission Type must not be null.");
            }
            if (targetArea == null) {
                throw new IllegalArgumentException("Target Area coordinates must not be null.");
            }
            if (batteryCapacityMah <= 0 || batteryCapacityMah > 30000) {
                throw new IllegalArgumentException("Battery capacity must be between 1 and 30,000 mAh. Provided: " + batteryCapacityMah);
            }
            if (maxAltitudeMeters <= 0 || maxAltitudeMeters > 10000) {
                throw new IllegalArgumentException("Max altitude must be between 1 and 10,000 meters. Provided: " + maxAltitudeMeters);
            }
        }

        private void validateCrossFieldRules() {
            validateIndividualSurveillanceConstraint();
            validateOpticsNightVisionConstraint();
        }

        /**
         * Individual Constraint Rule:
         * SURVEILLANCE or SEARCH_AND_RESCUE missions require gpsEnabled == true AND batteryCapacityMah >= 5000.
         */
        private void validateIndividualSurveillanceConstraint() {
            boolean isCriticalMission = (missionType == MissionType.SURVEILLANCE || missionType == MissionType.SEARCH_AND_RESCUE);
            if (isCriticalMission) {
                if (!gpsEnabled) {
                    throw new IllegalStateException("Individual Constraint Violated: Mission type " + missionType +
                            " mandates GPS to be enabled.");
                }
                if (batteryCapacityMah < 5000) {
                    throw new IllegalStateException("Individual Constraint Violated: Mission type " + missionType +
                            " requires at least 5000 mAh battery capacity. Provided: " + batteryCapacityMah);
                }
            }
        }

        /**
         * Cross-Field Rule 2:
         * If night vision optics are enabled, altitude must not exceed 5000 meters due to sensor atmospheric limits.
         */
        private void validateOpticsNightVisionConstraint() {
            if (nightVisionEnabled && maxAltitudeMeters > 5000) {
                throw new IllegalStateException("Cross-Field Validation Violated: Night vision optics cannot function above 5000 meters altitude. Configured altitude: " + maxAltitudeMeters);
            }
        }
    }
}
