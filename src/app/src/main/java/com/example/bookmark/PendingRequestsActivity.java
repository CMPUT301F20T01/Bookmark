package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.SearchDialogFragment;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.Owner;
import com.example.bookmark.models.Request;

import java.util.ArrayList;

/**
 * This activity shows a user a list of books that they have pending requests
 * for. They can select a book which takes them to the
 * RequestedBookDetailsActivity where they can see the books details.
 * TODO what else do we want here?
 *
 * @author Ryan Kortbeek.
 */
public class PendingRequestsActivity extends AppCompatActivity implements SearchDialogFragment.OnFragmentInteractionListener {

    ArrayList<Book> requestedBooks;
    BookList requestedBooksAdapter;
    ListView requestedBooksListView;

    ActionBar pendingRequestsActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        pendingRequestsActionBar = getSupportActionBar();
        assert pendingRequestsActionBar != null;
        pendingRequestsActionBar.setTitle("Pending Requests");

        requestedBooksListView = findViewById(R.id.requested_books_listview);

        requestedBooks = new ArrayList<>();
        getRequestedBooks();
        requestedBooksAdapter = new BookList(this, requestedBooks, true, true);
        requestedBooksListView.setAdapter(requestedBooksAdapter);
        requestedBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PendingRequestsActivity.this,
                    RequestedBookDetailsActivity.class);
                // TODO decide how the book data is to be sent to the
                //  RequestedBookDetailsActivity
                intent.putExtra("selected-book",
                    (Parcelable) requestedBooks.get(i));
                startActivity(intent);
            }
        });
    }

    private void getRequestedBooks() {
        // TODO get books requested by current user - need access to current
        //  user and firebase

        // Proof of concept
        Owner owner = new Owner("u", "fn", "ln",
            "email", "pn",
            new ArrayList<Book>(), new ArrayList<Request>());

        Book b1 = new Book("Title 1", "Author 1", "1111111", owner);
        b1.setDescription("Book 1 description");

        requestedBooks.add(b1);
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
    public void executeSearch(String searchString) {
        // TODO call search method from singleton that interacts with firebase

        Intent intent = new Intent(PendingRequestsActivity.this, ExploreActivity.class);
        // TODO put books that match the searched keyword(s) into intent that
        //  is sent to the ExploreActivity which will display the search
        //  results

        // Proof of concept
        intent.putExtra("proof", "Intent has been received!");

        startActivity(intent);
    }
}
