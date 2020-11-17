package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.bookmark.abstracts.AddEditBookActivity;
import com.example.bookmark.models.Book;

/**
 * This activity allows a user to edit the details of a book, remove a photo
 * and delete a book. Navigate here from the BookDetailsActivity
 * <p>
 * Outstanding Issues/TODOs
 * Need to hook up to DB
 *
 * @author Mitch Adam.
 */
public class EditBookActivity extends AddEditBookActivity {
    private Button doneEditBookButton;
    private Button deleteBookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContextView called by AddEditBookActivity via getLayoutResourceId()
        getSupportActionBar().setTitle("Edit Book");

        doneEditBookButton = findViewById((R.id.edit_book_done_button));
        doneEditBookButton.setOnClickListener(v -> doneEditBook());

        deleteBookButton = findViewById((R.id.edit_book_delete_button));
        deleteBookButton.setOnClickListener(v -> deleteBook());

        // TODO: Get book from intent
        // TODO: Call populateFields with book from intent

        // TODO: Delete this once ^ is done:
        Intent myIntent = getIntent(); // gets the previously created intent
        isbnEditText.setText(myIntent.getStringExtra("ISBN"));
    }

    private void populateFields(Book book) {
        // Set the values of all the EditTexts
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_book;
    }

    protected void doneEditBook() {
        // TODO: Call uriToPhotograph() only here, once the done button has been pressed (Uri to Bitmap is relatively expensive)
        // TODO: Edit the book object, pass changes to firebase
        Log.d("Edit Book", "Click done add book");
    }

    private void deleteBook() {
        // TODO: Sync up with Kyle on Firebase stuff
        Log.d("Edit Book", "Click delete book");
    }
}
