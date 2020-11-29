package com.example.bookmark;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;

/**
 * This activity shows a user a list of books that they have pending requests
 * for. They can select a book which takes them to the
 * RequestedBookDetailsActivity where they can see the books details.
 *
 * @author Ryan Kortbeek.
 */
public class PendingRequestsActivity extends ListingBooksActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the menu with the search icon. Override this
     * method if different menu icons are desired.
     *
     * @param menu menu to inflate
     * @return true (shows the inflated option menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu with the filter and search icons
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    /**
     * Returns the title that is to be used for this activity.
     *
     * @return String
     */
    @Override
    protected String getActivityTitle() {
        return "Pending Requests";
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
     * Gets all books from the firestore database that are requested by the
     * current user and sets the values of visibleBooks and relevantBooks
     * accordingly.
     */
    @Override
    protected void getBooks() {
        if (user == null) {
            String username = UserUtil.getLoggedInUser(this);
            StorageServiceProvider.getStorageService().retrieveUserByUsername(
                username,
                user1 -> {
                    StorageServiceProvider.getStorageService().retrieveBooksByRequester(
                        user1,
                        books -> {
                            visibleBooks.clear();
                            relevantBooks.clear();
                            for (Book book : books) {
                                if (book.getStatus() == Book.Status.REQUESTED) {
                                    relevantBooks.add(book);
                                }
                            }
                            visibleBooks.addAll(relevantBooks);
                            visibleBooksAdapter.notifyDataSetChanged();
                        }, e -> {
                            DialogUtil.showErrorDialog(this, e);
                        }
                    );
                }, e -> {
                    DialogUtil.showErrorDialog(this, e);
                }
            );
        } else {
            StorageServiceProvider.getStorageService().retrieveBooksByRequester(
                user,
                books -> {
                    visibleBooks.clear();
                    relevantBooks.clear();
                    for (Book book : books) {
                        if (book.getStatus() == Book.Status.REQUESTED) {
                            relevantBooks.add(book);
                        }
                    }
                    visibleBooks.addAll(relevantBooks);
                    visibleBooksAdapter.notifyDataSetChanged();
                }, e -> {
                    DialogUtil.showErrorDialog(this, e);
                }
            );
        }
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
        return PendingRequestsActivity.this;
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
        return RequestedBookDetailsActivity.class;
    }
}
