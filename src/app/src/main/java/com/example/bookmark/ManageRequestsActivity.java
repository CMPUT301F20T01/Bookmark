package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookmark.adapters.RequestList;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.Geolocation;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description of class.
 *
 * @author Nayan Prakash.
 */
public class ManageRequestsActivity extends BackButtonActivity {

    public static String TAG = "Manage Requests Activity";

    public static int GET_MEETING_LOCATION = 1;

    private Book book;
    private User owner;
    private ListView requestList;
    private ArrayAdapter<Request> requestAdapter;
    private ArrayList<Request> requestDataList;

    /**
     * This function creates the ManageRequests view and retrieves the Owner from Firebase
     * This function also sets the Book Title and sets the requestList adapter
     * @param savedInstanceState an instance state that has the state of the ManageRequestsActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String bookTitle = "Book Title";

        if (bundle != null) {
            book = (Book) bundle.getSerializable("Book");
            owner = (User) bundle.getSerializable("User");
            setRequestData();
            bookTitle = book.getTitle();
        }

        TextView bookName = findViewById(R.id.book_name);
        bookName.setText(bookTitle);

        requestList = findViewById(R.id.request_list);
        requestDataList = new ArrayList<>();
        requestAdapter = new RequestList(this, requestDataList);
        requestList.setAdapter(requestAdapter);
    }

    /**
     * This function handles activity results from new activities. Specifically, this stores
     * accepted Requests after retrieving meeting locations from AcceptRequestsActivity
     * @param requestCode the requestCode of the activity result
     * @param resultCode the resultCode of the activity result
     * @param data the intent of the activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ManageRequestsActivity.GET_MEETING_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Geolocation geolocation = (Geolocation) bundle.getSerializable("Geolocation");
                    Request request = (Request) bundle.getSerializable("Request");
                    request.setLocation(geolocation);
                    request.setStatus(Request.Status.ACCEPTED);
                    StorageServiceProvider.getStorageService().retrieveRequestsByBook(
                        book,
                        requestList -> {
                            for (Request r: requestList) {
                                if (!r.getId().toString().equals(request.getId().toString())) {
                                    StorageServiceProvider.getStorageService().deleteRequest(
                                        r,
                                        aVoid -> Log.d(TAG, "Request " + r.getId().toString() + " deleted"),
                                        e -> DialogUtil.showErrorDialog(this, e)
                                    );
                                }
                            }
                        },
                        e -> DialogUtil.showErrorDialog(this, e)
                    );
                    StorageServiceProvider.getStorageService().storeRequest(
                        request,
                        aVoid -> Log.d(TAG, "Request marked ACCEPTED"),
                        e -> DialogUtil.showErrorDialog(this, e)
                    );
                    book.setStatus(Book.Status.ACCEPTED);
                    StorageServiceProvider.getStorageService().storeBook(
                        book,
                        aVoid -> Log.d(TAG, "Book marked ACCEPTED"),
                        e -> DialogUtil.showErrorDialog(this, e)
                    );
                    finish();
                }
            }
        }
    }

    /**
     * This functions sets the request listener for the book's list of requests from Firebase
     */
    private void setRequestData() {
        OnSuccessListener<List<Request>> onSuccessListener = new OnSuccessListener<List<Request>>() {
            @Override
            public void onSuccess(List<Request> requestList) {
                if (requestList != null) {
                    requestDataList.clear();
                    for (Request r: requestList) {
                        requestDataList.add(r);
                    }
                    requestAdapter.notifyDataSetChanged();
                }
            }
        };
        StorageServiceProvider.getStorageService().retrieveRequestsByBook(book, onSuccessListener, e -> DialogUtil.showErrorDialog(this, e));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestData();
    }
}
