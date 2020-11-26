package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookmark.models.Book;

/**
 * TODO: Description of class.
 *
 * @author Nayan Prakash.
 */
public class RequestedBookDetailsActivity extends BackButtonActivity {

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

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_book_details);
        getSupportActionBar().setTitle("Book Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent myIntent = getIntent();
        Bundle myBundle = myIntent.getExtras();
        book = (Book) myBundle.getSerializable("Book");

        titleTextView = findViewById(R.id.requested_details_title_text);
        authorTextView = findViewById(R.id.requested_details_author_text);
        isbnTextView = findViewById(R.id.requested_details_isbn_text);
        descriptionTextView = findViewById(R.id.requested_details_description_text);
        imageView = findViewById(R.id.requested_details_book_image);
        statusTextView = findViewById(R.id.requested_details_book_status_text);

        setBookDetails();
        fillBookDetails();
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
}
