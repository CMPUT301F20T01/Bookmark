package com.example.bookmark.server;

import android.util.Log;

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

    public FirebaseProvider getInstance() {
        if (instance == null) {
            instance = new FirebaseProvider();
        }
        return instance;
    }

    public void createUser(User user) {
        db.collection("users")
            .document(user.getUsername())
            .set(user.toFirestoreDocument())
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "User created successfully.");
            })
            .addOnFailureListener(e -> Log.w(TAG, "Error creating user.", e));
    }

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
}
