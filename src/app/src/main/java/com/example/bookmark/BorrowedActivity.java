package com.example.bookmark;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a user a list of books that they are currently
 * borrowing. They can select a book which takes them to the
 * BorrowerBookDetailsActivity where they can see the books details and
 * return the book.
 *
 * @author Ryan Kortbeek.
 */
public class BorrowedActivity extends ListingBooksActivity {
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
        setSearchMenuItem(menu.findItem(R.id.menu_filter_search_search_btn));
        return true;
    }

    /**
     * Returns the title that is to be used for this activity.
     *
     * @return String
     */
    @Override
    protected String getActivityTitle() {
        return "Borrowed";
    }

    /**
     * Gets all books from the firestore database that are borrowed by the
     * current user and sets the values of visibleBooks and relevantBooks
     * accordingly.
     */
    @Override
    protected void getRelevantBooks() {
        StorageServiceProvider.getStorageService().retrieveBooksByRequester(
            user,
            books -> {
                List<Book> relevantBooks = new ArrayList<>();
                for (Book book : books) {
                    if (book.getStatus() == Book.Status.BORROWED) {
                        relevantBooks.add(book);
                    }
                }
                updateBookList(relevantBooks);
            }, e -> {
                DialogUtil.showErrorDialog(this, e);
            }
        );
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
        return BorrowedActivity.this;
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
        return BorrowerBookDetailsActivity.class;
    }
}
