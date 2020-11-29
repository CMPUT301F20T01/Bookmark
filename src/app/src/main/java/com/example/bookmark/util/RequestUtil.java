package com.example.bookmark.util;

import android.content.Context;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.StorageServiceProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Semaphore;

/**
 * This class implements utility functions related to managing requests
 *
 * @author Nayan Prakash
 */
public class RequestUtil {
    public static void retrieveRequestsOnBookByStatus(
        Book book,
        Request.Status status,
        OnSuccessListener<Request> onSuccessListener,
        OnFailureListener onFailureListener
    ) {
        StorageServiceProvider.getStorageService().retrieveRequestsByBook(
            book,
            requestList -> {
                for (Request r: requestList) {
                    if (r.getStatus().equals(status)) {
                        onSuccessListener.onSuccess(r);
                        return;
                    }
                }
            },
            onFailureListener
        );
    }
}
