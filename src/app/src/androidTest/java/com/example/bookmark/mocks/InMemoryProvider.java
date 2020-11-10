package com.example.bookmark.mocks;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.bookmark.mocks.MockModels.mockBook1;
import static com.example.bookmark.mocks.MockModels.mockBook2;
import static com.example.bookmark.mocks.MockModels.mockOwner;
import static com.example.bookmark.mocks.MockModels.mockRequest1;
import static com.example.bookmark.mocks.MockModels.mockRequest2;
import static com.example.bookmark.mocks.MockModels.mockRequester;

/**
 * An implementation of StorageProvider that stores all of the app's data in memory.
 * Useful for intent testing and other types of tests that do not need a server connection.
 *
 * @author Kyle Hennig.
 */
public class InMemoryProvider implements StorageProvider {
    private static final StorageProvider instance = new InMemoryProvider();

    private final List<User> users = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();
    private final List<Request> requests = new ArrayList<>();

    public static StorageProvider getInstance() {
        return instance;
    }

    private InMemoryProvider() {
        users.add(mockOwner());
        users.add(mockRequester());
        books.add(mockBook1());
        books.add(mockBook2());
        requests.add(mockRequest1());
        requests.add(mockRequest2());
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
                return;
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
    public void storeRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        requests.add(request);
        onSuccessListener.onSuccess(null);
    }

    @Override
    public void retrieveRequest(Book book, User requester, OnSuccessListener<Request> onSuccessListener, OnFailureListener onFailureListener) {
        for (Request request : requests) {
            if (request.getBookId().equals(book.getId()) && request.getRequesterId().equals(request.getId())) {
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
