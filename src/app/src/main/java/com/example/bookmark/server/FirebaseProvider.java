package com.example.bookmark.server;

import android.util.Log;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

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
     * Saves the specified user to Firebase.
     *
     * @param user              The user.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void saveUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
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
     * Gets a user from Firebase.
     *
     * @param username          The user's username.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void getUserByUsername(String username, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {
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
                Log.d(TAG, "getUserByUsername failed.", e);
                onFailureListener.onFailure(e);
            });
    }

    /**
     * Saves the specified book to Firebase.
     *
     * @param book              The book.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void saveBook(Book book, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
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
     * Gets a book from Firebase.
     *
     * @param isbn              The book's ISBN.
     * @param onSuccessListener Callback to run on success.
     * @param onFailureListener Callback to run on failure.
     */
    public void getBookByIsbn(String isbn, OnSuccessListener<Book> onSuccessListener, OnFailureListener onFailureListener) {
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
                Log.d(TAG, "getBookByIsbn failed.", e);
                onFailureListener.onFailure(e);
            });
    }
}
