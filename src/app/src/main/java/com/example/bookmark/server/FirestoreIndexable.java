package com.example.bookmark.server;

public interface FirestoreIndexable extends FirestoreSerializable {
    /**
     * Gets the unique id for this object.
     *
     * @return The id.
     */
    String getId();
}
