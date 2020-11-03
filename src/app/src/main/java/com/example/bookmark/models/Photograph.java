package com.example.bookmark.models;

import android.graphics.Bitmap;

/**
 * Represents a photograph taken by the app.
 *
 * @author Kyle Hennig.
 */
public class Photograph {
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
}
