package com.example.bookmark.mocks;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Geolocation;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;

/**
 * Mocks users, books, and requests.
 *
 * @author Kyle Hennig.
 */
public class MockModels {
    public static User mockOwner() {
        return new User("john.smith42", "John", "Smith", "jsmith@ualberta.ca", "7801234567");
    }

    public static User mockRequester() {
        return new User("mary.jane9", "Mary", "Jane", "mjane@ualberta.ca", "7809999999");
    }

    public static Book mockBook1() {
        return new Book(mockOwner(), "Code Complete 2", "Steve McConnell", "0-7356-1976-0");
    }

    public static Book mockBook2() {
        return new Book(mockOwner(), "Programming Pearls", "Jon Bentley", "978-0-201-65788-3");
    }

    public static Request mockRequest1() {
        return new Request(mockBook1(), mockRequester(), new Geolocation(53.5461, -113.4938));
    }

    public static Request mockRequest2() {
        return new Request(mockBook1(), mockRequester(), new Geolocation(53.5461, -113.4938));
    }
}
