package com.example.bookmark.models;

import com.example.bookmark.server.FirestoreSerializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a geolocation.
 *
 * @author Kyle Hennig.
 */
public class Geolocation implements FirestoreSerializable, Serializable {
    private final double latitude;
    private final double longitude;

    /**
     * Creates a Geolocation.
     *
     * @param latitude  The latitude.
     * @param longitude The longitude.
     */
    public Geolocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the latitude.
     *
     * @return The latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return The longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        return map;
    }

    public static Geolocation fromFirestoreDocument(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        return new Geolocation(
            (double) map.get("latitude"),
            (double) map.get("longitude")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geolocation that = (Geolocation) o;
        return Double.compare(that.latitude, latitude) == 0 &&
            Double.compare(that.longitude, longitude) == 0;
    }
}
