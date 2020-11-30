package com.example.bookmark.abstracts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bookmark.NavigationDrawerActivity;
import com.example.bookmark.R;
import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.FilterDialogFragment;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for activities that list books - some use cases include
 * browsing others' books (ExploreActivity), viewing books the user is
 * currently borrowing (BorrowedActivity), and viewing books the user has
 * currently requested. Child classes must implement the abstract methods
 * getActivityTitle(), getBookOwnerVisibility(), getBookStatusVisibility(),
 * getRelevantBooks(), getPackageContext(), and getIntentDestination().
 *
 * @author Ryan Kortbeek.
 */
public abstract class ListingBooksActivity extends NavigationDrawerActivity
    implements FilterDialogFragment.FilterDialogListener {
    private static final String FILTER_FRAGMENT_TAG = "FilterFragment";
    public static final String USER = "com.example.bookmark.USER";
    public static final String EXTRA_BOOK = "com.example.bookmark.BOOK";

    private final List<Book> bookList = new ArrayList<>();
    private BookList bookListAdapter;
    private ListView booksListView;

    private TextInputLayout searchBarLayout;
    private EditText searchEditText;
    private MenuItem filterMenuItem;
    private MenuItem searchMenuItem;

    private boolean[] statusFilterEnabled = new boolean[Book.Status.values().length];
    private String statusFilterConstrainString;

    protected User user = null;
    protected Book selectedBook = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_books);
        getSupportActionBar().setTitle(getActivityTitle());

        booksListView = findViewById(R.id.books_listview);
        searchBarLayout = findViewById(R.id.search_bar_textInputLayout);
        searchEditText = searchBarLayout.getEditText();

        // Hides the search bar to start
        searchBarLayout.setVisibility(View.GONE);

        // Initialize status filter to include all statuses (true)
        Arrays.fill(statusFilterEnabled, true);
        statusFilterConstrainString = "";

        // Gets the logged in user
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(
            username,
            u -> user = u,
            e -> DialogUtil.showErrorDialog(this, e)
        );

        // Setup bookListAdapter
        bookListAdapter = new BookList(this, bookList);
        booksListView.setAdapter(bookListAdapter);

        // Setup listeners
        booksListView.setOnItemClickListener((adapterView, view, i, l) -> goToBookDetails(i));
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Drawable searchIcon = (Drawable) searchMenuItem.getIcon();
                searchIcon.mutate().setTint(ContextCompat.getColor(searchEditText.getContext(),
                    charSequence.length() == 0 ? R.color.colorWhite : R.color.colorAccent
                ));
                searchMenuItem.setIcon(searchIcon);
                updateAdapterFilter();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            getBooks();
            pullToRefresh.setRefreshing(false);
        });

    }

    /**
     * Gets the books that should be listed via getBooks(). Called either after
     * onCreate() and onStart() or when the user returns to the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Gets the books to list
        getBooks();
    }

    /**
     * Inflates the menu with the filter and search icons. Override this
     * method if different menu icons are desired.
     *
     * @param menu menu to inflate
     * @return true (shows the inflated option menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu with the filter and search icons
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_search, menu);
        filterMenuItem = menu.findItem(R.id.menu_filter_search_filter_btn);
        searchMenuItem = menu.findItem(R.id.menu_filter_search_search_btn);
        return true;
    }

    /**
     * Executes the behaviour that is desired upon selecting a menu item.
     *
     * @param item selected options menu item
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter_search_search_btn:
                // Changes visibility of search bar
                toggleSearchVisibility();
                break;
            case R.id.menu_filter_search_filter_btn:
                FilterDialogFragment.newInstance(statusFilterEnabled)
                    .show(getSupportFragmentManager(), FILTER_FRAGMENT_TAG);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    /**
     * Toggles the visibility of the search bar between VISIBLE and GONE.
     */
    private void toggleSearchVisibility() {
        if (searchBarLayout.getVisibility() == View.VISIBLE) {
            searchBarLayout.setVisibility(View.GONE);
        } else {
            searchBarLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Updates the adapter filter based on the current status filter and search text.
     */
    private void updateAdapterFilter() {
        String constraint = statusFilterConstrainString + " " + searchEditText.getText().toString();
        bookListAdapter.getFilter()
            .filter(constraint.length() == 1 ? null : constraint);
    }

    /**
     * Callback function for FilterDialogFragment. Updates the statusFilterEnabled
     * member and rebuilds the status filter constrain string.
     *
     * @param statusFilterEnabled boolean array describing the status of the filters
     */
    public void onFilterUpdate(boolean[] statusFilterEnabled) {
        this.statusFilterEnabled = statusFilterEnabled;

        ArrayList<String> enabledFilterStrings = new ArrayList<>();
        Book.Status[] statusEnums = Book.Status.values();
        for (int i = 0; i < statusFilterEnabled.length; i++) {
            if (statusFilterEnabled[i]) {
                enabledFilterStrings.add(statusEnums[i].toString());
            }
        }

        // Build statusFilterConstrainString and update icon
        Drawable filterIcon = (Drawable) filterMenuItem.getIcon();
        if (enabledFilterStrings.size() == statusFilterEnabled.length) {
            statusFilterConstrainString = "";
            filterIcon.mutate().setTint(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            statusFilterConstrainString = BookList.STATUS_FILTER_OP
                + TextUtils.join(BookList.FILTER_OP_DELIM, enabledFilterStrings);
            filterIcon.mutate().setTint(ContextCompat.getColor(this, R.color.colorAccent));
        }
        filterMenuItem.setIcon(filterIcon);
        updateAdapterFilter();
    }

    /**
     * Gets all relevant books.
     */
    private void getBooks() {
        if (user == null) {
            String username = UserUtil.getLoggedInUser(this);
            StorageServiceProvider.getStorageService().retrieveUserByUsername(
                username,
                user1 -> {
                    this.user = user1;
                    getRelevantBooks();
                }, e -> {
                    DialogUtil.showErrorDialog(this, e);
                }
            );
        } else {
            getRelevantBooks();
        }
    }

    /**
     * Replaces the current book list with the given updatedList. To be called in
     * a subclass's getRelevantBooks() function.
     *
     * @param updatedList list to replace current book list
     */
    protected void updateBookList(List<Book> updatedList) {
        bookList.clear();
        bookList.addAll(updatedList);
        bookListAdapter.notifyDataSetChanged();
    }

    /**
     * Item click handler for list view. Goes to the book details activity
     * specified by getIntentDestination().
     *
     * @param i item position in listview
     */
    private void goToBookDetails(int i) {
        // Passes the selected book to the specified intent destination
        Intent intent = new Intent(getPackageContext(),
            getIntentDestination());
        intent.putExtra(USER, user);
        intent.putExtra(EXTRA_BOOK, bookListAdapter.getItem(i));
        startActivity(intent);
    }

    /**
     * This method must return the desired title of the activity. This title
     * will be displayed in the top left corner of the options menu.
     *
     * @return String
     */
    protected abstract String getActivityTitle();

    /**
     * Must compile a list of all books that match the broad restrictions of
     * the implementing activity (i.e. for BorrowedActivity this would pertain
     * to all books that are currently borrowed by the current user) and pass
     * them back through the updateBookList() function.
     */
    protected abstract void getRelevantBooks();

    /**
     * Returns the context that is used for the starting point of the
     * intent that is created when a Book in the visibleBooksListView is
     * clicked.
     *
     * @return Context
     */
    protected abstract Context getPackageContext();

    /**
     * Returns the class that is used for the destination of the
     * intent that is created when a Book in the visibleBooksListView is
     * clicked.
     *
     * @return Class<?>
     */
    protected abstract Class<?> getIntentDestination();
}
