package com.example.bookmark.abstracts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.bookmark.BackButtonActivity;
import com.example.bookmark.R;
import com.example.bookmark.ScanIsbnActivity;
import com.example.bookmark.fragments.ImageSelectDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

/**
 * @author Eric Claerhout
 */
public abstract class AddEditBookActivity extends BackButtonActivity
    implements ImageSelectDialogFragment.ImageSelectListener {
    private static final int ISBN_REQUEST_CODE = 100;
    private static final String IMG_SELECT_TAG = "ImageSelectFragment";

    protected ImageView bookImage;
    protected ImageView deleteBookImageButton;
    protected EditText titleEditText;
    protected EditText authorEditText;
    protected EditText isbnEditText;
    protected EditText descriptionEditText;

    protected TextInputLayout titleTextInputLayout;
    protected TextInputLayout authorTextInputLayout;
    protected TextInputLayout isbnTextInputLayout;

    protected Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        isbnTextInputLayout = findViewById(R.id.add_edit_book_isbn);
        isbnTextInputLayout.setEndIconOnClickListener(v -> goToScanISBN());

        bookImage = findViewById(R.id.book_image);
        bookImage.setOnClickListener(v -> addImage());

        deleteBookImageButton = findViewById(R.id.add_edit_book_delete_image_button);
        deleteBookImageButton.setOnClickListener(v -> deleteImage());

        titleEditText = ((TextInputLayout) findViewById(R.id.add_edit_book_title)).getEditText();
        authorEditText = ((TextInputLayout) findViewById(R.id.add_edit_book_author)).getEditText();
        isbnEditText = isbnTextInputLayout.getEditText();
        descriptionEditText = ((TextInputLayout) findViewById(R.id.add_edit_book_description)).getEditText();

        titleTextInputLayout = findViewById(R.id.add_edit_book_title);
        authorTextInputLayout = findViewById(R.id.add_edit_book_author);
    }

    /**
     * Helper function called in onCreate() to retrieve the layout. This should be overridden by
     * subclasses to return the desired layout so it can be set in this abstract class layer. The
     * subclass should not call setContentView() itself.
     *
     * @return The layout resource ID
     */
    protected abstract int getLayoutResourceId();

    /**
     * Gets called if an isbn number is scanned in.
     *
     * @param isbn
     * @return
     */
    protected abstract void getBookDetails(String isbn);

    /**
     * Launch the activity to scan ISBN codes.
     */
    private void goToScanISBN() {
        Intent intent = new Intent(AddEditBookActivity.this, ScanIsbnActivity.class);
        startActivityForResult(intent, ISBN_REQUEST_CODE);
    }

    /**
     * Launch the fragment to select a book image.
     */
    private void addImage() {
        ImageSelectDialogFragment.newInstance().show(getSupportFragmentManager(), IMG_SELECT_TAG);
    }

    /**
     * Callback for when an image is selected.
     */
    public void onImageSelect(Uri uri) {
        // TODO: Save URI for when creating a book class
        imageUri = uri;
        bookImage.setImageURI(uri);
        deleteBookImageButton.setVisibility(View.VISIBLE);
    }

    /**
     * Delete the added book image.
     */
    private void deleteImage() {
        // TODO: Sync up with Kyle on Firebase stuff
        // TODO: Clear saved URI
        imageUri = null;
        bookImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_photo_alternate_24));
        deleteBookImageButton.setVisibility(View.GONE);
    }


    /**
     * Callback for when the scan ISBN activity returns.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        // Get ISBN
        if (requestCode == ISBN_REQUEST_CODE) {
            String isbn = data.getStringExtra("ISBN");
            isbnEditText.setText(isbn);
            getBookDetails(isbn);
        }
    }
}
