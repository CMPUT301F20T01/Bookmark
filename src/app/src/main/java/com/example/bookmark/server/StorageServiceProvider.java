package com.example.bookmark.server;

import android.util.Log;

/**
 * A provider for the app's storage service.
 *
 * @author Kyle Hennig.
 */
public class StorageServiceProvider {
    private static final String TAG = "StorageServiceProvider";

    private static StorageService storageService;

    /**
     * Gets the storage service.
     *
     * @return The storage service.
     */
    public static StorageService getStorageService() {
        if (storageService == null) {
            Log.d(TAG, "Using default storage service FirebaseStorageService.");
            storageService = new FirebaseStorageService();
        }
        return storageService;
    }

    /**
     * Sets the storage service.
     *
     * @param storageService The storage service to use.
     * @throws RuntimeException If the storage service has already been set.
     */
    public static void setStorageService(StorageService storageService) {
        if (!StorageServiceProvider.storageService.getClass().equals(storageService.getClass())) {
            throw new RuntimeException("You cannot change the type of the storage service!");
        }
        StorageServiceProvider.storageService = storageService;
    }
}
