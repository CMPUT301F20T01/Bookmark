package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookmark.abstracts.AddEditBookActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;

/**
 * This activity shows the details of a book. Depending on the
 * status of the book the user can then take some action. A user
 * can also navigate to the edit book activity from here.
 * <p>
 * Outstanding Issues/TODOs
 * Need to hook up to DB
 *
 * @author Mitch Adam.
 */
public class MyBookDetailsActivity extends BackButtonActivity implements MenuOptions {
    private static final int EDIT_REQUEST_CODE = 101;

    private User user;
    private Book book;

    private String isbn;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView statusTextView;

    private Button actionButton;

    private final View.OnClickListener manageRequestsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            manageRequests();
        }
    };

    private final View.OnClickListener giveBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giveBook();
        }
    };

    private final View.OnClickListener receiveBookListener = new View.OnClickListener() {
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

        Intent intent = getIntent(); // gets the previously created intent
        Bundle bundle = intent.getExtras();

        user = (User) bundle.getSerializable("User");
        book = (Book) bundle.getSerializable("Book");

        titleTextView = findViewById(R.id.book_details_title_text);
        authorTextView = findViewById(R.id.book_details_author_text);
        isbnTextView = findViewById(R.id.book_details_isbn_text);
        descriptionTextView = findViewById(R.id.book_details_description_text);
        imageView = findViewById(R.id.book_details_book_image);
        statusTextView = findViewById(R.id.book_details_book_status_text);

        actionButton = findViewById(R.id.book_details_action_btn);

        fillBookDetails();
        configureActionButton();
    }

    private void fillBookDetails() {
        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());
        isbnTextView.setText("ISBN: " + book.getIsbn());
        descriptionTextView.setText("Description: " + book.getDescription());
//        if (book.getPhotograph() != null) {
//            imageView.setImageURI(book.getPhotograph().getUri());
//        }
        statusTextView.setText("Status: "
            + book.getStatus().toString().charAt(0)
            + book.getStatus().toString().substring(1).toLowerCase());

    }

    private void configureActionButton() {
        switch (book.getStatus()) {
            case AVAILABLE:
                actionButton.setVisibility(View.INVISIBLE);
                break;
            case REQUESTED:
                actionButton.setText("Manage Requests");
                actionButton.setOnClickListener(manageRequestsListener);
                break;
            case ACCEPTED:
                actionButton.setText("Give");
                actionButton.setOnClickListener(giveBookListener);
                break;
            case BORROWED:
                actionButton.setText("Receive");
                actionButton.setOnClickListener(receiveBookListener);
                break;
        }
    }

    private void manageRequests() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Book", book);
        bundle.putSerializable("User", user);
        Intent intent = new Intent(MyBookDetailsActivity.this, ManageRequestsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", book);
                Intent intent = new Intent(MyBookDetailsActivity.this, EditBookActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    /**
     * Callback for when the Edit book activity returns
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        // Get ISBN
        if (requestCode == EDIT_REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            book = (Book) bundle.getSerializable("Book");
            fillBookDetails();
        }
    }

}
