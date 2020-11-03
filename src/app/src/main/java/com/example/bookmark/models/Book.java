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

    /**
     * Creates a Book.
     *
     * @param title  The title.
     * @param author The author.
     * @param isbn   This ISBN.
     * @param owner  The owner.
     */
    public Book(String title, String author, String isbn, Owner owner) {
        this(title, author, isbn, owner.getUsername());
    }

    /**
     * Creates a Book.
     *
     * @param title  The title.
     * @param author The author.
     * @param isbn   This ISBN.
     * @param owner  The username of the owner.
     */
    public Book(String title, String author, String isbn, String owner) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.owner = owner;
    }

    /**
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the author.
     *
     * @return The author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the ISBN.
     *
     * @return The ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the username of the owner.
     *
     * @return The username of the owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the photograph.
     *
     * @return The photograph.
     */
    public Photograph getPhotograph() {
        return photograph;
    }

    /**
     * Sets the photograph.
     *
     * @param photograph The photograph.
     */
    public void setPhotograph(Photograph photograph) {
        this.photograph = photograph;
    }

    /**
     * Gets the description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description.
     *
     * @param description The description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the status.
     *
     * @return The status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status The status.
     */
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
