package com.example.bookmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bookmark.adapters.BookList;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.Owner;
import com.example.bookmark.models.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * This activity shows a user a list of their books.
 * They can select a book to see and edit the details of a book.
 * They can also add a book from here
 *
 *  TODO: Add more to these? Classes/Listeners?
 * @author Mitch Adam.
 */
public class MyBooksActivity extends AppCompatActivity {
    // TODO: Figure out the back navigation
    // Going to need some sort of owner or uid

    List<Book> allBooks = new ArrayList<Book>();
    List<Book> filteredBooks;

    BookList booksAdapter;
    ListView booksListView;

    FloatingActionButton addBookBtn;

    private View.OnClickListener addBookListener = new View.OnClickListener() {
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

        getBooks();
        setFilteredBooks();
        booksAdapter = new BookList(this, filteredBooks);
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
            "email", "pn",
            new ArrayList<Book>(), new ArrayList<Request>() );

        Book b1 = new Book("Title 1", "Author 1", "1111111", owner );
        b1.setDescription("Book 1 description");

        Book b2 = new Book("Title 2", "Author 2", "22222", owner );
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_filter_search_search_btn:
                //TODO: Open search fragment
                break;
            case R.id.menu_filter_search_filter_btn:
                //TODO: Open filter fragment
                break;
        }
        return(super.onOptionsItemSelected(item));
    }
}