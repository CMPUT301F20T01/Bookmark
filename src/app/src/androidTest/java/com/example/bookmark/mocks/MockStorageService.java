package com.example.bookmark.mocks;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Photograph;
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
    public static StorageService getMockStorageService() {
        List<User> users = new ArrayList<>();
        users.add(MockModels.getMockOwner());
        users.add(MockModels.getMockRequester());
        List<Book> books = new ArrayList<>();
        MockModels.getMockBook1().setStatus(Book.Status.REQUESTED);
        MockModels.getMockBook2().setStatus(Book.Status.BORROWED);
        books.add(MockModels.getMockBook1());
        books.add(MockModels.getMockBook2());
        books.add(MockModels.getMockBook3());
        List<Request> requests = new ArrayList<>();
        requests.add(MockModels.getMockRequest1());
        requests.add(MockModels.getMockRequest2());
        List<Photograph> photographs = new ArrayList<>();
        photographs.add(MockModels.getMockPhotograph());
        return new InMemoryStorageService(users, books, requests, photographs);
    }
}
