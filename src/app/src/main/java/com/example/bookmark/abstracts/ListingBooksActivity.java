package com.example.bookmark.abstracts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bookmark.NavigationDrawerActivity;
import com.example.bookmark.R;
import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.FilterDialogFragment;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Abstract class for activities that list books - some use cases include
 * viewing search results (ExploreActivity), viewing books the user is
 * currently borrowing (BorrowedActivity), and viewing books the user has
 * currently requested. Child classes must implement the abstract methods
 * getActivityTitle(), getBookOwnerVisibility(), getBookStatusVisibility(),
 * getBooks(), setContext(), and setIntentDestination().
 *
 * @author Ryan Kortbeek.
 */
public abstract class ListingBooksActivity extends NavigationDrawerActivity
        implements FilterDialogFragment.FilterDialogListener{
    private static final String FILTER_FRAGMENT_TAG = "FilterFragment";
    public static final String USER = "com.example.bookmark.USER";
    public static final String EXTRA_BOOK = "com.example.bookmark.BOOK";

    // All books that match the broad restrictions of the implementing
    // activity (i.e. for BorrowedActivity this would pertain to all books
    // that are currently borrowed by the current user)
    protected final List<Book> relevantBooks = new ArrayList<>();
    // All books that match the search text (if the search text is empty then
    // the contents of visibleBooks will be equal to the contents of
    // relevantBooks
    protected List<Book> visibleBooks = new ArrayList<>();
    protected BookList visibleBooksAdapter;
    protected ListView visibleBooksListView;

    protected TextInputLayout searchBarLayout;
    protected TextInputEditText searchBar;

    protected User user = null;

    private boolean[] statusFilterEnabled = new boolean[Book.Status.values().length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_books);
        searchBarLayout = findViewById(R.id.search_bar_textInputLayout);
        // Hides the search bar to start
        searchBarLayout.setVisibility(View.GONE);
        searchBar = (TextInputEditText) searchBarLayout.getEditText();
        visibleBooksListView = findViewById(R.id.visible_books_listview);

        // Initialize status filter to include all statuses (true)
        Arrays.fill(statusFilterEnabled, true);

        // Gets the logged in user
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(
            username,
            u -> user = u,
            e -> DialogUtil.showErrorDialog(this, e)
        );

        getSupportActionBar().setTitle(getActivityTitle());

        visibleBooksAdapter = new BookList(this, visibleBooks,
            getBookOwnerVisibility(), getBookStatusVisibility());
        visibleBooksListView.setAdapter(visibleBooksAdapter);

        visibleBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Passes the selected book to the specified intent destination
                Intent intent = new Intent(getPackageContext(),
                    getIntentDestination());
                intent.putExtra(USER, user);
                intent.putExtra(EXTRA_BOOK, visibleBooks.get(i));
                startActivity(intent);
            }
        });

        // Adds an addTextChangedListener to the search bar which allows the
        // user to filter through the listed books and the results to be
        // updated every time the search text is changed
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchString = charSequence.toString();
                // Updates the search results every time the search text is
                // changed
                updateSearchResults(searchString.split(" "));
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
                onSearchSelected();
                break;
            case R.id.menu_filter_search_filter_btn:
                FilterDialogFragment.newInstance(statusFilterEnabled)
                    .show(getSupportFragmentManager(), FILTER_FRAGMENT_TAG);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    /**
     * Updates the visibility of the search bar based on its current
     * visibility. If the search bar is currently VISIBLE then it will be set
     * to have a visibility of GONE. If the search bar is currently GONE then
     * it will be set to have a visibility of VISIBLE. Setting the visibility
     * to GONE means that it does not take up any space for layout purposes
     * (it disappears and the visible books ListView takes over that space).
     */
    private void onSearchSelected() {
        if (searchBarLayout.getVisibility() == View.VISIBLE) {
            searchBarLayout.setVisibility(View.GONE);
        } else {
            searchBarLayout.setVisibility(View.VISIBLE);
        }
    }

    public void onFilterUpdate(boolean[] statusFilterEnabled) {
        this.statusFilterEnabled = statusFilterEnabled;

        // if all are true, just send null constraint

        ArrayList<String> enabledFilterStrings = new ArrayList<>();
        Book.Status[] statusEnums = Book.Status.values();
        for (int i = 0; i < statusFilterEnabled.length; i++) {
            if (statusFilterEnabled[i]) {
                enabledFilterStrings.add(statusEnums[i].toString());
            }
         }
        String constraint = BookList.STATUS_FILTER_OP
            + TextUtils.join(BookList.FILTER_OP_DELIM, enabledFilterStrings);
        visibleBooksAdapter.getFilter().filter(constraint);
    }

    /**
     * Called every time the text in the search bar is changed. Finds all
     * books from relevantBooks that contains all of the keywords in the
     * passed string array, keywords, and displays them. String comparisons are
     * case-insensitive.
     *
     * @param keywords keywords to search
     */
    private void updateSearchResults(String[] keywords) {
        boolean match;
        visibleBooks.clear();
        if (keywords.length == 0) {
            visibleBooks.addAll(relevantBooks);
        } else {
            for (Book book : relevantBooks) {
                match = true;
                for (String keyword : keywords) {
                    if (!book.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    visibleBooks.add(book);
                }
            }
        }
        visibleBooksAdapter.notifyDataSetChanged();
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
     * This method must return the desired title of the activity. This title
     * will be displayed in the top left corner of the options menu.
     *
     * @return String
     */
    protected abstract String getActivityTitle();

    /**
     * This method must return a boolean value corresponding to whether the
     * owner of each listed book should be displayed.
     *
     * @return boolean
     */
    protected abstract boolean getBookOwnerVisibility();

    /**
     * This method must return a boolean value corresponding to whether the
     * status of each listed book should be displayed.
     *
     * @return boolean
     */
    protected abstract boolean getBookStatusVisibility();

    /**
     * Must get all books that match the broad restrictions of the implementing
     * activity (i.e. for BorrowedActivity this would pertain to all books
     * that are currently borrowed by the current user). Must clear the
     * relevantBooks ArrayList and then add all these matching books to
     * relevantBooks. These books should then be copied into the
     * visibleBooks ArrayList and notifyDataSetChanged should be called on the
     * visibleBooksAdapter.
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
