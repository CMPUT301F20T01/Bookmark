package com.example.bookmark.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TODO: Description of class.
 *
 * @author Kyle Hennig.
 */
public class User implements FirestoreSerializable {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;

    public User(String username, String firstName, String lastName, String emailAddress, String phoneNumber) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public static User fromFirestoreDocument(Map<String, Object> map) {
        return new User(
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
