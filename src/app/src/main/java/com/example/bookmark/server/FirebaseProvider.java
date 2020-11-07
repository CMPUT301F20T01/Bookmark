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
    private enum Collection {
        USERS("users"),
        BOOKS("books"),
        REQUESTS("requests");

        private final String name;

        Collection(String name) {
            this.name = name;
        }
    }

    private static final String TAG = "FirebaseProvider";
    private static final FirebaseProvider instance = new FirebaseProvider();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseProvider() {
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
     * Stores the specified user to Firebase.
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
     * @param username          The user's username.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveUserByUsername(String username, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveEntity(Collection.USERS, username, User::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Stores the specified book to Firebase.
     *
     * @param book              The book.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void storeBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(Collection.BOOKS, book, onSuccessListener, onFailureListener);
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
     * Stores the specified request to Firebase.
     *
     * @param request           The request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void storeRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        storeEntity(Collection.REQUESTS, request, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves the list of requests for a book from Firebase.
     *
     * @param book              The book the request was for.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveRequestsByBook(Book book, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveEntitiesMatching(Collection.REQUESTS, query -> query.whereEqualTo("book", book.getId()), Request::fromFirestoreDocument, onSuccessListener, onFailureListener);
    }

    /**
     * Retrieves the list of requests made by a requester from Firebase.
     *
     * @param requester         The user who made the request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveRequestsByRequester(User requester, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        retrieveEntitiesMatching(Collection.REQUESTS, query -> query.whereEqualTo("user", requester.getId()), Request::fromFirestoreDocument, onSuccessListener, onFailureListener);
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

    private void storeEntity(Collection collection, FirestoreSerializable entity, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection(collection.name)
            .document(entity.getId())
            .set(entity.toFirestoreDocument())
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, String.format("Stored %s with id %s to collection %s.", entity.getClass().getName().toLowerCase(), entity.getId(), collection.name));
                onSuccessListener.onSuccess(aVoid);
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, String.format("Error storing %s with id %s to collection %s.: ", entity.getClass().getName().toLowerCase(), entity.getId(), collection.name), e);
                onFailureListener.onFailure(e);
            });
    }

    private <T> void retrieveEntity(
        Collection collection,
        String id,
        Function<Map<String, Object>, T> fromFirestoreDocument,
        OnSuccessListener<T> onSuccessListener,
        OnFailureListener onFailureListener
    ) {
        db.collection(collection.name)
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

    private <T> void retrieveEntities(
        Collection collection,
        Function<Map<String, Object>, T> fromFirestoreDocument,
        OnSuccessListener<List<T>> onSuccessListener,
        OnFailureListener onFailureListener
    ) {
        db.collection(collection.name)
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

    private <T> void retrieveEntitiesMatching(
        Collection collection,
        Function<Query, Query> conditions,
        Function<Map<String, Object>, T> fromFirestoreDocument,
        OnSuccessListener<List<T>> onSuccessListener,
        OnFailureListener onFailureListener
    ) {
        conditions.apply(db.collection(collection.name))
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

    public void deleteEntity(Collection collection, String id, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection(collection.name)
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
