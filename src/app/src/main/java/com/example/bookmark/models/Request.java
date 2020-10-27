package com.example.bookmark.models;

public class Request {
    public enum Status {
        REQUESTED, ACCEPTED, BORROWED
    }

    private Book book;
    private Borrower requester;
    private Geolocation location;
    private Status status = Status.REQUESTED;

    public Request(Book book, Borrower requester, Geolocation location) {
        this.book = book;
        this.requester = requester;
        this.location = location;
    }

    // TODO: Implement methods.
}
