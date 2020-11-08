package com.example.bookmark.server;

import android.util.Log;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A singleton class the provides access to our Firestore database.
 *
 * @author Kyle Hennig.
 */
public class FirebaseProvider {
    private static class Collection {
        private static final String USERS = "users";
        private static final String BOOKS = "books";
        private static final String REQUESTS = "requests";
    }

    private static final FirebaseProvider instance = new FirebaseProvider();
    private static final String TAG = "FirebaseProvider";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected FirebaseProvider() {
        // Singleton class.
    }

    /**
     * Gets a singleton instance of the FirebaseProvider.
     *
     * @return The instance.
     */
    public static FirebaseProvider getInstance() {
        return instance;
    }

    /**
     * Stores a user to Firebase.
     *
     * @param user              The user.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void storeUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(Collection.USERS, user, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves a user from Firebase.
     *
     * @param username          The username of the user.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveUserByUsername(String username, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveEntity(Collection.USERS, username, User::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Stores a book to Firebase.
     *
     * @param book              The book.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void storeBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(Collection.BOOKS, book, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves a book from Firebase.
     *
     * @param owner             The owner of the book.
     * @param isbn              The ISBN of the book.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveBook(User owner, String isbn, OnSuccessListener<Book> onSuccessListener, OnFailureListener onFailureListener) {
        String id = String.format("%s:%s", owner.getId(), isbn);
        retrieveEntity(Collection.BOOKS, id, Book::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves all the books from Firebase.
     *
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveBooks(OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveEntities(Collection.BOOKS, Book::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves the books owned by a user.
     *
     * @param owner             The owner of the books.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveBooksByOwner(User owner, OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveEntitiesMatching(Collection.BOOKS, query -> query.whereEqualTo("ownerId", owner.getId()), Book::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves the books requested by a user.
     *
     * @param requester         The requester of the books.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveBooksByRequester(User requester, OnSuccessListener<List<Book>> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveRequestsByRequester(requester, requests -> {
            List<String> bookIds = new ArrayList<>();
            for (Request request : requests) {
                bookIds.add(request.getBookId());
            }
            retrieveBooks(books -> {
                List<Book> booksByRequester = new ArrayList<>();
                for (Book book : books) {
                    if (bookIds.contains(book.getId())) {
                        booksByRequester.add(book);
                    }
                }
                onSuccessListener.onSuccess(booksByRequester);
            }, onFailureListener);
        }, onFailureListener);
    }

    /**
     * Stores a request to Firebase.
     *
     * @param request           The request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void storeRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(Collection.REQUESTS, request, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves a request from Firebase.
     *
     * @param book              The book the request was for.
     * @param requester         The user who made the request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveRequest(Book book, User requester, OnSuccessListener<Request> onSuccessListener, OnFailureListener onFailureListener) {
        String id = String.format("%s:%s", book.getId(), requester.getId());
        retrieveEntity(Collection.REQUESTS, id, Request::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves the requests for a book from Firebase.
     *
     * @param book              The book the request was for.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveRequestsByBook(Book book, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveEntitiesMatching(Collection.REQUESTS, query -> query.whereEqualTo("book", book.getId()), Request::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves the requests made by a requester from Firebase.
     *
     * @param requester         The user who made the request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveRequestsByRequester(User requester, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveEntitiesMatching(Collection.REQUESTS, query -> query.whereEqualTo("requester", requester.getId()), Request::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Deletes a request from Firebase.
     *
     * @param request           The request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void deleteRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        deleteEntity(Collection.REQUESTS, request.getId(), onSuccessListener, onFailureListener);
    }

    protected void storeEntity(String collection, FirestoreIndexable entity, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection(collection)
            .document(entity.getId())
            .set(entity.toFirestoreDocument())
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, String.format("Stored %s with id %s to collection %s.", entity.getClass().getName().toLowerCase(), entity.getId(), collection));
                onSuccessListener.onSuccess(aVoid);
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, String.format("Error storing %s with id %s to collection %s.: ", entity.getClass().getName().toLowerCase(), entity.getId(), collection), e);
                onFailureListener.onFailure(e);
            });
    }

    protected <T> void retrieveEntity(String collection, String id, Function<Map<String, Object>, T> fromFirestoreDocument, OnSuccessListener<T> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection(collection)
            .document(id)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, String.format("Retrieved entity with id %s from collection %s.", id, collection));
                    onSuccessListener.onSuccess(fromFirestoreDocument.apply(documentSnapshot.getData()));
                } else {
                    Log.d(TAG, String.format("No entity with id %s exists in collection %s.", id, collection));
                    onSuccessListener.onSuccess(null);
                }
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, String.format("Error retrieving entity with id %s from collection %s.", id, collection));
                onFailureListener.onFailure(e);
            });
    }

    protected <T> void retrieveEntities(String collection, Function<Map<String, Object>, T> fromFirestoreDocument, OnSuccessListener<List<T>> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection(collection)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<T> entities = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    entities.add(fromFirestoreDocument.apply(queryDocumentSnapshot.getData()));
                }
                Log.d(TAG, String.format("Retrieved entities from collection %s", collection));
                onSuccessListener.onSuccess(entities);
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, String.format("Error retrieving entities from collection %s.", collection));
                onFailureListener.onFailure(e);
            });
    }

    protected <T> void retrieveEntitiesMatching(String collection, Function<Query, Query> conditions, Function<Map<String, Object>, T> fromFirestoreDocument, OnSuccessListener<List<T>> onSuccessListener, OnFailureListener onFailureListener) {
        conditions.apply(db.collection(collection))
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<T> entities = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    entities.add(fromFirestoreDocument.apply(queryDocumentSnapshot.getData()));
                }
                Log.d(TAG, String.format("Retrieved entities from collection %s matching conditions.", collection));
                onSuccessListener.onSuccess(entities);
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, String.format("Error retrieving entities from collection %s matching conditions.", collection));
                onFailureListener.onFailure(e);
            });
    }

    protected void deleteEntity(String collection, String id, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection(collection)
            .document(id)
            .delete()
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, String.format("Deleted entity %s from collection %s.", id, collection));
                onSuccessListener.onSuccess(aVoid);
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, String.format("Error deleting entity %s from collection %s: ", id, collection), e);
                onFailureListener.onFailure(e);
            });
    }
}
