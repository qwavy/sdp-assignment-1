package com.sdp.drone;

import com.sdp.drone.director.DroneMissionDirector;
import com.sdp.drone.legacy.LegacyDroneMission;
import com.sdp.drone.model.DroneMission;
import com.sdp.drone.model.LocationCoordinates;
import com.sdp.drone.model.MissionType;

/**
 * Main application demonstrating legacy constructor anti-pattern vs refactored Builder pattern,
 * preset configurations by Director, and validation rule enforcement.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== DRONE MISSION CONFIGURATION SYSTEM ===");
        System.out.println("Course: Software Design Patterns | Pattern: Builder");
        System.out.println("Instructor: PhD Makpal Zhartybayeva\n");

        LocationCoordinates astanaArea = new LocationCoordinates(51.16939, 71.44907, 15.0);

        // 1. Part A - Legacy Construction Anti-Pattern
        System.out.println("--- Part A: Legacy Constructor (Anti-Pattern) ---");
        LegacyDroneMission legacyMission = new LegacyDroneMission(
                "M-101", "DRONE-ALPHA", MissionType.SURVEILLANCE, astanaArea,
                6000, 3000, true, true, false, "Standard-HD"
        );
        System.out.println("Legacy Mission Created: " + legacyMission);
        System.out.println("Problem: Parameter boolean flags (true, true, false) are unreadable and error-prone!\n");

        // 2. Part B & D - Builder Pattern & Director Presets
        System.out.println("--- Part B & D: Builder Pattern & Director Presets ---");
        DroneMissionDirector director = new DroneMissionDirector();

        System.out.println("\n1. Building RECON_BASIC Preset:");
        DroneMission reconMission = director.buildReconBasic("M-201", "DRONE-BETA", astanaArea);
        System.out.println("Recon Mission: " + reconMission);

        System.out.println("\n2. Building SAFE_SURVEILLANCE Preset:");
        DroneMission safeSurveillance = director.buildSurveillanceSafe("M-202", "DRONE-GAMMA", astanaArea);

        System.out.println("\n3. Building HIGH_ALTITUDE Preset:");
        DroneMission highAltMission = director.buildHighAltitudePerformance("M-203", "DRONE-DELTA", astanaArea);
        System.out.println("High Altitude Mission: " + highAltMission);

        // 3. Custom Fluent Builder Construction
        System.out.println("\n--- Custom Fluent Builder Mission ---");
        DroneMission customMission = new DroneMission.Builder("M-300", "DRONE-EAGLE", MissionType.SEARCH_AND_RESCUE, astanaArea)
                .withBatteryCapacity(10000)
                .withMaxAltitude(4000)
                .enableGps()
                .enableNightVision()
                .enableEncryptedTelemetry()
                .withPayloadCameraType("FLIR-Thermal-X")
                .build();
        System.out.println("Custom Search & Rescue Mission Built Successfully: " + customMission);

        // 4. Validation Violation Demonstrations
        System.out.println("\n--- Part C: Demonstrating Validation Rule Enforcement ---");
        try {
            System.out.println("Attempting to build SURVEILLANCE mission without GPS...");
            new DroneMission.Builder("M-404", "DRONE-FAIL", MissionType.SURVEILLANCE, astanaArea)
                    .disableGps()
                    .withBatteryCapacity(6000)
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Caught Expected Constraint Failure: " + e.getMessage());
        }

        try {
            System.out.println("Attempting to build Night Vision mission at 8000m altitude...");
            new DroneMission.Builder("M-405", "DRONE-FAIL2", MissionType.RECON, astanaArea)
                    .enableNightVision()
                    .withMaxAltitude(8000)
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Caught Expected Cross-Field Failure: " + e.getMessage());
        }

        System.out.println("\nSystem execution completed successfully.");
    }
}
