package com.example.bookmark.mocks;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.InMemoryStorageService;
import com.example.bookmark.server.StorageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Mocks a storage service with preexisting data.
 *
 * @author Kyle Hennig.
 */
public class MockStorageService {
    public static StorageService mockStorageService() {
        List<User> users = new ArrayList<>();
        users.add(MockModels.mockOwner());
        users.add(MockModels.mockRequester());
        List<Book> books = new ArrayList<>();
        books.add(MockModels.mockBook1());
        books.add(MockModels.mockBook2());
        List<Request> requests = new ArrayList<>();
        requests.add(MockModels.mockRequest1());
        requests.add(MockModels.mockRequest2());
        return new InMemoryStorageService(users, books, requests);
    }
}
