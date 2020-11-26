package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookmark.abstracts.AddEditBookActivity;
import com.example.bookmark.models.Book;
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
        // Don't support editing a title, author, isbn
        //TODO: May want to remove instead
        titleEditText.setText(book.getTitle());
        titleEditText.setFocusable(false);
        authorEditText.setText(book.getAuthor());
        authorEditText.setFocusable(false);
        isbnEditText.setText(book.getIsbn());
        isbnEditText.setFocusable(false);
        authorEditText.setFocusable(false);
        descriptionEditText.setText(book.getDescription());
    }

    /**
     * Book is finished being edited, update the book details.
     */
    private void doneEditBook() {
        // TODO: Call uriToPhotograph() only here, once the done button has been pressed (Uri to Bitmap is relatively expensive)

        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username, user -> {
            String description = descriptionEditText.getText().toString();
            book.setDescription(description);
            //book.setPhotograph(uriToPhotograph(bookImage));
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
        Log.d("Edit Book", "Click delete book");
    }
}
