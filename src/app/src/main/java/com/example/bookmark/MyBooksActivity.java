package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This activity shows a user a list of their books.
 * They can select a book to see and edit the details of a book.
 * They can also add a book from here
 *
 * @author Mitch Adam.
 */
public class MyBooksActivity extends ListingBooksActivity {
    FloatingActionButton addBookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add the add book button
        addBookBtn = findViewById(R.id.listing_books_action_btn);
        addBookBtn.setVisibility(View.VISIBLE);
        addBookBtn.setOnClickListener((view -> goToAddBook()));
    }

    /**
     * Returns the title that is to be used for this activity.
     *
     * @return String
     */
    @Override
    protected String getActivityTitle() {
        return "My Books";
    }

    /**
     * Returns whether the the owner field of each Book listed in the
     * visibleBooksListView for this activity should be visible.
     *
     * @return boolean
     */
    @Override
    protected boolean getBookOwnerVisibility() {
        return false;
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

    @Override
    protected void getRelevantBooks() {
        OnFailureListener onFailureListener = e -> DialogUtil.showErrorDialog(this, e);
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(
            username,
            user -> {
                this.user = user;
                StorageServiceProvider.getStorageService().retrieveBooksByOwner(
                    user,
                    books -> {
                        updateBookList(books);
                    },
                    onFailureListener
                );
            },
            onFailureListener
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
        return MyBooksActivity.this;
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
        return MyBookDetailsActivity.class;
    }


    /**
     * Start add book intent
     */
    private void goToAddBook() {
        Intent intent = new Intent(MyBooksActivity.this, AddBookActivity.class);
        startActivity(intent);
    }
}
