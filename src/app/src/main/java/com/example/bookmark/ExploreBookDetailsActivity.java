package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.StorageService;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;

/**
 * TODO: Description of class.
 * @author Nayan Prakash.
 */
public class ExploreBookDetailsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_explore_book_details);
        getSupportActionBar().setTitle("Book Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra(ExploreActivity.EXTRA_BOOK);

        titleTextView = findViewById(R.id.explore_details_title_text);
        authorTextView = findViewById(R.id.explore_details_author_text);
        isbnTextView = findViewById(R.id.explore_details_isbn_text);
        descriptionTextView = findViewById(R.id.explore_details_description_text);
        imageView = findViewById(R.id.explore_details_book_image);
        statusTextView = findViewById(R.id.explore_details_book_status_text);

        actionButton = findViewById(R.id.explore_details_action_btn);

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

    public void handleRequestButtonClick(View v) {
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(
            username,
            user -> {
                Request request = new Request(book, user, null);
                StorageServiceProvider.getStorageService().storeRequest(
                    request,
                    aVoid -> {
                        book.setStatus(Book.Status.REQUESTED);
                    },
                    e -> DialogUtil.showErrorDialog(this, e)
                );
            },
            e -> DialogUtil.showErrorDialog(this, e)
        );
    }
}
