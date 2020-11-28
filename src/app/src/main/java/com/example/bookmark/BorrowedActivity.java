package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.SearchDialogFragment;
import com.example.bookmark.models.Book;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a user a list of books that they are currently
 * borrowing. They can select a book which takes them to the
 * BorrowedBookDetailsActivity where they can see the books details and
 * return the book.
 * TODO what else do we want here?
 *
 * @author Ryan Kortbeek.
 */
public class BorrowedActivity extends ListingBooksActivity {

    protected void setActivityTitle() {
        activityTitle = "Borrowed";
    }

    protected void setBookOwnerAndStatusVisibility() {
        showOwner = true;
        showStatus = false;
    }

    protected void setIntentStartingPoint() {
        intentStartingPoint = this;
    }

    protected void setIntentDestination() {
        intentDestination = BorrowedBookDetailsActivity.class;
    }

    protected void getBooks() {
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username, user -> {
            StorageServiceProvider.getStorageService().retrieveBooksByRequester(user,
                books -> {
                    relevantBooks.clear();
                    visibleBooks.clear();
                    for (Book book : books) {
                        if (book.getStatus() == Book.Status.BORROWED) {
                            relevantBooks.add(book);
                        }
                    }
                    visibleBooks.addAll(relevantBooks);
                    visibleBooksAdapter.notifyDataSetChanged();
                }, e -> {
                    DialogUtil.showErrorDialog(this, e);
                });
        }, e -> {
            DialogUtil.showErrorDialog(this, e);
        });
    }
}
