package com.example.bookmark.server;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.EntityId;
import com.example.bookmark.models.Photograph;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of StorageProvider that stores all of the app's data in memory.
 * Useful for intent testing and other types of tests that do not need a server connection.
 *
 * @author Kyle Hennig.
 */
public class InMemoryStorageService implements StorageService {
    private final Map<EntityId, User> users = new HashMap<>();
    private final Map<EntityId, Book> books = new HashMap<>();
    private final Map<EntityId, Request> requests = new HashMap<>();
    private final Map<EntityId, Photograph> photographs = new HashMap<>();

    /**
     * Creates an InMemoryStorageService.
     */
    public InMemoryStorageService() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Creates an InMemoryStorageService.
     *
     * @param users       The users that should exist to start.
     * @param books       The books that should exist to start.
     * @param requests    The requests that should exist to start.
     * @param photographs The photographs that should exist to start.
     */
    public InMemoryStorageService(List<User> users, List<Book> books, List<Request> requests, List<Photograph> photographs) {
        for (User user : users) {
            storeEntity(this.users, user);
        }
        for (Book book : books) {
            storeEntity(this.books, book);
        }
        for (Request request : requests) {
            storeEntity(this.requests, request);
        }
        for (Photograph photograph : photographs) {
            storeEntity(this.photographs, photograph);
        }
    }

    @Override
    public void storeUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(users, user);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveUserByUsername(String username, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                onSuccessListener.onSuccess(user);
                return;
            }
        }
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void storeBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(books, book);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveBook(EntityId id, OnSuccessListener<Book> onSuccessListener, OnFailureListener onFailureListener) {
        onSuccessListener.onSuccess(books.get(id));
    }

    @Override
    public void retrieveBooks(OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        onSuccessListener.onSuccess(new ArrayList<>(books.values()));
    }

    @Override
    public void retrieveBooksByOwner(User owner, OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        List<Book> booksByOwner = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getOwnerId().equals(owner.getId())) {
                booksByOwner.add(book);
            }
        }
        onSuccessListener.onSuccess(booksByOwner);
    }

    @Override
    public void retrieveBooksByRequester(User requester, OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        List<EntityId> bookIds = new ArrayList<>();
        for (Request request : requests.values()) {
            bookIds.add(request.getBookId());
        }
        List<Book> booksByRequester = new ArrayList<>();
        for (Book book : books.values()) {
            if (bookIds.contains(book.getId())) {
                booksByRequester.add(book);
            }
        }
        onSuccessListener.onSuccess(booksByRequester);
    }

    @Override
    public void deleteBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        books.remove(book.getId());
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void storeRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(requests, request);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveRequest(EntityId id, OnSuccessListener<Request> onSuccessListener, OnFailureListener onFailureListener) {
        onSuccessListener.onSuccess(requests.get(id));
    }

    @Override
    public void retrieveRequestsByBook(Book book, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        List<Request> requestsByBook = new ArrayList<>();
        for (Request request : requests.values()) {
            if (request.getBookId().equals(book.getId())) {
                requestsByBook.add(request);
            }
        }
        onSuccessListener.onSuccess(requestsByBook);
    }

    @Override
    public void retrieveRequestsByRequester(User requester, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        List<Request> requestsByRequester = new ArrayList<>();
        for (Request request : requests.values()) {
            if (request.getRequesterId().equals(requester.getId())) {
                requestsByRequester.add(request);
            }
        }
        onSuccessListener.onSuccess(requestsByRequester);
    }

    @Override
    public void deleteRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        requests.remove(request.getId());
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void storePhotograph(Photograph photograph, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(photographs, photograph);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrievePhotograph(EntityId id, OnSuccessListener<Photograph> onSuccessListener, OnFailureListener onFailureListener) {
        onSuccessListener.onSuccess(photographs.get(id));
    }

    @Override
    public void deletePhotograph(Photograph photograph, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        photographs.remove(photograph.getId());
        onSuccessListener.onSuccess(null);
    }

    private <T extends FirestoreIndexable> void storeEntity(Map<EntityId, T> map, T entity) {
        if (entity.getId() != null) {
            map.put(entity.getId(), entity);
        } else {
            map.put(new EntityId(), entity);
        }
    }
}
