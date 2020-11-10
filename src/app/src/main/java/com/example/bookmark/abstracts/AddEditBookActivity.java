package com.example.bookmark.abstracts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.example.bookmark.BackButtonActivity;
import com.example.bookmark.R;
import com.example.bookmark.ScanIsbnActivity;
import com.example.bookmark.fragments.ImageSelectDialogFragment;

public abstract class AddEditBookActivity extends BackButtonActivity
        implements ImageSelectDialogFragment.ImageSelectListener {
    private static final int ISBN_REQUEST_CODE = 100;
    private static final String IMG_SELECT_TAG = "ImageSelectFragment";

    protected ImageButton scanISBNButton;
    protected ImageButton addPhotoButton;
    protected ImageButton deletePhotoButton;
    protected EditText titleEditText;
    protected EditText authorEditText;
    protected EditText isbnEditText;
    protected EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scanISBNButton = findViewById(R.id.book_scan_isbn_btn);
        scanISBNButton.setOnClickListener(v -> goToScanISBN());

        addPhotoButton = findViewById(R.id.book_add_photo_btn);
        addPhotoButton.setOnClickListener(v -> addPhoto());

        deletePhotoButton = findViewById(R.id.book_delete_photo_btn);
        deletePhotoButton.setOnClickListener(v -> deletePhoto());

        titleEditText = findViewById(R.id.book_title_field);
        authorEditText = findViewById(R.id.book_author_field);
        isbnEditText = findViewById(R.id.book_isbn_field);
        descriptionEditText = findViewById(R.id.book_description_field);
    }

    private void goToScanISBN() {
        Intent intent = new Intent(AddEditBookActivity.this, ScanIsbnActivity.class);
        startActivityForResult(intent, ISBN_REQUEST_CODE);
    }

    private void addPhoto() {
        ImageSelectDialogFragment.newInstance().show(getSupportFragmentManager(), IMG_SELECT_TAG);
    }

    public void onImageSelect(Uri uri) {
        // TODO: Save URI for when creating a book class
        addPhotoButton.setImageURI(uri);
        deletePhotoButton.setVisibility(View.VISIBLE);
    }

    private void deletePhoto() {
        // TODO: Sync up with Kyle on Firebase stuff
        // TODO: Properly clear the image instead of replacing it with original drawable
        // TODO: Clear saved URI
        addPhotoButton.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_add));
        deletePhotoButton.setVisibility(View.GONE);
    }

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
        }
    }
}
