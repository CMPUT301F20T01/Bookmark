package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmark.models.Book;

/**
 * TODO: Description of class.
 *
 * @author Nayan Prakash.
 */
public class BorrowedBookDetailsActivity extends BackButtonActivity {

    public static final int GET_ISBN = 1;

    String isbn;
    String title;
    String author;
    String description;
    String status;
    // TODO: Image image;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView statusTextView;

    private Button actionButton;

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_book_details);
        getSupportActionBar().setTitle("Book Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent myIntent = getIntent();
        Bundle myBundle = myIntent.getExtras();
        book = (Book) myBundle.getSerializable("Book");

        titleTextView = findViewById(R.id.borrowed_details_title_text);
        authorTextView = findViewById(R.id.borrowed_details_author_text);
        isbnTextView = findViewById(R.id.borrowed_details_isbn_text);
        descriptionTextView = findViewById(R.id.borrowed_details_description_text);
        imageView = findViewById(R.id.borrowed_details_book_image);
        statusTextView = findViewById(R.id.borrowed_details_book_status_text);

        actionButton = findViewById(R.id.borrowed_details_action_btn);

        setBookDetails();
        fillBookDetails();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BorrowedBookDetailsActivity.GET_ISBN && resultCode == Activity.RESULT_OK) {
            String isbn = data.getStringExtra("ISBN");
            if(book.getIsbn().equals(isbn)) {
                book.setStatus(Book.Status.AVAILABLE);
            } else {
                Toast.makeText(this, "Scanned ISBN is not the same as this book's ISBN", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setBookDetails() {
        isbn = book.getIsbn();
        author = book.getAuthor();
        title = book.getTitle();
        description = book.getDescription();
        status = book.getStatus().toString();
    }

    private void fillBookDetails() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText("ISBN: " + isbn);
        descriptionTextView.setText("Description: " + description);
        //imageView.setImageBitmap();
        statusTextView.setText("Status: " + status);
    }

    public void handleReturnButtonClick(View v) {
        Intent intent = new Intent(this, ScanIsbnActivity.class);
        startActivityForResult(intent, BorrowedBookDetailsActivity.GET_ISBN);
    }
}
