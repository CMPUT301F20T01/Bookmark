package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO: Description of class.
 * @author Mitch Adam.
 */
public class MyAvailableBookDetailsActivity extends AppCompatActivity {

    private TextView titleEditText;
    private TextView authorEditText;
    private TextView isbnEditText;
    private TextView descriptionEditText;
    private ImageView imageView;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_available_book_details);
        getSupportActionBar().setTitle("Book Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleEditText = findViewById(R.id.book_details_title_text);
        authorEditText = findViewById(R.id.book_details_author_text);
        isbnEditText = findViewById(R.id.book_details_isbn_text);
        descriptionEditText = findViewById(R.id.book_details_description_text);
        imageView = findViewById(R.id.book_details_book_image);
        statusText = findViewById(R.id.book_details_book_status_text);

        fillBookDetails();
    }

    public void fillBookDetails() {
        //TODO: Pass in actual details
        Log.d("Book Details", "Setting book description");
        titleEditText.setText("My Book Title");
        authorEditText.setText("My Book Author");
        isbnEditText.setText("ISBN: 11111111111");
        descriptionEditText.setText("Description: My Book description");
        //TODO: imageView.setImageBitmap();
        statusText.setText("Status: Available");
    }
}
