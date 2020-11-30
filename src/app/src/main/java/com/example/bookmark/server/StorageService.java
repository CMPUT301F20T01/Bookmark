package com.example.bookmark.server;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.EntityId;
import com.example.bookmark.models.Photograph;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**
 * Interface for a class capable of storing and retrieving the app's data.
 *
 * @author Kyle Hennig.
 */
public interface StorageService {
    /**
     * Stores a user.
     *
     * @param user              The user.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void storeUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves a user.
     *
     * @param username          The username of the user.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrieveUserByUsername(String username, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Stores a book.
     *
     * @param book              The book.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void storeBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves a book.
     *
     * @param id                The id of the book.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrieveBook(EntityId id, OnSuccessListener<Book> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves all the books.
     *
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrieveBooks(OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves the books owned by a user.
     *
     * @param owner             The owner of the books.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrieveBooksByOwner(User owner, OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves the books requested by a user.
     *
     * @param requester         The requester of the books.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrieveBooksByRequester(User requester, OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Deletes a book.
     *
     * @param book              The book.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void deleteBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Stores a request.
     *
     * @param request           The request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void storeRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves a request.
     *
     * @param id                The id of the request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrieveRequest(EntityId id, OnSuccessListener<Request> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves the requests for a book.
     *
     * @param book              The book the request was for.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrieveRequestsByBook(Book book, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves the requests made by a requester.
     *
     * @param requester         The user who made the request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrieveRequestsByRequester(User requester, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Deletes a request.
     *
     * @param request           The request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void deleteRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Stores a photograph.
     *
     * @param photograph        The photograph.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void storePhotograph(Photograph photograph, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Retrieves a photograph.
     *
     * @param id                The id of the photograph.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void retrievePhotograph(EntityId id, OnSuccessListener<Photograph> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Deletes a photograph.
     *
     * @param photograph        The photograph.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    void deletePhotograph(Photograph photograph, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);
}
