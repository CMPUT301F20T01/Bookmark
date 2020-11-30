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
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.bookmark.abstracts.AddEditBookActivity;
import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.EntityId;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.RequestUtil;

/**
 * This activity shows the details of a book. Depending on the
 * status of the book the user can then take some action. A user
 * can also navigate to the edit book activity from here.
 *
 * @author Mitch Adam.
 */
public class MyBookDetailsActivity extends BackButtonActivity implements MenuOptions {

    private static final String TAG = "My Book Details";

    private static final int EDIT_REQUEST_CODE = 101;
    private static final int GET_ISBN_TO_GIVE_BOOK = 102;
    private static final int GET_ISBN_TO_RECEIVE_BOOK = 103;

    private User user;
    private Book book;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
    private TextView statusTextView;

    private Button actionButton;

    //Set correct functionality based on book status
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
        book = (Book) intent.getSerializableExtra(ListingBooksActivity.EXTRA_BOOK);
        user = (User) intent.getSerializableExtra(ListingBooksActivity.USER);

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
        loadImage();


        // Set the user if borrowed or accepted
        if (book.getStatus().equals(Book.Status.BORROWED)
            || book.getStatus().equals(Book.Status.ACCEPTED)) {
            StorageServiceProvider.getStorageService().retrieveRequestsByBook(
                book,
                requestList -> {
                    for (Request r : requestList) {
                        if (r.getStatus().toString().equals(book.getStatus().toString())) {
                            String bookStatus = ("Status: "
                                + book.getStatus().toString().charAt(0)
                                + book.getStatus().toString().substring(1).toLowerCase()
                                + " by " + r.getRequesterId().toString());
                            statusTextView.setText(bookStatus);
                        }
                    }
                },
                e -> DialogUtil.showErrorDialog(this, e)
            );
        } else {
            // Otherwise just set status
            String bookStatus = ("Status: "
                + book.getStatus().toString().charAt(0)
                + book.getStatus().toString().substring(1).toLowerCase());
            statusTextView.setText(bookStatus);
        }
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

    private void loadImage() {
        if (book != null && book.getPhotograph() != null) {
            EntityId photoId = book.getPhotograph();
            StorageServiceProvider.getStorageService().retrievePhotograph(
                photoId,
                photograph -> {
                    if (photograph != null) {
                        imageView.setImageURI(photograph.getImageUri());
                    }
                }, e -> DialogUtil.showErrorDialog(this, e));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_book));
        }
    }

    /**
     * Handle managing request
     */
    private void manageRequests() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Book", book);
        bundle.putSerializable("User", user);
        Intent intent = new Intent(MyBookDetailsActivity.this, ManageRequestsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Handle giving a book
     */
    private void giveBook() {
        Intent intent = new Intent(this, ScanIsbnActivity.class);
        startActivityForResult(intent, GET_ISBN_TO_GIVE_BOOK);
    }

    /**
     * Handle receiving a book
     */
    private void receiveBook() {
        Intent intent = new Intent(this, ScanIsbnActivity.class);
        startActivityForResult(intent, GET_ISBN_TO_RECEIVE_BOOK);
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
     * Callback for when the Edit book activity returns or when a ISBN is retrieved from
     * ScanIsbnActivity.
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

        if (requestCode == GET_ISBN_TO_GIVE_BOOK || requestCode == GET_ISBN_TO_RECEIVE_BOOK) {
            String isbn = data.getStringExtra("ISBN");
            if (book.getIsbn().equals(isbn)) {
                if (requestCode == GET_ISBN_TO_GIVE_BOOK) {
                    RequestUtil.retrieveRequestsOnBookByStatus(
                        book,
                        Request.Status.ACCEPTED,
                        request -> {
                            book.setStatus(Book.Status.BORROWED);
                            request.setStatus(Request.Status.BORROWED);
                            StorageServiceProvider.getStorageService().storeRequest(
                                request,
                                aVoid -> Log.d(TAG, "Request marked BORROWED"),
                                e -> DialogUtil.showErrorDialog(this, e)
                            );
                            StorageServiceProvider.getStorageService().storeBook(
                                book,
                                aVoid -> Log.d(TAG, "Book marked BORROWED"),
                                e -> DialogUtil.showErrorDialog(this, e)
                            );
                        },
                        e -> DialogUtil.showErrorDialog(this, e));
                } else if (requestCode == GET_ISBN_TO_RECEIVE_BOOK) {
                    RequestUtil.retrieveRequestsOnBookByStatus(
                        book,
                        Request.Status.BORROWED,
                        request -> {
                            book.setStatus(Book.Status.AVAILABLE);
                            StorageServiceProvider.getStorageService().deleteRequest(
                                request,
                                aVoid -> Log.d(TAG, "Request deleted"),
                                e -> DialogUtil.showErrorDialog(this, e)
                            );
                            StorageServiceProvider.getStorageService().storeBook(
                                book,
                                aVoid -> Log.d(TAG, "Book marked AVAILABLE"),
                                e -> DialogUtil.showErrorDialog(this, e)
                            );
                        },
                        e -> DialogUtil.showErrorDialog(this, e));
                }
            } else {
                Toast.makeText(this, "Scanned ISBN is not the same as this book's ISBN", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == EDIT_REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            book = (Book) bundle.getSerializable("Book");

            // Book is deleted
            if (book == null) {
                Intent intent = new Intent(MyBookDetailsActivity.this,
                    MyBooksActivity.class);
                startActivity(intent);
            } else {
                fillBookDetails();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImage();
        if (book != null) {
            StorageServiceProvider.getStorageService().retrieveBook(
                book.getId(),
                b -> {
                    book = b;
                    fillBookDetails();
                    configureActionButton();
                },
                e -> DialogUtil.showErrorDialog(this, e)
            );
        }
    }

}
