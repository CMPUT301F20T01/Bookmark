package com.example.bookmark.server;

/**
 * TODO: Description of class.
 * @author Kyle Hennig.
 */
public class FirebaseProvider {
    private static FirebaseProvider instance;

    private FirebaseProvider() {
        // Singleton class.
    }

    public FirebaseProvider getInstance() {
        if (instance == null) {
            instance = new FirebaseProvider();
        }
        return instance;
    }
}
