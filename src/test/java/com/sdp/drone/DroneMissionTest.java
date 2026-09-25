package com.sdp.drone;

import com.sdp.drone.director.DroneMissionDirector;
import com.sdp.drone.model.DroneMission;
import com.sdp.drone.model.LocationCoordinates;
import com.sdp.drone.model.MissionType;

/**
 * Automated Test Suite for DroneMission Configuration & Builder Pattern.
 * Covers 12 comprehensive test scenarios including valid cases, invalid cases,
 * boundary limits, individual constraints, builder immutability/reuse independence,
 * and preset configurations.
 */
public class DroneMissionTest {

    private static final LocationCoordinates DEFAULT_AREA = new LocationCoordinates(51.16939, 71.44907, 10.0);
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("     RUNNING AUTOMATED DRONE MISSION TESTS       ");
        System.out.println("=================================================");

        DroneMissionTest runner = new DroneMissionTest();

        runner.runTest("1. Valid Basic Recon Mission", runner::testValidBasicReconMission);
        runner.runTest("2. Valid Safe Surveillance Preset", runner::testValidSafeSurveillancePreset);
        runner.runTest("3. Valid High Altitude Mission", runner::testValidHighAltitudeMission);

        runner.runTest("4. Invalid Empty Mission ID", runner::testInvalidEmptyMissionId);
        runner.runTest("5. Invalid Battery Capacity (>30,000 mAh)", runner::testInvalidBatteryCapacity);
        runner.runTest("6. Invalid Max Altitude (>10,000 m)", runner::testInvalidMaxAltitude);

        runner.runTest("7. Boundary Case: Night Vision at exact 5000m Altitude Limit", runner::testBoundaryAltitudeNightVisionExactLimit);
        runner.runTest("8. Boundary Case: Surveillance at exact 5000 mAh Battery Limit", runner::testBoundarySurveillanceBatteryExactLimit);

        runner.runTest("9. Individual Constraint: Surveillance without GPS fails", runner::testIndividualConstraintSurveillanceWithoutGps);
        runner.runTest("10. Individual Constraint: Surveillance with 4999 mAh battery fails", runner::testIndividualConstraintSurveillanceWeakBattery);
        runner.runTest("11. Cross-Field Rule: Night Vision above 5000m fails", runner::testCrossFieldNightVisionHighAltitude);

        runner.runTest("12. Builder Reuse & Product Independence", runner::testBuilderReuseProductIndependence);

        System.out.println("=================================================");
        System.out.println(String.format("TEST RESULTS: Passed: %d | Failed: %d | Total: %d",
                testsPassed, testsFailed, (testsPassed + testsFailed)));
        System.out.println("=================================================");

        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private void runTest(String testName, Runnable testMethod) {
        try {
            System.out.print("[TEST] " + testName + " ... ");
            testMethod.run();
            System.out.println("PASSED");
            testsPassed++;
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            t.printStackTrace();
            testsFailed++;
        }
    }

    // 1. Valid Scenarios

    public void testValidBasicReconMission() {
        DroneMission mission = new DroneMission.Builder("M-01", "D-01", MissionType.RECON, DEFAULT_AREA)
                .withBatteryCapacity(4000)
                .withMaxAltitude(2000)
                .enableGps()
                .build();

        assertCondition(mission.getMissionId().equals("M-01"), "Mission ID mismatch");
        assertCondition(mission.getDroneId().equals("D-01"), "Drone ID mismatch");
        assertCondition(mission.getMissionType() == MissionType.RECON, "Mission Type mismatch");
        assertCondition(mission.getBatteryCapacityMah() == 4000, "Battery mismatch");
    }

    public void testValidSafeSurveillancePreset() {
        DroneMissionDirector director = new DroneMissionDirector();
        DroneMission mission = director.buildSurveillanceSafe("M-SAFE", "D-SAFE", DEFAULT_AREA);

        assertCondition(mission.getMissionType() == MissionType.SURVEILLANCE, "Should be SURVEILLANCE");
        assertCondition(mission.isGpsEnabled(), "GPS must be enabled");
        assertCondition(mission.getBatteryCapacityMah() >= 5000, "Battery must be >= 5000");
        assertCondition(mission.getPayloadCameraType().equals("Thermal-4K-Zoom"), "Payload camera type mismatch");
    }

    public void testValidHighAltitudeMission() {
        DroneMission mission = new DroneMission.Builder("M-HI", "D-HI", MissionType.HIGH_ALTITUDE, DEFAULT_AREA)
                .withMaxAltitude(9500)
                .withBatteryCapacity(15000)
                .disableNightVision()
                .build();

        assertCondition(mission.getMaxAltitudeMeters() == 9500, "Altitude should be 9500");
        assertCondition(!mission.isNightVisionEnabled(), "Night vision should be disabled");
    }

    // 2. Invalid Scenarios

    public void testInvalidEmptyMissionId() {
        try {
            new DroneMission.Builder("  ", "D-01", MissionType.RECON, DEFAULT_AREA).build();
            throw new AssertionError("Should have thrown IllegalArgumentException for blank missionId");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testInvalidBatteryCapacity() {
        try {
            new DroneMission.Builder("M-01", "D-01", MissionType.RECON, DEFAULT_AREA)
                    .withBatteryCapacity(35000)
                    .build();
            throw new AssertionError("Should have thrown IllegalArgumentException for battery > 30000 mAh");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testInvalidMaxAltitude() {
        try {
            new DroneMission.Builder("M-01", "D-01", MissionType.RECON, DEFAULT_AREA)
                    .withMaxAltitude(12000)
                    .build();
            throw new AssertionError("Should have thrown IllegalArgumentException for altitude > 10000 meters");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // 3. Boundary Cases

    public void testBoundaryAltitudeNightVisionExactLimit() {
        DroneMission mission = new DroneMission.Builder("M-BOUND1", "D-BOUND1", MissionType.RECON, DEFAULT_AREA)
                .withMaxAltitude(5000)
                .enableNightVision()
                .build();

        assertCondition(mission.getMaxAltitudeMeters() == 5000, "5000m should be valid with night vision");
        assertCondition(mission.isNightVisionEnabled(), "Night vision should be enabled");
    }

    public void testBoundarySurveillanceBatteryExactLimit() {
        DroneMission mission = new DroneMission.Builder("M-BOUND2", "D-BOUND2", MissionType.SURVEILLANCE, DEFAULT_AREA)
                .withBatteryCapacity(5000)
                .enableGps()
                .build();

        assertCondition(mission.getBatteryCapacityMah() == 5000, "5000 mAh battery should be valid for surveillance");
    }

    // 4. Individual Constraint & Cross-Field Rules

    public void testIndividualConstraintSurveillanceWithoutGps() {
        try {
            new DroneMission.Builder("M-FAIL1", "D-FAIL1", MissionType.SURVEILLANCE, DEFAULT_AREA)
                    .disableGps()
                    .withBatteryCapacity(6000)
                    .build();
            throw new AssertionError("Surveillance mission without GPS must throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertCondition(e.getMessage().contains("GPS"), "Exception message should mention GPS requirement");
        }
    }

    public void testIndividualConstraintSurveillanceWeakBattery() {
        try {
            new DroneMission.Builder("M-FAIL2", "D-FAIL2", MissionType.SURVEILLANCE, DEFAULT_AREA)
                    .enableGps()
                    .withBatteryCapacity(4999)
                    .build();
            throw new AssertionError("Surveillance mission with 4999 mAh battery must throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertCondition(e.getMessage().contains("at least 5000 mAh"), "Exception message should mention battery capacity requirement");
        }
    }

    public void testCrossFieldNightVisionHighAltitude() {
        try {
            new DroneMission.Builder("M-FAIL3", "D-FAIL3", MissionType.RECON, DEFAULT_AREA)
                    .enableNightVision()
                    .withMaxAltitude(5001)
                    .build();
            throw new AssertionError("Night vision at altitude 5001m must throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertCondition(e.getMessage().contains("Night vision optics cannot function above 5000 meters"), "Exception should mention optics limit");
        }
    }

    // 5. Builder Reuse & Product Independence Test

    public void testBuilderReuseProductIndependence() {
        DroneMission.Builder builder = new DroneMission.Builder("M-ORIG", "D-ORIG", MissionType.RECON, DEFAULT_AREA)
                .withBatteryCapacity(6000)
                .withMaxAltitude(3000)
                .enableGps();

        DroneMission product1 = builder.build();

        // Mutate builder state to construct product 2
        builder.setMissionId("M-MUTATED")
                .setDroneId("D-MUTATED")
                .withBatteryCapacity(9000)
                .withMaxAltitude(4500);

        DroneMission product2 = builder.build();

        // Assert Product 1 remains completely unchanged and independent
        assertCondition(product1.getMissionId().equals("M-ORIG"), "Product 1 mission ID mutated!");
        assertCondition(product1.getDroneId().equals("D-ORIG"), "Product 1 drone ID mutated!");
        assertCondition(product1.getBatteryCapacityMah() == 6000, "Product 1 battery capacity mutated!");
        assertCondition(product1.getMaxAltitudeMeters() == 3000, "Product 1 max altitude mutated!");

        // Assert Product 2 has new values
        assertCondition(product2.getMissionId().equals("M-MUTATED"), "Product 2 mission ID mismatch");
        assertCondition(product2.getBatteryCapacityMah() == 9000, "Product 2 battery capacity mismatch");
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException("Assertion Failed: " + message);
        }
    }
}
