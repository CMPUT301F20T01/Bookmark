package com.example.bookmark.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a unique id that can identify an entity.
 * Assigns new ids using version 4 UUID generation.
 *
 * @author Kyle Hennig.
 */
public class EntityId implements Serializable {
    private final String id;

    public EntityId() {
        this.id = UUID.randomUUID().toString();
    }

    public EntityId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityId entityId = (EntityId) o;
        return Objects.equals(id, entityId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
