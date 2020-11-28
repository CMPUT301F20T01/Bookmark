package com.example.bookmark;

import android.view.Menu;
import android.view.MenuInflater;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;

/**
 * This activity shows a user a list of books that match the keyword(s) from
 * the search they have just performed. They can select a book which takes them
 * to the ExploreBookDetailsActivity where they can see the books details.
 * TODO what else do we want here?
 *
 * @author Ryan Kortbeek.
 */
public class ExploreActivity extends ListingBooksActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu with the filter and search icons
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_search, menu);
        return true;
    }

    protected void setActivityTitle() {
        activityTitle = "Explore";
    }

    protected void setBookOwnerAndStatusVisibility() {
        showOwner = true;
        showStatus = true;
    }

    protected void setIntentStartingPoint() {
        intentStartingPoint = this;
    }

    protected void setIntentDestination() {
        intentDestination = ExploreBookDetailsActivity.class;
    }

    protected void getBooks() {
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username, user -> {
            StorageServiceProvider.getStorageService().retrieveBooks(books -> {
                relevantBooks.clear();
                visibleBooks.clear();
                for (Book book : books) {
                    if ((book.getOwnerId() != user.getId()) &&
                        (book.getStatus() != Book.Status.BORROWED) &&
                        (book.getStatus() != Book.Status.ACCEPTED)) {
                        relevantBooks.add(book);
                    }
                }
                visibleBooks.addAll(relevantBooks);
            }, e -> {
                DialogUtil.showErrorDialog(this, e);
            });
        }, e -> {
            DialogUtil.showErrorDialog(this, e);
        });
        System.out.println(visibleBooks);
        visibleBooksAdapter.notifyDataSetChanged();
    }
}
