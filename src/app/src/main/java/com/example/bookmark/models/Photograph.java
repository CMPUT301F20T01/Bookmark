package com.example.bookmark.models;

import android.graphics.Bitmap;

import com.example.bookmark.server.FirestoreSerializable;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a photograph taken by the app.
 *
 * @author Kyle Hennig.
 */
public class Photograph implements FirestoreSerializable {
    private Bitmap bitmap;

    public Photograph(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        // TODO: Photograph will likely have to be compressed and serialized due to size.
        return null;
    }

    public static Photograph fromFirestoreDocument(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        // TODO: Deserialize and decompress the photograph.
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photograph that = (Photograph) o;
        return Objects.equals(bitmap, that.bitmap);
    }
}
