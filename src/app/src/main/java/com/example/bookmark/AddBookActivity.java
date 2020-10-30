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
 * TODO: Description of class.
 * @author Mitch Adam.
 */
public class AddBookActivity extends AppCompatActivity {

    private ImageButton scanISBNButton;
    private ImageButton addPhotoButton;
    private Button doneAddBookButton;


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

    private View.OnClickListener doneAddBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doneAddBook();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        getSupportActionBar().setTitle("Add Book");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scanISBNButton = findViewById(R.id.add_book_scan_isbn_btn);
        scanISBNButton.setOnClickListener(scanISBNListener);

        addPhotoButton = findViewById(R.id.add_book_add_photo_btn);
        addPhotoButton.setOnClickListener(addPhotoListener);

        doneAddBookButton = findViewById((R.id.add_book_done_btn));
        doneAddBookButton.setOnClickListener(doneAddBookListener);

        titleEditText = findViewById(R.id.add_book_title_field);
        authorEditText = findViewById(R.id.add_book_author_field);
        isbnEditText = findViewById(R.id.add_book_isbn_field);
        descriptionEditText = findViewById(R.id.add_book_description_field);
    }

    private void goToScanISBN() {
        Intent intent = new Intent(AddBookActivity.this, ScanIsbnActivity.class);
        startActivityForResult(intent, 1);
    }

    private void addPhoto() {
        // TODO: I think Eric is doing this
        Log.d("Add Book", "Click add photo");

    }

    private void doneAddBook() {
        // TODO: Create a book class and return that
        Log.d("Add Book", "Click done add book");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Get ISBN
        if(requestCode==1){
            String isbn = data.getStringExtra("ISBN");
            isbnEditText.setText(isbn);
        }
    }


// TODO: Figure out the back navigation

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle arrow click here
//        if (item.getItemId() == android.R.id.home) {
//            finish(); // close this activity and return to preview activity (if there is any)
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        Intent intent = getIntent();
//        setResult(MainActivity.ACTIVITY_CANCELED, intent);
//        finish();
//        return true;
//    }
}
