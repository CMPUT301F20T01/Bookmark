package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.SearchDialogFragment;
import com.example.bookmark.models.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a user a list of books that they have pending requests
 * for. They can select a book which takes them to the
 * RequestedBookDetailsActivity where they can see the books details.
 * TODO what else do we want here?
 *
 * @author Ryan Kortbeek.
 */
public class PendingRequestsActivity extends NavigationDrawerActivity
    implements SearchDialogFragment.OnFragmentInteractionListener {
    public static final String EXTRA_BOOK = "com.example.bookmark.BOOK";
    public static final String SEARCHED_KEYWORDS = "com.example.bookmark" +
        ".SEARCH";

    private List<Book> requestedBooks = new ArrayList<>();
    private BookList requestedBooksAdapter;
    private ListView requestedBooksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        getSupportActionBar().setTitle("Pending Requests");

        requestedBooksListView = findViewById(R.id.requested_books_listview);

        getRequestedBooks();
        requestedBooksAdapter = new BookList(this, requestedBooks, true, true);
        requestedBooksListView.setAdapter(requestedBooksAdapter);
        requestedBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PendingRequestsActivity.this,
                    RequestedBookDetailsActivity.class);
                intent.putExtra(EXTRA_BOOK, requestedBooks.get(i));
                startActivity(intent);
            }
        });
    }

    private void getRequestedBooks() {
        // TODO get books requested by current user - need access to current
        //  user and firebase

        // Proof of concept
        // Book b1 = new Book("Title 1", "Author 1", "1111111", "o");
        // b1.setDescription("Book 1 description");

        // requestedBooks.add(b1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu with the filter and search icons
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_search, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter_search_search_btn:
                // Opens search fragment
                new SearchDialogFragment().show(getSupportFragmentManager(),
                    "SEARCH_AVAILABLE_BOOKS");
            case R.id.menu_filter_search_filter_btn:
                // TODO open filter fragment
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void sendSearchedKeywords(String searchString) {
        Intent intent = new Intent(PendingRequestsActivity.this, ExploreActivity.class);
        intent.putExtra(SEARCHED_KEYWORDS, searchString);
        startActivity(intent);
    }
}
