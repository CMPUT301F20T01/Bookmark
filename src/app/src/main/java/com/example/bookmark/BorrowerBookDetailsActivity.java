package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.EntityId;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.RequestUtil;

/**
 * This activity shows the details of a book. As well, depending on the status of the book,
 * the activity will configure the action button to prompt the user to execute different actions
 * while also implementing different functionality for each status of the book.
 *
 * @author Nayan Prakash
 */
public class BorrowerBookDetailsActivity extends BackButtonActivity {

    private static final String TAG = "Borrower Book Details";

    private static final int SCAN_ISBN_TO_RETURN = 101;
    private static final int SCAN_ISBN_TO_BORROW = 102;

    String isbn;
    String title;
    String author;
    String description;
    String ownedBy;
    String status;
    EntityId imageId;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView ownedByTextView;
    private TextView statusTextView;

    private Book book;
    private User user;

    private Button actionButton;

    // Set correct functionality based on book status
    private final View.OnClickListener requestBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestBook();
        }
    };

    private final View.OnClickListener returnBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            returnBook();
        }
    };

    private final View.OnClickListener borrowBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            borrowBook();
        }
    };

    /**
     * This function creates the BorrowerBookDetails view and retrieves both the book object
     * and the user object (the borrower) from the intent. Then, the views of the activity
     * are set and the action button is configured based on the current book status.
     *
     * @param savedInstanceState an instance state that has the state of the BorrowerBookDetailsActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_book_details);
        getSupportActionBar().setTitle("Book Details");

        Intent intent = getIntent(); // gets the previously created intent
        book = (Book) intent.getSerializableExtra(ListingBooksActivity.EXTRA_BOOK);
        user = (User) intent.getSerializableExtra(ListingBooksActivity.USER);

        titleTextView = findViewById(R.id.borrower_book_details_title_text);
        authorTextView = findViewById(R.id.borrower_book_details_author_text);
        isbnTextView = findViewById(R.id.borrower_book_details_isbn_text);
        descriptionTextView = findViewById(R.id.borrower_book_details_description_text);
        imageView = findViewById(R.id.borrower_book_details_book_image);
        ownedByTextView = findViewById(R.id.borrower_book_details_owned_by);
        statusTextView = findViewById(R.id.borrower_book_details_book_status_text);

        actionButton = findViewById(R.id.borrower_book_details_action_btn);

        setBookDetails();
        fillBookDetails();
        configureActionButton();
    }

    /**
     * This function handles retrieving data from the Book object
     */
    private void setBookDetails() {
        isbn = book.getIsbn();
        author = book.getAuthor();
        title = book.getTitle();
        description = book.getDescription();
        imageId = book.getPhotograph();
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
        if (imageId != null) {
            StorageServiceProvider.getStorageService().retrievePhotograph(
                imageId,
                photograph -> {
                    if (photograph != null) {
                        imageView.setImageURI(photograph.getImageUri());
                    }
                },
                e -> DialogUtil.showErrorDialog(this, e)
            );
        }
        ownedByTextView.setText("Owned by: " + ownedBy);
        statusTextView.setText("Status: " + status);
    }

    /**
     * This function handles setting the text of the action button and setting different
     * onClickListeners of the button depending on the status of the book
     */
    private void configureActionButton() {
        switch (book.getStatus()) {
            case AVAILABLE:
                actionButton.setText("REQUEST");
                actionButton.setOnClickListener(requestBookListener);
                break;
            case REQUESTED:
                // by default this user is not one of the requesters on this book so book is requestable
                actionButton.setText("REQUEST");
                actionButton.setOnClickListener(requestBookListener);
                StorageServiceProvider.getStorageService().retrieveRequestsByBook(
                    book,
                    requestList -> {
                        for (Request r : requestList) {
                            if (r.getRequesterId().toString().equals(user.getId().toString())) {
                                // TODO: set button style to OutlinedButton
                                actionButton.setText("REQUESTED");
                                actionButton.setOnClickListener(aVoid -> {
                                });
                                return;
                            }
                        }
                    },
                    e -> DialogUtil.showErrorDialog(this, e)
                );
                break;
            case ACCEPTED:
                actionButton.setText("BORROW");
                actionButton.setOnClickListener(borrowBookListener);
                break;
            case BORROWED:
                actionButton.setText("RETURN");
                actionButton.setOnClickListener(returnBookListener);
                break;
        }
    }

    /**
     * Set click handler for owned by TextView
     */
    public void borrowerOwnerTextViewClickListener(View v) {
        Intent intent = new Intent(BorrowerBookDetailsActivity.this, ViewProfileActivity.class);
        intent.putExtra("USERNAME", ownedBy);
        startActivity(intent);
    }

    /**
     * Handle requesting book
     */
    private void requestBook() {
        Request request = new Request(book, user, null);
        request.setStatus(Request.Status.REQUESTED);
        StorageServiceProvider.getStorageService().storeRequest(
            request,
            aVoid -> {
                book.setStatus(Book.Status.REQUESTED);
                StorageServiceProvider.getStorageService().storeBook(
                    book,
                    a -> {
                        Log.d(TAG, "Book stored as REQUESTED");
                        setBookDetails();
                        fillBookDetails();
                        configureActionButton();
                    },
                    e -> DialogUtil.showErrorDialog(this, e)
                );
            },
            e -> DialogUtil.showErrorDialog(this, e)
        );
    }

    /**
     * Handle borrowing a book
     */
    private void borrowBook() {
        RequestUtil.retrieveRequestsOnBookByStatus(
            book,
            Request.Status.ACCEPTED,
            request -> {
                Intent intent = new Intent(this, BorrowBookActivity.class);
                intent.putExtra(ListingBooksActivity.EXTRA_BOOK, book);
                intent.putExtra(BorrowBookActivity.EXTRA_REQUEST, request);
                startActivityForResult(intent, SCAN_ISBN_TO_BORROW);
            },
            e -> DialogUtil.showErrorDialog(this, e)
        );
    }

    /**
     * Handle returning a book
     */
    private void returnBook() {
        Intent intent = new Intent(this, ScanIsbnActivity.class);
        startActivityForResult(intent, SCAN_ISBN_TO_RETURN);
    }

    /**
     * Callback for when an ISBN is retrieved from ScanIsbnActivity. A different requestCode
     * is used depending on the status of the book, and this means that the retrieved ISBN
     * will be handled differently depending on the status of the book.
     *
     * @param requestCode the requestCode of the activity results
     * @param resultCode  the resultCode of the activity result
     * @param data        the intent of the activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        // set book status to AVAILABLE and delete the request
        if (requestCode == SCAN_ISBN_TO_RETURN) {
            String isbn = data.getStringExtra("ISBN");
            if (book.getIsbn().equals(isbn)) {
                RequestUtil.retrieveRequestsOnBookByStatus(
                    book,
                    Request.Status.BORROWED,
                    request -> {
                        book.setStatus(Book.Status.AVAILABLE);
                        StorageServiceProvider.getStorageService().deleteRequest(
                            request,
                            aVoid -> Log.d(TAG, "Request stored"),
                            e -> DialogUtil.showErrorDialog(this, e)
                        );
                        StorageServiceProvider.getStorageService().storeBook(
                            book,
                            aVoid -> Log.d(TAG, "Book stored"),
                            e -> DialogUtil.showErrorDialog(this, e)
                        );
                        // update book details and action button
                        setBookDetails();
                        fillBookDetails();
                        configureActionButton();
                    },
                    e -> DialogUtil.showErrorDialog(this, e));
            } else {
                Toast.makeText(this, "Scanned ISBN is not the same as this book's ISBN", Toast.LENGTH_SHORT).show();
            }
        }
        // set book and request status to BORROWED
        else if (requestCode == SCAN_ISBN_TO_BORROW) {
            String isbn = data.getStringExtra("ISBN");
            if (book.getIsbn().equals(isbn)) {
                RequestUtil.retrieveRequestsOnBookByStatus(
                    book, Request.Status.ACCEPTED,
                    request -> {
                        book.setStatus(Book.Status.BORROWED);
                        request.setStatus(Request.Status.BORROWED);
                        StorageServiceProvider.getStorageService().storeRequest(
                            request,
                            aVoid -> Log.d(TAG, "Request stored"),
                            e -> DialogUtil.showErrorDialog(this, e)
                        );
                        StorageServiceProvider.getStorageService().storeBook(
                            book,
                            aVoid -> Log.d(TAG, "Book stored"),
                            e -> DialogUtil.showErrorDialog(this, e)
                        );
                        // update book details and action button
                        setBookDetails();
                        fillBookDetails();
                        configureActionButton();
                    },
                    e -> DialogUtil.showErrorDialog(this, e)
                );
            } else {
                Toast.makeText(this, "Scanned ISBN is not the same as this book's ISBN", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
