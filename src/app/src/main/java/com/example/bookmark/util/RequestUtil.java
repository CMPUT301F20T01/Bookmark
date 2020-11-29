package com.example.bookmark.util;

import android.content.Context;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.StorageServiceProvider;

import java.util.concurrent.Semaphore;

/**
 * This class implements utility functions related to managing requests
 *
 * @author Nayan Prakash
 */
public class RequestUtil {
    public static Request retrieveRequestsOnBookByStatus(Book book, Request.Status status, Context context) {
        Semaphore semaphore = new Semaphore(0);
        // a final single-element Request array is used to allow assignment within onSuccessListener
        final Request[] request = {null};
        StorageServiceProvider.getStorageService().retrieveRequestsByBook(
            book,
            requestList -> {
                for (Request r: requestList) {
                    if (r.getStatus().equals(status)) {
                        request[0] = r;
                        break;
                    }
                }
                semaphore.release();
            },
            e -> DialogUtil.showErrorDialog(context, e)
        );
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return request[0];
    }
}
