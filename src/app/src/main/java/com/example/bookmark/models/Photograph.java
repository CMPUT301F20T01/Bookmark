package com.example.bookmark.models;

import android.net.Uri;

import com.example.bookmark.server.FirestoreIndexable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a photograph taken by the app.
 *
 * @author Kyle Hennig.
 */
public class Photograph implements FirestoreIndexable {
    private final EntityId id;
    private Uri imageUri;

    /**
     * Creates a Photograph.
     *
     * @param imageUri The URI of the image.
     */
    public Photograph(Uri imageUri) {
        this(new EntityId(), imageUri);
    }

    private Photograph(EntityId id, Uri imageUri) {
        this.id = id;
        this.imageUri = imageUri;
    }

    /**
     * Gets the image URI.
     *
     * @return The URI.
     */
    public Uri getImageUri() {
        return imageUri;
    }

    /**
     * Sets the image URI.
     *
     * @param imageUri The URI.
     */
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("imageUri", imageUri.toString());
        return map;
    }

    public static Photograph fromFirestoreDocument(String id, Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        return new Photograph(
            new EntityId(id),
            (Uri) map.get("imageUri")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photograph that = (Photograph) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(imageUri, that.imageUri);
    }
}
