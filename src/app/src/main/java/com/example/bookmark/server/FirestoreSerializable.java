package com.example.bookmark.server;

import java.util.Map;

public interface FirestoreSerializable {
    /**
     * Gets the unique id for this object.
     *
     * @return The id.
     */
    String getId();

    /**
     * Gets the object as a map of strings to types supported by Firestore documents.
     *
     * @return The Firestore document.
     */
    Map<String, Object> toFirestoreDocument();


}
