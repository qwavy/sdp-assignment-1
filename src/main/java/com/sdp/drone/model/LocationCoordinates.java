package com.sdp.drone.model;

import java.util.Objects;

/**
 * Value Object representing geographic coordinates and operational radius for a drone target area.
 * Immutable design guarantees thread safety and prevents unintended side effects.
 */
public final class LocationCoordinates {
    private final double latitude;
    private final double longitude;
    private final double radiusKm;

    public LocationCoordinates(double latitude, double longitude, double radiusKm) {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees. Provided: " + latitude);
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees. Provided: " + longitude);
        }
        if (radiusKm <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0 km. Provided: " + radiusKm);
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusKm = radiusKm;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadiusKm() {
        return radiusKm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationCoordinates coordinates = (LocationCoordinates) o;
        return Double.compare(coordinates.latitude, latitude) == 0 &&
               Double.compare(coordinates.longitude, longitude) == 0 &&
               Double.compare(coordinates.radiusKm, radiusKm) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, radiusKm);
    }

    @Override
    public String toString() {
        return String.format("[Lat: %.4f, Lon: %.4f, Radius: %.1f km]", latitude, longitude, radiusKm);
    }
}
