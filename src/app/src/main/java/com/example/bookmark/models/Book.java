package com.example.bookmark.models;

import com.example.bookmark.server.FirestoreIndexable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a book.
 *
 * @author Kyle Hennig.
 */
public class Book implements FirestoreIndexable, Serializable {
    public enum Status {
        AVAILABLE, REQUESTED, ACCEPTED, BORROWED
    }

    private final EntityId id;

    private final EntityId ownerId;
    private String title;
    private String author;
    private String isbn;

    private EntityId photographId = null;
    private String description = "";
    private Status status = Status.AVAILABLE;

    /**
     * Creates a Book.
     *
     * @param owner  The owner.
     * @param title  The title.
     * @param author The author.
     * @param isbn   The ISBN.
     */
    public Book(User owner, String title, String author, String isbn) {
        this(new EntityId(), owner.getId(), title, author, isbn);
    }

    private Book(EntityId id, EntityId ownerId, String title, String author, String isbn) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * Gets the id of the owner.
     *
     * @return The id of the owner.
     */
    public EntityId getOwnerId() {
        return ownerId;
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
     * Sets the author.
     *
     * @param author The author.
     */
    public void setAuthor(String author) {
        this.author = author;
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
     * Sets the ISBN.
     *
     * @param isbn The ISBN.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the photograph.
     *
     * @return The photograph.
     */
    public EntityId getPhotograph() {
        return photographId;
    }

    /**
     * Sets the photograph.
     *
     * @param photograph The photograph.
     */
    public void setPhotograph(Photograph photograph) {
        if (photograph != null) {
            this.photographId = photograph.getId();
        } else {
            this.photographId = null;
        }
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

    /**
     * Sets the title
     *
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", ownerId.toString());
        map.put("title", title);
        map.put("author", author);
        map.put("isbn", isbn);
        map.put("photographId", photographId != null ? photographId.toString() : null);
        map.put("description", description);
        map.put("status", status);
        return map;
    }

    public static Book fromFirestoreDocument(String id, Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Book book = new Book(
            new EntityId(id),
            new EntityId((String) map.get("ownerId")),
            (String) map.get("title"),
            (String) map.get("author"),
            (String) map.get("isbn")
        );
        String photographIdString = (String) map.get("photographId");
        book.photographId = photographIdString != null ? new EntityId(photographIdString) : null;
        book.description = (String) map.get("description");
        book.status = Status.valueOf((String) map.get("status"));
        return book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
            Objects.equals(author, book.author) &&
            Objects.equals(isbn, book.isbn) &&
            Objects.equals(ownerId, book.ownerId) &&
            Objects.equals(photographId, book.photographId) &&
            Objects.equals(description, book.description) &&
            status == book.status;
    }
}
