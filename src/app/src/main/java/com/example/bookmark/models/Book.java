package com.example.bookmark.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a book.
 *
 * @author Kyle Hennig.
 */
public class Book implements FirestoreSerializable {
    public enum Status {
        AVAILABLE, REQUESTED, ACCEPTED, BORROWED
    }

    private final String title;
    private final String author;
    private final String isbn;
    private final String owner;

    private Photograph photograph = null;
    private String description = "";
    private Status status = Status.AVAILABLE;

    public Book(String title, String author, String isbn, String owner) {
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

    public String getOwner() {
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

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("author", author);
        map.put("isbn", isbn);
        map.put("owner", owner);
        // TODO: Photograph will likely have to be compressed and serialized due to size.
        map.put("photograph", photograph);
        map.put("description", description);
        // TODO: Verify that status enum serializes and deserializes correctly.
        map.put("status", status);
        return map;
    }

    public static Book fromFirestoreDocument(Map<String, Object> map) {
        Book book = new Book((String) map.get("title"), (String) map.get("author"), (String) map.get("isbn"), (String) map.get("owner"));
        book.photograph = (Photograph) map.get("photograph");
        book.description = (String) map.get("description");
        book.status = (Status) map.get("status");
        return book;
    }
}
