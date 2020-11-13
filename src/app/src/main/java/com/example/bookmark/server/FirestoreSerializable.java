package com.example.bookmark.server;

import java.util.Map;

/**
 * Interface for an object that can be serialized to a Firebase compatible format.
 *
 * @author Kyle Hennig.
 */
public interface FirestoreSerializable {
    /**
     * Gets the object as a map of strings to types supported by Firestore documents.
     *
     * @return The Firestore document.
     */
    Map<String, Object> toFirestoreDocument();
}
