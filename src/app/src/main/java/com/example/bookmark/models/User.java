package com.example.bookmark.models;

import com.example.bookmark.server.FirestoreIndexable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a user of the app.
 *
 * @author Kyle Hennig.
 */
public class User implements FirestoreIndexable, Serializable {
    private final EntityId id;

    private final String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;

    /**
     * Creates a User.
     *
     * @param username     The user's username.
     * @param firstName    The user's first name.
     * @param lastName     The user's last name.
     * @param emailAddress The user's email address.
     * @param phoneNumber  The user's phone number.
     */
    public User(String username, String firstName, String lastName, String emailAddress, String phoneNumber) {
        this(new EntityId(), username, firstName, lastName, emailAddress, phoneNumber);
    }

    private User(EntityId id, String username, String firstName, String lastName, String emailAddress, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the user's username.
     *
     * @return The user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's first name.
     *
     * @return The user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName The user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return The user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName The user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's email address.
     *
     * @return The user's email address.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the user's email address.
     *
     * @param emailAddress The user's email address.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the user's phone number.
     *
     * @return The user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phoneNumber The user's phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("emailAddress", emailAddress);
        map.put("phoneNumber", phoneNumber);
        return map;
    }

    public static User fromFirestoreDocument(String id, Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        return new User(
            new EntityId(id),
            (String) map.get("username"),
            (String) map.get("firstName"),
            (String) map.get("lastName"),
            (String) map.get("emailAddress"),
            (String) map.get("phoneNumber")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
            Objects.equals(firstName, user.firstName) &&
            Objects.equals(lastName, user.lastName) &&
            Objects.equals(emailAddress, user.emailAddress) &&
            Objects.equals(phoneNumber, user.phoneNumber);
    }
}
