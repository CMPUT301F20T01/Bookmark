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
 * This activity shows a user a list of books that they are currently
 * borrowing. They can select a book which takes them to the
 * BorrowedBookDetailsActivity where they can see the books details and
 * return the book.
 * TODO what else do we want here?
 *
 * @author Ryan Kortbeek.
 */
public class BorrowedActivity extends NavigationDrawerActivity
    implements SearchDialogFragment.OnFragmentInteractionListener {
    public static final String EXTRA_BOOK = "com.example.bookmark.BOOK";
    public static final String SEARCHED_KEYWORDS = "com.example.bookmark" +
        ".SEARCH";

    private List<Book> borrowedBooks = new ArrayList<>();
    private BookList borrowedBooksAdapter;
    private ListView borrowedBooksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed);

        getSupportActionBar().setTitle("Borrowed");

        borrowedBooksListView = findViewById(R.id.borrowed_books_listview);

        getBorrowedBooks();
        borrowedBooksAdapter = new BookList(this, borrowedBooks, true, false);
        borrowedBooksListView.setAdapter(borrowedBooksAdapter);
        borrowedBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BorrowedActivity.this,
                    BorrowedBookDetailsActivity.class);
                intent.putExtra(EXTRA_BOOK, borrowedBooks.get(i));
                startActivity(intent);
            }
        });
    }

    private void getBorrowedBooks() {
        // TODO get books currently borrowed by current user - need access to
        //  current user and firebase

        // Proof of concept
        // Book b1 = new Book("Title 1", "Author 1", "1111111", "o");
        // b1.setDescription("Book 1 description");

        // borrowedBooks.add(b1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu with the search icon
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter_search_search_btn) {
            // Opens search fragment
            new SearchDialogFragment().show(getSupportFragmentManager(),
                "SEARCH_AVAILABLE_BOOKS");
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void sendSearchedKeywords(String searchString) {
        Intent intent = new Intent(BorrowedActivity.this, ExploreActivity.class);
        intent.putExtra(SEARCHED_KEYWORDS, searchString);
        startActivity(intent);
    }
}
