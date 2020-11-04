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
import java.util.List;

/**
 * This activity shows a user a list of books that match the keyword(s) from
 * the search they have just performed. They can select a book which takes them
 * to the ExploreBookDetailsActivity where they can see the books details.
 * TODO what else do we want here?
 *
 * @author Ryan Kortbeek.
 */
public class ExploreActivity extends AppCompatActivity implements SearchDialogFragment.OnFragmentInteractionListener {

    List<Book> searchResults = new ArrayList<>();
    BookList searchResultsAdapter;
    ListView searchResultsListView;

    ActionBar exploreActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        getSupportActionBar().setTitle("Explore");

        searchResultsListView = findViewById(R.id.search_results_listview);

        getSearchResults(getIntent());
        searchResultsAdapter = new BookList(this, searchResults, true, true);
        searchResultsListView.setAdapter(searchResultsAdapter);
        searchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ExploreActivity.this,
                    ExploreBookDetailsActivity.class);
                // TODO decide how the book data is to be sent to the
                //  ExploreBookDetailsActivity
                intent.putExtra("selected-book",
                    (Parcelable) searchResults.get(i));
                startActivity(intent);
            }
        });
    }

    private void getSearchResults(Intent intent) {
        // TODO get books from intent that match the searched keyword(s) -
        //  this should be passed from the activity that started this
        //  activity (i.e. the class that implements the executeSearch(..)
        //  function from the interface in the SearchDialogFragment should
        //  perform the search and pass the results to this activity)

        // Proof of concept
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String proof = bundle.getString("proof");

            Owner owner = new Owner("u", "fn", "ln",
                "email", "pn");

            Book b1 = new Book("Title 1", "Author 1", "1111111", owner);
            b1.setDescription(proof);

            searchResults.add(b1);
        }
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

        Intent intent = new Intent(ExploreActivity.this, ExploreActivity.class);
        // TODO put books that match the searched keyword(s) into intent that
        //  is sent to the ExploreActivity which will display the search
        //  results

        // Proof of concept
        intent.putExtra("proof", "Intent has been received!");

        startActivity(intent);
    }
}
