package de.fh_dortmund.vms.streetview1900.controls;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
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
import de.fh_dortmund.vms.streetview1900.views.MapInfoWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ress on 30.04.2016.
 */
public class MarkerManager implements GoogleMap.OnMarkerClickListener {

    private static final String LOG_TAG = MarkerManager.class.getName();

    private final MapInfoWindow mInfoWindowAdapter;
    private final Activity mParentActivity;
    private final GoogleMap mMap;
    private final StreetView1900Endpoint mRestEndpoint;

    public MarkerManager(Activity activity, GoogleMap map) {
        mParentActivity = activity;
        mMap = map;

        // Set custom Info Window for markers
        mInfoWindowAdapter = new MapInfoWindow(activity);
        mMap.setInfoWindowAdapter(mInfoWindowAdapter);

        // Init REST service interface
        mRestEndpoint = StreetView1900Service.getInstance().getEndpoint();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.i(LOG_TAG, marker.getTitle());
        Call<Location> currentLocation = mRestEndpoint.getLocation(Integer.parseInt(marker.getTitle()));
        currentLocation.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Location l = response.body();
                Log.i(LOG_TAG, "Hatta gefunden: " + l.toString());
                mInfoWindowAdapter.setInformation(l.getName(), l.getDescription());
                marker.showInfoWindow();
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {

            }
        });
        return false;
    }

    /**
     * Load locations from the webservice and display with markers on Google Map
     */
    public void showMarkers() {
        // REST magic
        Call<List<Location>> allLocations = mRestEndpoint.getLocations();
        allLocations.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful()) {
                    displayLocationsOnMap(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.e(LOG_TAG, "Unable to load locations from REST service", t);
            }
        });
    }

    /**
     * Adds a marker for each location in locations
     * @param locations
     */
    private void displayLocationsOnMap(List<Location> locations) {
        for (Location l : locations) {
            Log.i(LOG_TAG, l.toString());
            LatLng position = new LatLng(51.486296 + 0.1 * Math.random(), 7.412094 + 0.1 * Math.random());
            mMap.addMarker(new MarkerOptions()
                    .icon(getMarkerIcon())
                    .position(position)
                    .title("" + l.getId()));
        }
    }

    private BitmapDescriptor getMarkerIcon() {
        float[] hsv = new float[3];
        Color.colorToHSV(mParentActivity.getResources().getColor(R.color.colorPrimary, mParentActivity.getTheme()), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
