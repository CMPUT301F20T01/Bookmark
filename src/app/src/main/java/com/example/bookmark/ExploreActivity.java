package com.example.bookmark;

import android.content.Context;
import android.os.Bundle;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a user a list of books that match the keyword(s) from
 * the search they have just performed. They can select a book which takes them
 * to the ExploreBookDetailsActivity where they can see the books details.
 *
 * @author Ryan Kortbeek.
 */
public class ExploreActivity extends ListingBooksActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Returns the title that is to be used for this activity.
     *
     * @return String
     */
    @Override
    protected String getActivityTitle() {
        return "Explore";
    }

    /**
     * Returns whether the the owner field of each Book listed in the
     * visibleBooksListView for this activity should be visible.
     *
     * @return boolean
     */
    @Override
    protected boolean getBookOwnerVisibility() {
        return true;
    }

    /**
     * Returns whether the the status field of each Book listed in the
     * visibleBooksListView for this activity should be visible.
     *
     * @return boolean
     */
    @Override
    protected boolean getBookStatusVisibility() {
        return true;
    }

    /**
     * Gets all books from the firestore database that are not accepted or
     * borrowed and sets the values of visibleBooks and relevantBooks
     * accordingly.
     */
    @Override
    protected void getRelevantBooks() {
        StorageServiceProvider.getStorageService().retrieveBooks(books -> {
            List<Book> relevantBooks = new ArrayList<>();
            for (Book book : books) {
                if (!book.getOwnerId().equals(user.getId()) &&
                    (book.getStatus() != Book.Status.BORROWED) &&
                    (book.getStatus() != Book.Status.ACCEPTED)) {
                    relevantBooks.add(book);
                }
            }
            updateBookList(relevantBooks);
        }, e -> {
            DialogUtil.showErrorDialog(this, e);
        });
    }

    /**
     * Returns the context that is used for the starting point of the
     * intent that is created when a Book in the visibleBooksListView is
     * clicked.
     *
     * @return Context
     */
    @Override
    protected Context getPackageContext() {
        return ExploreActivity.this;
    }

    /**
     * Returns the class that is used for the destination of the
     * intent that is created when a Book in the visibleBooksListView is
     * clicked.
     *
     * @return Class<?>
     */
    @Override
    protected Class<?> getIntentDestination() {
        return ExploreBookDetailsActivity.class;
    }
}
