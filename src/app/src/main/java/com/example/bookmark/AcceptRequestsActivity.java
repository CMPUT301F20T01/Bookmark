package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bookmark.models.Geolocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;

/**
 * TODO: Description of class.
 * @author Nayan Prakash.
 */
public class AcceptRequestsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 100;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//    public static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 99;
//    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;

    private MapView mapView;
    private GoogleMap map;
    private Marker marker;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_requests);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(null);
        mapView.getMapAsync(this);

    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap m) {
        map = m;

        if (allPermissionsGranted()) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location l) {
                    location = l;
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
                @Override
                public void onProviderEnabled(String provider) {
                }
                @Override
                public void onProviderDisabled(String provider) {
                }
            });

            try {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                Log.d("LOCATION", e.toString());
                location = null;
            }

            Double latitude;
            Double longitude;
            if (location == null) {
                latitude = 53.5461;
                longitude = -113.4938;
            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            LatLng markerLocation = new LatLng(latitude, longitude);
            marker = map.addMarker(new MarkerOptions()
                .position(markerLocation)
                .title("Meeting Location")
                .snippet("Latitude: " + latitude + " Longitude: " + longitude)
            );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 11));
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    marker.setPosition(latLng);
                    marker.setSnippet("Latitude: " + latLng.latitude + " Longitude: " + latLng.longitude);
                }
            });
        } else {
            Log.d("LOCATION", "Should request permissions");
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, MY_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void on_done_press(View view) {
        /* This returns the set Geolocation to the ManageRequestsActivity which can handle setting
            the request object and passing to firebase
        */
        LatLng position = marker.getPosition();
        Geolocation meetingLocation = new Geolocation((float) position.latitude, (float) position.longitude);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Geolocation", meetingLocation);
        intent.putExtras(bundle);
        setResult(ManageRequestsActivity.RESULT_OK, intent);
        finish();
    }
}
