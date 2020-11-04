package com.example.bookmark.models;

import java.io.Serializable;

/**
 * TODO: Description of class.
 * @author Kyle Hennig.
 */
public class Geolocation implements Serializable {
    private float latitude;
    private float longitude;

    public Geolocation(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
