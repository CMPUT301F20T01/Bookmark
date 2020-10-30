package com.example.bookmark.models;

/**
 * TODO: Description of class.
 * @author Kyle Hennig.
 */
public class Book {
    public enum Status {
        AVAILABLE, REQUESTED, ACCEPTED, BORROWED
    }

    private String title;
    private String author;
    private String isbn;
    private Owner owner;

    private Photograph photograph = null;
    private String description = "";
    private Status status = Status.AVAILABLE;

    public Book(String title, String author, String isbn, Owner owner) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Owner getOwner() {
        return owner;
    }

    public Photograph getPhotograph() {
        return photograph;
    }

    public void setPhotograph(Photograph photograph) {
        this.photograph = photograph;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}