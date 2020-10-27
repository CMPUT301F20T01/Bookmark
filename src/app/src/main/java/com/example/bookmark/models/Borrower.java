package com.example.bookmark.models;

import java.util.List;

public class Borrower extends User {
    private List<Request> requests;
    private List<Book> borrowedBooks;

    public Borrower(String username, String firstName, String lastName, String emailAddress, String phoneNumber, List<Request> requests, List<Book> borrowedBooks) {
        super(username, firstName, lastName, emailAddress, phoneNumber);
        this.requests = requests;
        this.borrowedBooks = borrowedBooks;
    }

    // TODO: Implement methods.
}
