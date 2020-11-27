package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;

/**
 * This activity shows the details of a book. As well, the user can click the "Return"
 * button to which navigates the user to ScanIsbnActivity to get a Book's ISBN. Upon
 * completion of retrieving the ISBN, this Activity denotes the book available and
 * deletes it's associated request with the current user.
 *
 * @author Nayan Prakash.
 */
public class BorrowedBookDetailsActivity extends BackButtonActivity {

    public static final int GET_ISBN = 1;

    String isbn;
    String title;
    String author;
    String description;
    String ownedBy;
    String status;
    // TODO: Image image;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView ownedByTextView;
    private TextView statusTextView;

    private Button actionButton;

    private Book book;
    private User user;

    /**
     * This function creates the BorrowedBookDetails view and retrieves the book object from the
     * intent, and sets all the views of the activity
     *
     * @param savedInstanceState an instance state that has the state of the BorrowBookActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_book_details);
        getSupportActionBar().setTitle("Book Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("Book");
        user = (User) intent.getSerializableExtra("User");

        titleTextView = findViewById(R.id.borrowed_details_title_text);
        authorTextView = findViewById(R.id.borrowed_details_author_text);
        isbnTextView = findViewById(R.id.borrowed_details_isbn_text);
        descriptionTextView = findViewById(R.id.borrowed_details_description_text);
        imageView = findViewById(R.id.borrowed_details_book_image);
        ownedByTextView = findViewById(R.id.borrowed_details_owned_by);
        statusTextView = findViewById(R.id.borrowed_details_book_status_text);

        actionButton = findViewById(R.id.borrowed_details_action_btn);

        setBookDetails();
        fillBookDetails();
    }

    /**
     * This function handles the results from other activities. Specifically, this function handles
     * the results after returning from ScanIsbnActivity
     *
     * @param requestCode the requestCode of the activity results
     * @param resultCode  the resultCode of the activity result
     * @param data        the intent of the activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BorrowedBookDetailsActivity.GET_ISBN && resultCode == Activity.RESULT_OK) {
            String isbn = data.getStringExtra("ISBN");
            if(book.getIsbn().equals(isbn)) {
                // TODO: set book as available and update book status and request status
                StorageServiceProvider.getStorageService().retrieveRequest(
                    book,
                    user,
                    request -> {
                        book.setStatus(Book.Status.AVAILABLE);
                        StorageServiceProvider.getStorageService().deleteRequest(
                            request,
                            aVoid -> Log.d("Accepted Book Details", "Request stored"),
                            e -> DialogUtil.showErrorDialog(this, e)
                        );
                        StorageServiceProvider.getStorageService().storeBook(
                            book,
                            aVoid -> Log.d("Accepted Book Details", "Book stored"),
                            e -> DialogUtil.showErrorDialog(this, e)
                        );
                    },
                    e -> DialogUtil.showErrorDialog(this, e)
                );
            } else {
                Toast.makeText(this, "Scanned ISBN is not the same as this book's ISBN", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This function handles retrieving data from the Book object
     */
    private void setBookDetails() {
        isbn = book.getIsbn();
        author = book.getAuthor();
        title = book.getTitle();
        description = book.getDescription();
        ownedBy = book.getOwnerId();
        status = book.getStatus().toString();
    }

    /**
     * This function handles filling the text fields and image with the book data
     */
    private void fillBookDetails() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText("ISBN: " + isbn);
        descriptionTextView.setText("Description: " + description);
        //imageView.setImageBitmap();
        ownedByTextView.setText("Owned by: " + ownedBy);
        statusTextView.setText("Status: " + status);
    }

    /**
     * This is the function that handles the press of the "Return" button and starts an activity
     * to get the ISBN from ScanIsbnActivity
     *
     * @param v This is the view of the "Return" button
     */
    public void handleReturnButtonClick(View v) {
        Intent intent = new Intent(this, ScanIsbnActivity.class);
        startActivityForResult(intent, BorrowedBookDetailsActivity.GET_ISBN);
    }
}
