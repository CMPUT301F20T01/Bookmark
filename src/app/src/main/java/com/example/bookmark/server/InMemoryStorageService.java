package com.example.bookmark.server;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of StorageProvider that stores all of the app's data in memory.
 * Useful for intent testing and other types of tests that do not need a server connection.
 *
 * @author Kyle Hennig.
 */
public class InMemoryStorageService implements StorageService {
    private final List<User> users;
    private final List<Book> books;
    private final List<Request> requests;

    /**
     * Creates an InMemoryStorageService.
     */
    public InMemoryStorageService() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Creates an InMemoryStorageService.
     *
     * @param users    The users that should exist to start.
     * @param books    The books that should exist to start.
     * @param requests The requests that should exist to start.
     */
    public InMemoryStorageService(List<User> users, List<Book> books, List<Request> requests) {
        this.users = users;
        this.books = books;
        this.requests = requests;
    }

    @Override
    public void storeUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        users.add(user);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveUserByUsername(String username, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                onSuccessListener.onSuccess(user);
                return;
            }
        }
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void storeBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        books.add(book);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveBook(User owner, String isbn, OnSuccessListener<Book> onSuccessListener, OnFailureListener onFailureListener) {
        for (Book book : books) {
            if (book.getOwnerId().equals(owner.getId()) && book.getIsbn().equals(isbn)) {
                onSuccessListener.onSuccess(book);
                return;
            }
        }
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveBooks(OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        onSuccessListener.onSuccess(books);
    }

    @Override
    public void retrieveBooksByOwner(User owner, OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        List<Book> booksByOwner = new ArrayList<>();
        for (Book book : books) {
            if (book.getOwnerId().equals(owner.getId())) {
                booksByOwner.add(book);
            }
        }
        onSuccessListener.onSuccess(booksByOwner);
    }

    @Override
    public void retrieveBooksByRequester(User requester, OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        List<String> bookIds = new ArrayList<>();
        for (Request request : requests) {
            bookIds.add(request.getBookId());
        }
        List<Book> booksByRequester = new ArrayList<>();
        for (Book book : books) {
            if (bookIds.contains(book.getId())) {
                booksByRequester.add(book);
            }
        }
        onSuccessListener.onSuccess(booksByRequester);
    }

    @Override
    public void deleteBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        books.remove(book);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void storeRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        requests.add(request);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveRequest(Book book, User requester, OnSuccessListener<Request> onSuccessListener, OnFailureListener onFailureListener) {
        for (Request request : requests) {
            if (request.getBookId().equals(book.getId()) && request.getRequesterId().equals(requester.getId())) {
                onSuccessListener.onSuccess(request);
                return;
            }
        }
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveRequestsByBook(Book book, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        List<Request> requestsByBook = new ArrayList<>();
        for (Request request : requests) {
            if (request.getBookId().equals(book.getId())) {
                requestsByBook.add(request);
            }
        }
        onSuccessListener.onSuccess(requestsByBook);
    }

    @Override
    public void retrieveRequestsByRequester(User requester, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        List<Request> requestsByRequester = new ArrayList<>();
        for (Request request : requests) {
            if (request.getRequesterId().equals(requester.getId())) {
                requestsByRequester.add(request);
            }
        }
        onSuccessListener.onSuccess(requestsByRequester);
    }

    @Override
    public void deleteRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        requests.remove(request);
        onSuccessListener.onSuccess(null);
    }
}
