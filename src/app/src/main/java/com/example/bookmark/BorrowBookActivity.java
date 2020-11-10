package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Geolocation;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.FirebaseStorageService;
import com.example.bookmark.server.StorageService;
import com.example.bookmark.server.StorageServiceLocator;
import com.example.bookmark.util.DialogUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * This activity shows the borrower the meeting location to pick up the borrowed book and also
 * provides a button for a borrower to scan the borrowed book
 *
 * @author Nayan Prakash.
 */
public class BorrowBookActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String EXTRA_BOOK = "com.example.bookmark.BOOK";
    public static final String EXTRA_REQUEST = "com.example.bookmark.REQUEST";

    public static final int GET_ISBN = 1;

    private MapView mapView;
    private GoogleMap map;

    private Book book;
    private Request request;

    /**
     * This function creates the BorrowBook view and binds the onMapReady function to mapView
     *
     * @param savedInstanceState an instance state that has the state of the BorrowBookActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            book = (Book) bundle.getSerializable(EXTRA_BOOK);
            request = (Request) bundle.getSerializable(EXTRA_REQUEST);
        }

        mapView = (MapView) findViewById(R.id.borrowBookMap);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
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

        if (requestCode == BorrowBookActivity.GET_ISBN && resultCode == Activity.RESULT_OK) {
            String isbn = data.getStringExtra("ISBN");
            if (book.getIsbn().equals(isbn)) {
                book.setStatus(Book.Status.BORROWED);
                request.setStatus(Request.Status.BORROWED);
                storeToFirebase(book, request);
            } else {
                Toast.makeText(this, "Scanned ISBN is not the same as request ISBN", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void storeToFirebase(Book book, Request request) {
        OnSuccessListener<Void> onSuccessListener = aVoid -> {
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        };
        OnFailureListener onFailureListener = e -> DialogUtil.showErrorDialog(this, e);
        StorageService storageService = StorageServiceLocator.getInstance().getStorageService();
        storageService.storeRequest(request, aVoid ->
            storageService.storeBook(book, onSuccessListener, onFailureListener), onFailureListener);
    }

    /**
     * This is the function that handles when the mapView is ready. It places the marker at the
     * location of the Requests meeting location
     *
     * @param m the GoogleMap object of the mapView
     */
    @Override
    public void onMapReady(GoogleMap m) {
        map = m;

        Geolocation location = request.getLocation();
        LatLng meetingLocation = new LatLng(location.getLatitude(), location.getLongitude());
        map.addMarker(new MarkerOptions()
            .position(meetingLocation)
            .title("Meeting Location")
            .snippet("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude())
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(meetingLocation, 9));
    }

    /**
     * This is the function that handles the press of the "Scan ISBN" button and starts an activity
     * to get the ISBN from ScanIsbnActivity
     *
     * @param view This is the view of the "Scan ISBN" button
     */
    public void onScanISBNButtonPress(View view) {
        Intent intent = new Intent(this, ScanIsbnActivity.class);
        startActivityForResult(intent, BorrowBookActivity.GET_ISBN);
    }
}
