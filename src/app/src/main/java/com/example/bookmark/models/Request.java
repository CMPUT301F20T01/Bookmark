package com.example.bookmark.models;

import com.example.bookmark.server.FirestoreIndexable;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a request for a book.
 *
 * @author Kyle Hennig.
 */
public class Request implements FirestoreIndexable, Serializable {
    public enum Status {
        REQUESTED, ACCEPTED, BORROWED
    }

    private final EntityId id;

    private final EntityId bookId;
    private final EntityId requesterId;
    private final long createdDate; // Stored as a long since Date::equals is flawed.

    private Geolocation location;
    private Status status = Status.REQUESTED;

    /**
     * Creates a Request.
     *
     * @param book      The requested book.
     * @param requester The requester.
     * @param location  The pickup location.
     */
    public Request(Book book, User requester, Geolocation location) {
        this(new EntityId(), book.getId(), requester.getId(), new Date().getTime(), location);
    }

    private Request(EntityId id, EntityId bookId, EntityId requesterId, long createdDate, Geolocation location) {
        this.id = id;
        this.bookId = bookId;
        this.requesterId = requesterId;
        this.createdDate = createdDate;
        this.location = location;
    }

    /**
     * Gets the id of the requested book.
     *
     * @return The id of the requested book.
     */
    public EntityId getBookId() {
        return bookId;
    }

    /**
     * Gets the id of the requester.
     *
     * @return The id of the requester.
     */
    public EntityId getRequesterId() {
        return requesterId;
    }

    /**
     * Gets the date the request was created.
     *
     * @return The date the request was created.
     */
    public Date getCreatedDate() {
        return new Date(createdDate);
    }

    /**
     * Gets the pickup location.
     *
     * @return The pickup location.
     */
    public Geolocation getLocation() {
        return location;
    }

    /**
     * Sets the pickup location.
     *
     * @param location The pickup location.
     */
    public void setLocation(Geolocation location) {
        this.location = location;
    }

    /**
     * Gets the status of the request.
     *
     * @return The status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the request.
     *
     * @param status The status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("bookId", bookId);
        map.put("requesterId", requesterId);
        map.put("createdDate", createdDate);
        map.put("location", location != null ? location.toFirestoreDocument() : null);
        map.put("status", status);
        return map;
    }

    public static Request fromFirestoreDocument(String id, Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Geolocation location = Geolocation.fromFirestoreDocument((Map<String, Object>) map.get("location"));
        Request request = new Request(
            new EntityId(id),
            new EntityId((String) map.get("bookId")),
            new EntityId((String) map.get("requesterId")),
            (long) map.get("createdDate"),
            location
        );
        request.status = Status.valueOf((String) map.get("status"));
        return request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return createdDate == request.createdDate &&
            Objects.equals(bookId, request.bookId) &&
            Objects.equals(requesterId, request.requesterId) &&
            Objects.equals(location, request.location) &&
            status == request.status;
    }
}
