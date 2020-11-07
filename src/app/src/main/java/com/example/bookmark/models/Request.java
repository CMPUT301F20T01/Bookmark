package com.example.bookmark.models;

import com.example.bookmark.server.FirestoreSerializable;
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

    private final String bookId;
    private final String requesterId;
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
        this(book.getId(), requester.getId(), new Date(), location);
    }

    /**
     * Creates a Request.
     *
     * @param bookId      The id of the requested book.
     * @param requesterId The id of the requester.
     * @param createdDate The date the request was created.
     * @param location    The pickup location.
     */
    private Request(String bookId, String requesterId, Date createdDate, Geolocation location) {
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
    public String getBookId() {
        return bookId;
    }

    /**
     * Gets the id of the requester.
     *
     * @return The id of the requester.
     */
    public String getRequesterId() {
        return requesterId;
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
    public String getId() {
        return bookId + requesterId;
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("bookId", bookId);
        map.put("requesterId", requesterId);
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
        Request request = new Request((String) map.get("bookId"), (String) map.get("requesterId"), ((Timestamp) map.get("createdDate")).toDate(), location);
        request.status = Status.valueOf((String) map.get("status"));
        return request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(bookId, request.bookId) &&
            Objects.equals(requesterId, request.requesterId) &&
            Objects.equals(createdDate, request.createdDate) &&
            Objects.equals(location, request.location) &&
            status == request.status;
    }
}
