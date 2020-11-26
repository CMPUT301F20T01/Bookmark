package com.example.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.bookmark.abstracts.AddEditBookActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.Photograph;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;

/**
 * This activity allows a user to add a new book. It provides fields
 * to enter all book details, scan isbn and add a photo.
 *
 * @author Mitch Adam.
 */
public class AddBookActivity extends AddEditBookActivity {
    private Button doneAddBookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContextView called by AddEditBookActivity via getLayoutResourceId()
        getSupportActionBar().setTitle("Add Book");

        doneAddBookButton = findViewById((R.id.add_book_done_button));
        doneAddBookButton.setOnClickListener(v -> doneAddBook());
    }

    /**
     * Pass the layout to the superclass.
     *
     * @return The layout resource ID
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_book;
    }

    /**
     * Create and add the book to the database.
     */
    private void doneAddBook() {
        String title = titleEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String isbn = isbnEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Check input
        Boolean isValid = true;
        if (title.isEmpty()) {
            titleTextInputLayout.setError("Title should not be blank");
            isValid = false;
        } else {
            titleTextInputLayout.setError(null);
        }

        if (author.isEmpty()) {
            authorTextInputLayout.setError("Author should not be blank");
            isValid = false;
        } else {
            authorTextInputLayout.setError(null);
        }

        if (isbn.isEmpty()) {
            isbnTextInputLayout.setError("ISBN should not be blank");
            isValid = false;
        } else {
            isbnTextInputLayout.setError(null);
        }

        if (!isValid) {
            return;
        }
        // TODO: Call uriToPhotograph() only here, once the done button has been pressed (Uri to Bitmap is relatively expensive)
        String username = UserUtil.getLoggedInUser(this);
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username, user -> {
            Book book = new Book(user, title, author, isbn);
            book.setDescription(description);
            book.setPhotograph(new Photograph(imageUri));
            StorageServiceProvider.getStorageService().storeBook(book, aVoid -> {
            }, e -> DialogUtil.showErrorDialog(this, e));
            finish();
        }, e -> DialogUtil.showErrorDialog(this, e));
    }
}
