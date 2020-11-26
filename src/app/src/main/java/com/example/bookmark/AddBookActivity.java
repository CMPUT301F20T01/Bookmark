package com.example.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmark.abstracts.AddEditBookActivity;
import com.example.bookmark.models.Book;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
//            book.setPhotograph(new Photograph());
            StorageServiceProvider.getStorageService().storeBook(book, aVoid -> {
            }, e -> DialogUtil.showErrorDialog(this, e));
            finish();
        }, e -> DialogUtil.showErrorDialog(this, e));
    }

    /**
     * Gets book details from Google API
     *
     * @param isbn - the ISBN of the book to retrieve detials of
     */
    @Override
    protected void getBookDetails(String isbn) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = String.format(
            "https://www.googleapis.com/books/v1/volumes?q=%s&key=AIzaSyBWHRvbDfUIhuEE6Pju59jxarsJXPXSK48",
            isbn
        );

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject volumeInfo =
                            jsonObject
                                .getJSONArray("items")
                                .getJSONObject(0)
                                .getJSONObject("volumeInfo");

                        if (volumeInfo.has("title")) {
                            titleEditText.setText(volumeInfo.getString("title"));
                        }

                        if (volumeInfo.has("authors")) {
                            authorEditText.setText(
                                volumeInfo.getJSONArray("authors").getString(0));
                        }

                        if (volumeInfo.has("description")) {
                            descriptionEditText.setText(volumeInfo.getString("description"));
                        }

                    } catch (JSONException | IndexOutOfBoundsException err) {
                        Log.d("GOOGLE API", "Error converting response to JSON" + err.toString());
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("GOOGLE API", "NO GOOD" + error);

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
