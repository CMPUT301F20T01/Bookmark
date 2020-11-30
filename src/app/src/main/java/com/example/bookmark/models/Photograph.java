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

    public Photograph(Uri imageUri) {
        this(new EntityId(), imageUri);
    }

    private Photograph(EntityId id, Uri imageUri) {
        this.id = id;
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

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
