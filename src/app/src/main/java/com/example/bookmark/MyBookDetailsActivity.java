package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookmark.models.MenuOptions;

/**
 * This activity shows the details of a book. Depending on the
 * status of the book the user can then take some action. A user
 * can also navigate to the edit book activity from here.
 * <p>
 * TODO: Add more to these? Classes/Listeners?
 *
 * @author Mitch Adam.
 */
public class MyBookDetailsActivity extends BackButtonActivity implements MenuOptions {

    String isbn;
    String title;
    String author;
    String description;
    String status;
    //Image image;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView statusTextView;

    private Button actionButton;

    private View.OnClickListener manageRequestsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            manageRequests();
        }
    };

    private View.OnClickListener giveBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giveBook();
        }
    };

    private View.OnClickListener receiveBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            receiveBook();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_details);
        getSupportActionBar().setTitle("Book Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent myIntent = getIntent(); // gets the previously created intent
        isbn = myIntent.getStringExtra("ISBN");

        titleTextView = findViewById(R.id.book_details_title_text);
        authorTextView = findViewById(R.id.book_details_author_text);
        isbnTextView = findViewById(R.id.book_details_isbn_text);
        descriptionTextView = findViewById(R.id.book_details_description_text);
        imageView = findViewById(R.id.book_details_book_image);
        statusTextView = findViewById(R.id.book_details_book_status_text);

        actionButton = findViewById(R.id.book_details_action_btn);

        getBookDetailsFromISBN();
        fillBookDetails();
        configureActionButton();
    }

    private void getBookDetailsFromISBN() {
        // TODO: Get details from firebase
        author = "Book Author";
        title = "Book Title";
        description = "Book Description";
        //Image = some image
        status = "Requested";
    }

    private void fillBookDetails() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText("ISBN: " + isbn);
        descriptionTextView.setText("Description: " + description);
        //imageView.setImageBitmap();
        statusTextView.setText("Status: " + status);
    }

    private void configureActionButton() {
        switch (status) {
            case "Available":
                actionButton.setVisibility(View.INVISIBLE);
                break;
            case "Requested":
                actionButton.setText("Manage Requests");
                actionButton.setOnClickListener(manageRequestsListener);
                break;
            case "Accepted":
                actionButton.setText("Give");
                actionButton.setOnClickListener(giveBookListener);
                break;
            case "Borrowed":
                actionButton.setText("Receive");
                actionButton.setOnClickListener(receiveBookListener);
                break;
        }
    }

    private void manageRequests() {
        //TODO
        Log.d("Book Details", "Manage Requests Clicked");
    }

    private void giveBook() {
        //TODO
        Log.d("Book Details", "Give Book Clicked");
    }

    private void receiveBook() {
        //TODO
        Log.d("Book Details", "Receive Book Clicked");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_edit_btn:
                Intent intent = new Intent(MyBookDetailsActivity.this, EditBookActivity.class);
                intent.putExtra("ISBN", isbn);
                startActivity(intent);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

}
