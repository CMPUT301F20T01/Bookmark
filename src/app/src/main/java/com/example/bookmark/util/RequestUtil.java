package com.example.bookmark.util;

import android.content.Context;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.StorageServiceProvider;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This class implements utility functions related to managing requests
 *
 * @author Nayan Prakash
 */
public class RequestUtil {
    public static Request retrieveRequestsOnBookByStatus(Book book, Request.Status status, Context context) {
        AtomicReference<Request> r = null;
        StorageServiceProvider.getStorageService().retrieveRequestsByBook(
            book,
            requests -> {
                for (Request request: requests) {
                    if (request.getStatus().equals(status)) {
                        r.set(request);
                        break;
                    }
                }
            },
            e -> DialogUtil.showErrorDialog(context, e)
        );
        return r.get();
    }
}
