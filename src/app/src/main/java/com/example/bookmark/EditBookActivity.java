package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * This activity allows a user to edit the details of a book, remove a photo
 * and delete a book. Navigate here from the BookDetailsActivity
 * <p>
 * TODO: Add more to these? Classes/Listeners?
 *
 * @author Mitch Adam.
 */
public class EditBookActivity extends AppCompatActivity {
    // TODO: Figure out the back navigation

    String isbn;
    String title;
    String author;
    String description;

    private ImageButton scanISBNButton;
    private ImageButton addPhotoButton;
    private ImageButton deletePhotoButton;
    private Button doneAddBookButton;
    private Button deleteBookButton;

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private EditText descriptionEditText;


    private View.OnClickListener scanISBNListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToScanISBN();
        }
    };

    private View.OnClickListener addPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addPhoto();
        }
    };

    private View.OnClickListener deletePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deletePhoto();
        }
    };

    private View.OnClickListener doneAddBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doneAddBook();
        }
    };

    private View.OnClickListener deleteBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteBook();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        getSupportActionBar().setTitle("Edit Book");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent myIntent = getIntent(); // gets the previously created intent
        isbn = myIntent.getStringExtra("ISBN");

        scanISBNButton = findViewById(R.id.edit_book_scan_isbn_btn);
        scanISBNButton.setOnClickListener(scanISBNListener);

        addPhotoButton = findViewById(R.id.edit_book_add_photo_btn);
        addPhotoButton.setOnClickListener(addPhotoListener);

        deletePhotoButton = findViewById(R.id.edit_book_delete_photo_btn);
        deletePhotoButton.setOnClickListener(deletePhotoListener);

        doneAddBookButton = findViewById(R.id.edit_book_done_btn);
        doneAddBookButton.setOnClickListener(doneAddBookListener);

        deleteBookButton = findViewById(R.id.edit_book_delete_book_btn);
        deleteBookButton.setOnClickListener(deleteBookListener);

        titleEditText = findViewById(R.id.edit_book_title_field);
        authorEditText = findViewById(R.id.edit_book_author_field);
        isbnEditText = findViewById(R.id.edit_book_isbn_field);
        descriptionEditText = findViewById(R.id.edit_book_description_field);

        getBookDetailsFromISBN();
        fillBookDescriptionFields();
    }

    private void getBookDetailsFromISBN() {
        // TODO: Get details from firebase
        author = "Book Author";
        title = "Book Title";
        description = "Book Description";
    }

    private void fillBookDescriptionFields() {
        titleEditText.setText(title);
        authorEditText.setText(author);
        isbnEditText.setText(isbn);
        descriptionEditText.setText(description);
    }

    private void goToScanISBN() {
        Intent intent = new Intent(EditBookActivity.this, ScanIsbnActivity.class);
        startActivityForResult(intent, 1);
    }

    private void addPhoto() {
        // TODO: I think Eric is doing this
        Log.d("Edit Book", "Click add photo");
    }

    private void deletePhoto() {
        // TODO: Sync up with Kyle on Firebase stuff
        Log.d("Edit Book", "Click delete photo");
    }

    private void doneAddBook() {
        // TODO: Need the book class
        Log.d("Edit Book", "Click done add book");
    }

    private void deleteBook() {
        // TODO: Sync up with Kyle on Firebase stuff
        Log.d("Edit Book", "Click delete book");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get ISBN
        if (requestCode == 1) {
            String isbn = data.getStringExtra("ISBN");
            isbnEditText.setText(isbn);
        }
    }
}
