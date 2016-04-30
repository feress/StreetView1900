package de.fh_dortmund.vms.streetview1900.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import de.fh_dortmund.vms.streetview1900.R;
import de.fh_dortmund.vms.streetview1900.api.StreetView1900Endpoint;
import de.fh_dortmund.vms.streetview1900.api.StreetView1900Service;
import de.fh_dortmund.vms.streetview1900.api.model.Location;
import de.fh_dortmund.vms.streetview1900.controls.MarkerManager;
import de.fh_dortmund.vms.streetview1900.views.MapInfoWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private static final int STREET_VIEW_1900_PERMISSION_ACCESS_LOCATION = 1;

    private GoogleMap mMap;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get location service and request current location
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        requestLocation();
    }

    private void requestLocation() {
        // Check for ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not granted, request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    STREET_VIEW_1900_PERMISSION_ACCESS_LOCATION);
        } else {
            // If granted, request location
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STREET_VIEW_1900_PERMISSION_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                } else {
                    Toast.makeText(this, R.string.error_location_permission_refused, Toast.LENGTH_LONG);
                }
                return;
            }
        }
    }

    /**
     * This callback is triggered when the map is ready to be used.
     * This is where we can call the StreetView1900 REST Service and add markers for all existing
     * photos (MarkerManager does the magic for us).
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "Can't show current location. Permission denied.", e);
        }

        MarkerManager markerManager = new MarkerManager(this, mMap);
        mMap.setOnMarkerClickListener(markerManager);
        markerManager.showMarkers();
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        if(mMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
            mMap.animateCamera(cameraUpdate);
            try {
                mLocationManager.removeUpdates(this);
            }catch (SecurityException e) {
                Log.e(LOG_TAG, "Can't remove updates. Permission denied.", e);
            }
        }
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
}
