package com.example.bookmark.server;

import android.util.Log;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton class the provides access to our Firestore database.
 *
 * @author Kyle Hennig.
 */
public class FirebaseProvider {
    private static final String TAG = "FirebaseProvider";
    private static FirebaseProvider instance;

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
        if (instance == null) {
            instance = new FirebaseProvider();
        }
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
        db.collection("users")
            .document(user.getUsername())
            .set(user.toFirestoreDocument())
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "User created successfully.");
                onSuccessListener.onSuccess(aVoid);
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, "Error creating user.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Retrieves a user from Firebase.
     *
     * @param username          The user's username.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveUserByUsername(String username, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("users")
            .document(username)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, String.format("Retrieved user %s.", username));
                    onSuccessListener.onSuccess(User.fromFirestoreDocument(documentSnapshot.getData()));
                } else {
                    Log.d(TAG, String.format("No user with username %s found.", username));
                    onSuccessListener.onSuccess(null);
                }
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "retrieveUserByUsername failed.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Stores the specified book to Firebase.
     *
     * @param book              The book.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void storeBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("books")
            .document(book.getIsbn())
            .set(book.toFirestoreDocument())
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Book created successfully.");
                onSuccessListener.onSuccess(aVoid);
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, "Error creating book.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Retrieves a book from Firebase.
     *
     * @param isbn              The book's ISBN.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveBookByIsbn(String isbn, OnSuccessListener<Book> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("books")
            .document(isbn)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, String.format("Retrieved book %s.", isbn));
                    onSuccessListener.onSuccess(Book.fromFirestoreDocument(documentSnapshot.getData()));
                } else {
                    Log.d(TAG, String.format("No book with ISBN %s found.", isbn));
                    onSuccessListener.onSuccess(null);
                }
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "retrieveBookByIsbn failed.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Stores the specified request to Firebase.
     *
     * @param request           The request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void storeRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("requests")
            .document(String.format("%s:%s", request.getRequester(), request.getBook()))
            .set(request.toFirestoreDocument())
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Request created successfully.");
                onSuccessListener.onSuccess(aVoid);
            })
            .addOnFailureListener(e -> {
                Log.w(TAG, "Error creating request.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Retrieves a request from Firebase.
     *
     * @param user              The user who made the request.
     * @param book              The book the request was for.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveRequestByUserAndBook(User user, Book book, OnSuccessListener<Request> onSuccessListener, OnFailureListener onFailureListener) {
        String requestId = String.format("%s:%s", user.getUsername(), book.getIsbn());
        db.collection("requests")
            .document(requestId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, String.format("Retrieved request %s.", requestId));
                    onSuccessListener.onSuccess(Request.fromFirestoreDocument(documentSnapshot.getData()));
                } else {
                    Log.d(TAG, String.format("No request with user %s and book %s found.", user.getUsername(), book.getIsbn()));
                    onSuccessListener.onSuccess(null);
                }
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "retrieveRequestByUserAndBook failed.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Retrieves the list of requests made by a user from Firebase.
     *
     * @param user              The user who made the request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveRequestsByUser(User user, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("requests")
            .whereEqualTo("requester", user.getUsername())
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Request> requests = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    requests.add(Request.fromFirestoreDocument(queryDocumentSnapshot.getData()));
                }
                Log.d(TAG, String.format("Retrieved requests made by user %s.", user.getUsername()));
                onSuccessListener.onSuccess(requests);
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "retrieveRequestByUser failed.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Retrieves the list of requests for a book from Firebase.
     *
     * @param book              The book the request was for.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void retrieveRequestsByBook(Book book, OnSuccessListener<List<Request>> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("requests")
            .whereEqualTo("book", book.getIsbn())
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Request> requests = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    requests.add(Request.fromFirestoreDocument(queryDocumentSnapshot.getData()));
                }
                Log.d(TAG, String.format("Retrieved requests made for book %s.", book.getIsbn()));
                onSuccessListener.onSuccess(requests);
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "retrieveRequestByBook failed.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Deletes a request from Firebase.
     *
     * @param request           The request.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void deleteRequest(Request request, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        String requestId = String.format("%s:%s", request.getRequester(), request.getBook());
        db.collection("requests")
            .document(requestId)
            .delete()
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, String.format("Successfully deleted request %s.", requestId));
                onSuccessListener.onSuccess(aVoid);
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "deleteRequestByUserAndBook failed.", e);
                onFailureListener.onFailure(e);
            });
    }
}
