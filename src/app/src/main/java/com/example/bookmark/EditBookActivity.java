package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.example.bookmark.fragments.ImageSelectDialogFragment;

/**
 * This activity allows a user to edit the details of a book, remove a photo
 * and delete a book. Navigate here from the BookDetailsActivity
 * <p>
 * Outstanding Issues/TODOs
 * Need to hook up to DB
 *
 * @author Mitch Adam.
 */
public class EditBookActivity extends BackButtonActivity implements ImageSelectDialogFragment.ImageSelectListener {
    private static final int ISBN_REQUEST_CODE = 100;
    private static final String IMG_SELECT_TAG = "ImageSelectFragment";

    private String isbn;
    private String title;
    private String author;
    private String description;

    private ImageButton scanISBNButton;
    private ImageButton addPhotoButton;
    private ImageButton deletePhotoButton;
    private Button doneAddBookButton;
    private Button deleteBookButton;

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private EditText descriptionEditText;


    private final View.OnClickListener scanISBNListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToScanISBN();
        }
    };

    private final View.OnClickListener addPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addPhoto();
        }
    };

    private final View.OnClickListener deletePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deletePhoto();
        }
    };

    private final View.OnClickListener doneAddBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doneAddBook();
        }
    };

    private final View.OnClickListener deleteBookListener = new View.OnClickListener() {
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
        startActivityForResult(intent, ISBN_REQUEST_CODE);
    }

    private void addPhoto() {
        ImageSelectDialogFragment.newInstance().show(getSupportFragmentManager(), IMG_SELECT_TAG);
    }

    public void onImageSelect(Uri uri) {
        // TODO: Save URI for when creating a book class
        addPhotoButton.setImageURI(uri);
        deletePhotoButton.setVisibility(View.VISIBLE);
    }

    private void deletePhoto() {
        // TODO: Sync up with Kyle on Firebase stuff
        // TODO: Properly clear the image instead of replacing it with original drawable
        // TODO: Clear saved URI
        addPhotoButton.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_add));
        deletePhotoButton.setVisibility(View.GONE);
    }

    private void doneAddBook() {
        // TODO: Need the book class
        // TODO: Call uriToPhotograph() only here, once the done button has been pressed (Uri to Bitmap is relatively expensive)
        Log.d("Edit Book", "Click done add book");
    }

    private void deleteBook() {
        // TODO: Sync up with Kyle on Firebase stuff
        Log.d("Edit Book", "Click delete book");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) { return; }

        // Get ISBN
        if (requestCode == ISBN_REQUEST_CODE) {
            String isbn = data.getStringExtra("ISBN");
            isbnEditText.setText(isbn);
        }
    }
}
