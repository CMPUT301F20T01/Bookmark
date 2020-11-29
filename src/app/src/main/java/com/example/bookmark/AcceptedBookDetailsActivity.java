package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.RequestUtil;

/**
 * This activity shows the details of a book. As well, the user can click the "Borrow"
 * button to which navigates the user to ScanIsbnActivity to get a Book's ISBN. Upon
 * completion of retrieving the ISBN, this Activity denotes the book and it's associated request
 * with the current user as Borrowed.
 *
 * @author Nayan Prakash.
 */
public class AcceptedBookDetailsActivity extends BackButtonActivity {

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

    private Book book;
    private User user;

    /**
     * This function creates the AcceptedBookDetails view and retrieves the book object from the
     * intent, and sets all the views of the activity
     *
     * @param savedInstanceState an instance state that has the state of the BorrowBookActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_book_details);
        getSupportActionBar().setTitle("Book Details");

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra(ListingBooksActivity.EXTRA_BOOK);
        user = (User) intent.getSerializableExtra(ListingBooksActivity.USER);

        titleTextView = findViewById(R.id.accepted_details_title_text);
        authorTextView = findViewById(R.id.accepted_details_author_text);
        isbnTextView = findViewById(R.id.accepted_details_isbn_text);
        descriptionTextView = findViewById(R.id.accepted_details_description_text);
        imageView = findViewById(R.id.accepted_details_book_image);
        ownedByTextView = findViewById(R.id.accepted_details_owned_by);
        statusTextView = findViewById(R.id.accepted_details_book_status_text);

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

        if (requestCode == AcceptedBookDetailsActivity.GET_ISBN && resultCode == Activity.RESULT_OK) {
            String isbn = data.getStringExtra("ISBN");
            if(book.getIsbn().equals(isbn)) {
                Request request = RequestUtil.retrieveRequestsOnBookByStatus(book, Request.Status.ACCEPTED, this);
                book.setStatus(Book.Status.BORROWED);
                request.setStatus(Request.Status.BORROWED);
                StorageServiceProvider.getStorageService().storeRequest(
                    request,
                    aVoid -> Log.d("Accepted Book Details", "Request stored"),
                    e -> DialogUtil.showErrorDialog(this, e)
                );
                StorageServiceProvider.getStorageService().storeBook(
                    book,
                    aVoid -> Log.d("Accepted Book Details", "Book stored"),
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
        ownedBy = book.getOwnerId().toString();
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
     * This is the function that handles the press of the "Borrow" button and starts an activity
     * to get the ISBN from ScanIsbnActivity
     *
     * @param v This is the view of the "Borrow" button
     */
    public void handleBorrowButtonClick(View v) {
        Intent intent = new Intent(this, ScanIsbnActivity.class);
        startActivityForResult(intent, AcceptedBookDetailsActivity.GET_ISBN);
    }
}
