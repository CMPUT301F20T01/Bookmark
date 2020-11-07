package com.example.bookmark.server;

import java.util.Map;

public interface FirestoreSerializable {
    /**
     * Gets the object as a map of strings to types supported by Firestore documents.
     *
     * @return The Firestore document.
     */
    Map<String, Object> toFirestoreDocument();
}
