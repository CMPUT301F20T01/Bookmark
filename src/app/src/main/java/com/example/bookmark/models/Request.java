package com.example.bookmark.models;

/**
 * TODO: Description of class.
 * @author Kyle Hennig.
 */
public class Request {
    public enum Status {
        REQUESTED, ACCEPTED, BORROWED
    }

    private Book book;
    private Borrower requester;
    private Geolocation location;
    private Status status = Status.REQUESTED;

    public Request(Book book, Borrower requester, Geolocation location) {
        /**
         * TODO: add requestDate attribute (either Date object with toString method or just String)
         * @author Nayan Prakash
         */
        this.book = book;
        this.requester = requester;
        this.location = location;
    }

    // TODO: Implement methods.

    public Borrower getRequester() {
        return this.requester;
    }

    public String getRequestDate() {
        // TODO: Implement
        return null;
    }
}
