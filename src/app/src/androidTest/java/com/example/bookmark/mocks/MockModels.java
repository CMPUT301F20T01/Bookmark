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
    private static final User mockOwner = new User("john.smith42", "John", "Smith", "jsmith@ualberta.ca", "7801234567");
    private static final User mockRequester = new User("mary.jane9", "Mary", "Jane", "mjane@ualberta.ca", "7809999999");
    private static final Book mockBook1 = new Book(mockOwner, "Code Complete 2", "Steve McConnell", "0-7356-1976-0");
    private static final Book mockBook2 = new Book(mockOwner, "Programming Pearls", "Jon Bentley", "978-0-201-65788-3");
    private static final Book mockBook3 = new Book(mockOwner, "Unedited Title", "Unedited Author", "000000000");
    private static final Geolocation mockLocation = new Geolocation(53.5461, -113.4938);
    private static final Request request1 = new Request(mockBook1, mockRequester, mockLocation);
    private static final Request request2 = new Request(mockBook2, mockRequester, mockLocation);

    public static User getMockOwner() {
        return mockOwner;
    }

    public static User getMockRequester() {
        return mockRequester;
    }

    public static Book getMockBook1() {
        return mockBook1;
    }

    public static Book getMockBook2() {
        return mockBook2;
    }

    public static Book getMockBook3() {
        return mockBook3;
    }

    public static Request getMockRequest1() {
        return request1;
    }

    public static Request getMockRequest2() {
        return request2;
    }
}
