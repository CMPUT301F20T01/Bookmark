package com.example.bookmark.abstracts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.example.bookmark.models.Book;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public abstract class ListingBooksActivity extends NavigationDrawerActivity {
    public static final String EXTRA_BOOK = "com.example.bookmark.BOOK";
    protected String activityTitle = "";
    protected boolean showOwner = true;
    protected boolean showStatus = true;
    protected List<Book> relevantBooks = new ArrayList<>();
    protected List<Book> visibleBooks = new ArrayList<>();
    protected Context intentStartingPoint;
    protected Class<?> intentDestination;
    protected BookList visibleBooksAdapter;
    protected ListView visibleBooksListView;
    protected TextInputEditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_books);

        searchBar = (TextInputEditText) findViewById(R.id.search_bar_textInput);
        visibleBooksListView = (ListView) findViewById(R.id.visible_books_listView);

        setActivityTitle();
        setBookOwnerAndStatusVisibility();
        setIntentStartingPoint();
        setIntentDestination();
        getSupportActionBar().setTitle(activityTitle);

        getBooks();

        visibleBooksAdapter = new BookList(this, visibleBooks, showOwner,
            showStatus);
        visibleBooksListView.setAdapter(visibleBooksAdapter);
        visibleBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListingBooksActivity.this,
                    intentDestination);
                intent.putExtra(EXTRA_BOOK, relevantBooks.get(i));
                startActivity(intent);
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchString = charSequence.toString();
                updateSearchResults(searchString.split(" "));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter_search_search_btn:
                // Changes visibility of search bar
                if (searchBar.getVisibility() == View.INVISIBLE) {
                    searchBar.setVisibility(View.VISIBLE);
                    searchBar.setText("");
                } else {
                    searchBar.setText("");
                    searchBar.setVisibility(View.INVISIBLE);
                }
            case R.id.menu_filter_search_filter_btn:
                // TODO open filter fragment
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu with the filter and search icons
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBooks();
    }

    protected void updateSearchResults(String[] keywords) {
        visibleBooks.clear();
        if (keywords.length == 0) {
            visibleBooks.addAll(relevantBooks);
        } else {
            for (Book book : relevantBooks) {
                for (String keyword : keywords) {
                    if (book.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                        visibleBooks.add(book);
                        break;
                    }
                }
            }
        }
        visibleBooksAdapter.notifyDataSetChanged();
    }

    protected abstract void setActivityTitle();

    protected abstract void setBookOwnerAndStatusVisibility();

    protected abstract void setIntentStartingPoint();

    protected abstract void setIntentDestination();

    protected abstract void getBooks();
}


