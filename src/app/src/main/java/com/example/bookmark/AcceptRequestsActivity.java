package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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

/**
 * An activity that allows users to select a meeting location upon Request acceptance
 *
 * @author Nayan Prakash.
 */
public class AcceptRequestsActivity extends BackButtonActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 100;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private MapView mapView;
    private GoogleMap map;
    private Marker marker;
    private Location location;

    /**
     * This function creates the AcceptRequests view and binds the onMapReady function to mapView
     * @param savedInstanceState an instance state that has the state of the AcceptRequestsActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_requests);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(null);
        mapView.getMapAsync(this);

    }

    /**
     * This function checks if all permissions in REQUIRED_PERMISSIONS array are granted
     * @return true if all permissions are granted, false otherwise
     */
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * This is the function that handles when the mapView is ready. It places the marker at the
     * user's last know location and sets the OnMapClickListener
     * @param m the GoogleMap object of the mapView
     */
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
        } else {
            Log.d("LOCATION", "Should request permissions");
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, MY_PERMISSIONS_REQUEST_CODE);
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
    }

    /**
     * This is the function that handles mapView resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * This is the function that handles mapView onStart
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * This is the function that handles mapView stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * This is the function that handles mapView pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * This is the function that handles mapView destroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * This is the function that handles mapView low memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * This is the function that handles the click of the done button which grabs the marker's
     * location and returns it to the previous activity (ManageRequestsActivity)
     * @param view This is the view of the done button
     */
    public void onDonePress(View view) {
        /* This returns the set Geolocation to the ManageRequestsActivity which can handle setting
            the request object and passing to firebase
        */
        LatLng position = marker.getPosition();
        Geolocation meetingLocation = new Geolocation((float) position.latitude, (float) position.longitude);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Geolocation", meetingLocation);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
