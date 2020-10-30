package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO: Description of class.
 * @author Mitch Adam.
 */
public class MyAvailableBookDetailsActivity extends AppCompatActivity {

    String isbn;
    String title;
    String author;
    String description;
    String status;
    //Image image;

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

        Intent myIntent = getIntent(); // gets the previously created intent
        isbn = myIntent.getStringExtra("ISBN");

        titleEditText = findViewById(R.id.book_details_title_text);
        authorEditText = findViewById(R.id.book_details_author_text);
        isbnEditText = findViewById(R.id.book_details_isbn_text);
        descriptionEditText = findViewById(R.id.book_details_description_text);
        imageView = findViewById(R.id.book_details_book_image);
        statusText = findViewById(R.id.book_details_book_status_text);

        getBookDetailsFromISBN();
        fillBookDetails();
    }

    private void getBookDetailsFromISBN() {
        // TODO: Get details from firebase
        author = "Book Author";
        title = "Book Title";
        description = "Book Description";
        //Image = some image
        status = "Available";
    }

    public void fillBookDetails() {
        titleEditText.setText(title);
        authorEditText.setText(author);
        isbnEditText.setText("ISBN: " + isbn);
        descriptionEditText.setText("Description: " + description);
        //imageView.setImageBitmap();
        statusText.setText("Status: " + status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.edit_menu_edit_btn:
            Intent intent = new Intent(MyAvailableBookDetailsActivity.this, EditBookActivity.class);
            intent.putExtra("ISBN",isbn);
            startActivity(intent);
    }
        return(super.onOptionsItemSelected(item));
    }

}
