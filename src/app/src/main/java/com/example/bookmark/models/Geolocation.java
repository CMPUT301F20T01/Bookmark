package com.example.bookmark.models;

import com.example.bookmark.server.FirestoreSerializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represents a geolocation.
 *
 * @author Kyle Hennig.
 */
public class Geolocation implements FirestoreSerializable, Serializable {
    private final float latitude;
    private final float longitude;

    /**
     * Creates a Geolocation.
     *
     * @param latitude  The latitude.
     * @param longitude The longitude.
     */
    public Geolocation(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the latitude.
     *
     * @return The latitude.
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return The longitude.
     */
    public float getLongitude() {
        return longitude;
    }

    @Override
    public String getId() {
        return String.format(Locale.CANADA, "%f:%f", latitude, longitude);
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        return map;
    }

    public static Geolocation fromFirestoreDocument(Map<String, Object> map) {
        return new Geolocation(
            (float) ((double) map.get("latitude")),
            (float) ((double) map.get("longitude"))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geolocation that = (Geolocation) o;
        return Float.compare(that.latitude, latitude) == 0 &&
            Float.compare(that.longitude, longitude) == 0;
    }
}
