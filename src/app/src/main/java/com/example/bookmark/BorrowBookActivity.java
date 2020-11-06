package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.example.bookmark.models.Geolocation;
import com.example.bookmark.models.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * TODO: Description of class.
 * @author Nayan Prakash.
 */
public class BorrowBookActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int GET_ISBN = 1;

    private MapView mapView;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        mapView = (MapView) findViewById(R.id.borrowBookMap);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BorrowBookActivity.GET_ISBN) {
            if (resultCode == ScanIsbnActivity.ISBN_RESULT_OK) {
                String ISBN = data.getStringExtra("ISBN");
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap m) {
        map = m;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Request request = (Request) bundle.getSerializable("Request");
        Geolocation location = request.getLocation();
        LatLng meetingLocation = new LatLng(location.getLatitude(), location.getLongitude());
        map.addMarker(new MarkerOptions()
            .position(meetingLocation)
            .title("Meeting Location")
            .snippet("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude())
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(meetingLocation, 9));
    }

    public void onScanISBNButtonPress(View view) {
        Intent intent = new Intent(this, ScanIsbnActivity.class);
        startActivityForResult(intent, BorrowBookActivity.GET_ISBN);
    }
}
