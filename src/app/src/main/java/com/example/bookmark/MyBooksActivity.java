package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.SearchDialogFragment;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.MenuOptions;
import com.example.bookmark.models.Owner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a user a list of their books.
 * They can select a book to see and edit the details of a book.
 * They can also add a book from here
 * <p>
 * Outstanding Issues/TODOs
 * Need to hook up to DB
 *
 * @author Mitch Adam.
 */
public class MyBooksActivity extends AppCompatActivity
    implements SearchDialogFragment.OnFragmentInteractionListener, MenuOptions {
    public static final String SEARCHED_KEYWORDS = "com.example.bookmark" +
        ".SEARCH";
    // Going to need some sort of owner or uid

    private final List<Book> allBooks = new ArrayList<Book>();
    private List<Book> filteredBooks;

    private BookList booksAdapter;
    private ListView booksListView;

    FloatingActionButton addBookBtn;
    private Drawer navigationDrawer = null;

    private final View.OnClickListener addBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToAddBook();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_books_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Books");
        navigationDrawer = DrawerProvider.getDrawer(this, toolbar);


        booksListView = findViewById(R.id.my_books_listview);

        addBookBtn = findViewById(R.id.my_books_add_btn);
        addBookBtn.setOnClickListener(addBookListener);

        getBooks();
        setFilteredBooks();
        booksAdapter = new BookList(this, filteredBooks, false, true);
        booksListView.setAdapter(booksAdapter);
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MyBooksActivity.this, MyBookDetailsActivity.class);
                intent.putExtra("ISBN", filteredBooks.get(position).getIsbn());
                startActivity(intent);
            }
        });
    }

    private void getBooks() {
        // TODO: Get books from firebase by owner

        //Temp add some books
        Owner owner = new Owner("u", "fn", "ln",
            "email", "pn");

        Book b1 = new Book("Title 1", "Author 1", "1111111", owner);
        b1.setDescription("Book 1 description");

        Book b2 = new Book("Title 2", "Author 2", "22222", owner);
        b2.setDescription("Book 2 description");

        allBooks.add(b1);
        allBooks.add(b2);
    }

    private void setFilteredBooks() {
        // TODO: Implement filtering
        // I think eric is working on part of this?
        filteredBooks = allBooks;
    }

    private void goToAddBook() {
        Intent intent = new Intent(MyBooksActivity.this, AddBookActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter_search, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (navigationDrawer != null && navigationDrawer.isDrawerOpen()) {
            navigationDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter_search_search_btn:
                // Opens search fragment
                new SearchDialogFragment().show(getSupportFragmentManager(),
                    "SEARCH_AVAILABLE_BOOKS");
            case R.id.menu_filter_search_filter_btn:
                //TODO: Open filter fragment
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void sendSearchedKeywords(String searchString) {
        Intent intent = new Intent(MyBooksActivity.this, ExploreActivity.class);
        intent.putExtra(SEARCHED_KEYWORDS, searchString);
        startActivity(intent);
    }
}
