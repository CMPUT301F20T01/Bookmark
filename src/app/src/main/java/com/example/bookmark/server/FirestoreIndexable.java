package com.example.bookmark.server;

import com.example.bookmark.models.EntityId;

/**
 * Interface for an object that can be assigned a unique id by Firebase for storage in a collection.
 *
 * @author Kyle Hennig.
 */
public interface FirestoreIndexable extends FirestoreSerializable {
    /**
     * Gets the unique id for this object.
     *
     * @return The id.
     */
    EntityId getId();
}
