package com.example.bookmark.server;

/**
 * A service locator that allows getting and setting the current storage service.
 */
public class StorageServiceLocator {
    private static final StorageServiceLocator instance = new StorageServiceLocator();

    private StorageService storageService = new FirebaseStorageService();

    private StorageServiceLocator() {
        // Singleton class.
    }

    public static StorageServiceLocator getInstance() {
        return instance;
    }

    /**
     * Gets the storage service.
     *
     * @return The storage service.
     */
    public StorageService getStorageService() {
        return storageService;
    }

    /**
     * Sets the storage service.
     * Calling this method while the app is running may result in undefined behaviour.
     *
     * @param storageService The storage service to use.
     */
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }
}
