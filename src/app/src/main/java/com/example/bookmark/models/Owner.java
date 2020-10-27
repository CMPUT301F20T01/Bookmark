package com.example.bookmark.models;

import java.util.List;

/**
 * TODO: Description of class.
 * @author Kyle Hennig.
 */
public class Owner extends User {
    private List<Book> ownedBooks;
    private List<Request> requests;

    public Owner(String username, String firstName, String lastName, String emailAddress, String phoneNumber, List<Book> ownedBooks, List<Request> requests) {
        super(username, firstName, lastName, emailAddress, phoneNumber);
        this.ownedBooks = ownedBooks;
        this.requests = requests;
    }

    // TODO: Add methods.
}
