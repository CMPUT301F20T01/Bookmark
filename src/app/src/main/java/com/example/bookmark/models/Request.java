package com.example.bookmark.models;

import com.google.firebase.Timestamp;

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
public class Request implements FirestoreSerializable, Serializable {
    public enum Status {
        REQUESTED, ACCEPTED, BORROWED
    }

    private final String book;
    private final String requester;
    private final Date createdDate;

    private Geolocation location;
    private Status status = Status.REQUESTED;

    /**
     * Creates a Request.
     *
     * @param book      The requested book.
     * @param requester The requester.
     * @param location  The pickup location.
     */
    public Request(Book book, Borrower requester, Geolocation location) {
        this(book.getIsbn(), requester.getUsername(), new Date(), location);
    }

    /**
     * Creates a Request.
     *
     * @param book        The requested book ISBN.
     * @param requester   The requester's username.
     * @param createdDate The date the request was created.
     * @param location    The pickup location.
     */
    private Request(String book, String requester, Date createdDate, Geolocation location) {
        this.book = book;
        this.requester = requester;
        this.createdDate = createdDate;
        this.location = location;
    }

    /**
     * Gets the requested book's ISBN.
     *
     * @return The requested book's ISBN.
     */
    public String getBook() {
        return book;
    }

    /**
     * Gets the requester's username.
     *
     * @return The requester's username.
     */
    public String getRequester() {
        return requester;
    }

    /**
     * Gets the date the request was created.
     *
     * @return The date the request was created.
     */
    public Date getCreatedDate() {
        return createdDate;
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
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("book", book);
        map.put("requester", requester);
        map.put("createdDate", createdDate);
        map.put("location", location == null ? null : location.toFirestoreDocument());
        map.put("status", status);
        return map;
    }

    public static Request fromFirestoreDocument(Map<String, Object> map) {
        Map<String, Object> locationMap = (Map<String, Object>) map.get("location");
        Geolocation location = null;
        if (locationMap != null) {
            location = Geolocation.fromFirestoreDocument(locationMap);
        }
        Request request = new Request((String) map.get("book"), (String) map.get("requester"), ((Timestamp) map.get("createdDate")).toDate(), location);
        request.status = Status.valueOf((String) map.get("status"));
        return request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(book, request.book) &&
            Objects.equals(requester, request.requester) &&
            Objects.equals(createdDate, request.createdDate) &&
            Objects.equals(location, request.location) &&
            status == request.status;
    }
}
