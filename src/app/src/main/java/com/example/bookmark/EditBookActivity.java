package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.ImageLoader;
import com.example.bookmark.abstracts.AddEditBookActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.EntityId;
import com.example.bookmark.models.Photograph;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;


/**
 * This activity allows a user to edit the details of a book, remove a photo
 * and delete a book. Navigate here from the BookDetailsActivity
 *
 * @author Mitch Adam.
 */
public class EditBookActivity extends AddEditBookActivity {
    private Button doneEditBookButton;
    private Button deleteBookButton;

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContextView called by AddEditBookActivity via getLayoutResourceId()
        getSupportActionBar().setTitle("Edit Book");

        doneEditBookButton = findViewById((R.id.edit_book_done_button));
        doneEditBookButton.setOnClickListener(v -> doneEditBook());

        deleteBookButton = findViewById((R.id.edit_book_delete_button));
        deleteBookButton.setOnClickListener(v -> deleteBook());

        Intent intent = getIntent(); // gets the previously created intent
        Bundle bundle = intent.getExtras();
        book = (Book) bundle.getSerializable("Book");
        populateFields();
    }

    /**
     * Pass the layout to the superclass.
     *
     * @return The layout resource ID
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_book;
    }

    /**
     * Populate the inputs to the current values of the book being edited.
     */
    private void populateFields() {
        // Set the values of all the EditTexts
        titleEditText.setText(book.getTitle());
        authorEditText.setText(book.getAuthor());
        isbnEditText.setText(book.getIsbn());
        descriptionEditText.setText(book.getDescription());
        populatePhotograph();
    }

    private void populatePhotograph() {
        if (book.getPhotograph() != null) {
            // if book has a photographId, fetch id
            EntityId photoId = book.getPhotograph();
            // fetch photograph from storage service
            StorageServiceProvider.getStorageService().retrievePhotograph(photoId, photograph -> {
                imageUri = photograph.getImageUri();
                bookImage.setImageURI(photograph.getImageUri());
            }, e -> {
                DialogUtil.showErrorDialog(this, e);
            });
            deleteBookImageButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void deleteImage() {
        // update book in db
        book.setPhotograph(null);
        StorageServiceProvider.getStorageService().storeBook(book,
            aVoid -> {}, e -> {});
        // delete photograph in db
        StorageServiceProvider.getStorageService().deletePhotograph(new Photograph(imageUri), aVoid -> {
            }, e -> {});
        imageUri = null;
        bookImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_photo_alternate_24));
        deleteBookImageButton.setVisibility(View.GONE);
    }

    /**
     * Book is finished being edited, update the book details.
     */
    private void doneEditBook() {

        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username, user -> {
            String title = titleEditText.getText().toString();
            String author = authorEditText.getText().toString();
            String isbn = isbnEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setDescription(description);
            if (imageUri != null) {
                Photograph bookPhoto = new Photograph(imageUri);
                book.setPhotograph(bookPhoto);
                StorageServiceProvider.getStorageService().storePhotograph(bookPhoto, aVoid -> {
                }, e -> DialogUtil.showErrorDialog(this, e));
            } else {
                book.setPhotograph(null);
            }
            StorageServiceProvider.getStorageService().storeBook(book, aVoid -> {
            }, e -> DialogUtil.showErrorDialog(this, e));

            // Return the edited book
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Book", book);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }, e -> DialogUtil.showErrorDialog(this, e));
    }

    /**
     * Delete the book currently being edited.
     */
    private void deleteBook() {
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username, user -> {

            StorageServiceProvider.getStorageService().deleteBook(book, aVoid -> {
            }, e -> DialogUtil.showErrorDialog(this, e));

            // Return the edited book
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Book", null);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }, e -> DialogUtil.showErrorDialog(this, e));
    }


    /**
     * Don't fetch details for edit book on isbn scan
     */
    protected void getBookDetails(String isbn) {
    }
}
