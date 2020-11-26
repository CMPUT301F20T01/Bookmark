package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bookmark.adapters.BookList;
import com.example.bookmark.fragments.SearchDialogFragment;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a user a list of their books.
 * They can select a book to see and edit the details of a book.
 * They can also add a book from here
 *
 * @author Mitch Adam.
 */
public class MyBooksActivity extends NavigationDrawerActivity
    implements SearchDialogFragment.OnFragmentInteractionListener, MenuOptions {
    public static final String SEARCHED_KEYWORDS = "com.example.bookmark" +
        ".SEARCH";

    private User user;

    private final List<Book> allBooks = new ArrayList<Book>();
    private List<Book> filteredBooks = new ArrayList<Book>();

    private BookList booksAdapter;
    private ListView booksListView;

    FloatingActionButton addBookBtn;

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
        getSupportActionBar().setTitle("My Books");

        booksListView = findViewById(R.id.my_books_listview);

        addBookBtn = findViewById(R.id.my_books_add_btn);
        addBookBtn.setOnClickListener(addBookListener);
        booksAdapter = new BookList(this, filteredBooks, false, true);

        getBooks();
        setFilteredBooks();
        booksListView.setAdapter(booksAdapter);
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", filteredBooks.get(position));
                bundle.putSerializable("User", user);
                Intent intent = new Intent(MyBooksActivity.this, MyBookDetailsActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        // Pull down to refresh
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBooks();
                setFilteredBooks();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBooks();
        setFilteredBooks();
    }

    /**
     * Get list of users books
     */
    private void getBooks() {
        OnFailureListener onFailureListener = e -> DialogUtil.showErrorDialog(this, e);
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(
            username,
            user ->
                StorageServiceProvider.getStorageService().retrieveBooksByOwner(
                    user,
                    books -> {
                        allBooks.clear();
                        allBooks.addAll(books);
                        booksAdapter.notifyDataSetChanged();
                    },
                    onFailureListener
                ),
            onFailureListener
        );
    }


    /**
     * Set the list of books to display to user based on what is filtered
     */
    private void setFilteredBooks() {
        // TODO: Implement filtering
        filteredBooks = allBooks;
    }

    /**
     * Start add book intent
     */
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

    /**
     * Handle menu selection
     */
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
