package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.SearchDialogFragment;
import com.example.bookmark.models.Book;
import com.example.bookmark.server.FirebaseProvider;
import com.example.bookmark.util.DialogUtil;

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
public class ExploreActivity extends NavigationDrawerActivity
    implements SearchDialogFragment.OnFragmentInteractionListener {
    public static final String EXTRA_BOOK = "com.example.bookmark.BOOK";
    public static final String SEARCHED_KEYWORDS = "com.example.bookmark" +
        ".SEARCH";

    private List<Book> searchResults = new ArrayList<>();
    private BookList searchResultsAdapter;
    private ListView searchResultsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        getSupportActionBar().setTitle("Explore");

        searchResultsListView = findViewById(R.id.search_results_listview);

        Intent searchIntent = getIntent();
        String searchedKeywords =
            searchIntent.getStringExtra(SEARCHED_KEYWORDS);

        // TODO remove line below when we want to start using search
        searchedKeywords = "";

        executeSearch(searchedKeywords);
        searchResultsAdapter = new BookList(this, searchResults, true, true);
        searchResultsListView.setAdapter(searchResultsAdapter);
        searchResultsListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ExploreBookDetailsActivity.class);
            intent.putExtra(EXTRA_BOOK, searchResults.get(position));
            startActivity(intent);
        });
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
        // Instead of sending intent to this same activity just calls
        // executeSearch again

        // TODO replace line below with searchString when we want to start
        //  using search
        executeSearch("");
    }

    public void executeSearch(String searchedKeywords) {
        FirebaseProvider.getInstance().retrieveBooks(books -> {
            searchResults.clear();
            for (Book book : books) {
                if (book.getDescription().contains(searchedKeywords)) {
                    searchResults.add(book);
                }
            }
            searchResultsAdapter.notifyDataSetChanged();
        }, e -> DialogUtil.showErrorDialog(this, e));
    }
}
