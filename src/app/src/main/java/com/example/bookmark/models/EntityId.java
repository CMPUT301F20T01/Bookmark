package com.example.bookmark.models;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.UUID;

public class EntityId {
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
