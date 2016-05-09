package de.fh_dortmund.vms.streetview1900.controls;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.fh_dortmund.vms.streetview1900.BuildConfig;
import de.fh_dortmund.vms.streetview1900.R;
import de.fh_dortmund.vms.streetview1900.activities.ReproducePhotoActivity;
import de.fh_dortmund.vms.streetview1900.api.StreetView1900Endpoint;
import de.fh_dortmund.vms.streetview1900.api.StreetView1900Service;
import de.fh_dortmund.vms.streetview1900.api.model.ImageInformation;
import de.fh_dortmund.vms.streetview1900.api.model.Location;
import de.fh_dortmund.vms.streetview1900.views.MapInfoWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ress on 30.04.2016.
 */
public class MarkerManager implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String LOG_TAG = MarkerManager.class.getName();

    private final MapInfoWindow mInfoWindowAdapter;
    private final Activity mParentActivity;
    private final GoogleMap mMap;
    private final StreetView1900Endpoint mRestEndpoint;

    private Location mCurrentLocation;

    public MarkerManager(Activity activity, GoogleMap map) {
        mParentActivity = activity;
        mMap = map;

        // Register listeners and set custom Info Window for markers
        mInfoWindowAdapter = new MapInfoWindow(activity);
        mMap.setInfoWindowAdapter(mInfoWindowAdapter);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        // Init REST service interface
        mRestEndpoint = StreetView1900Service.getInstance().getEndpoint();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        mInfoWindowAdapter.resetInfoWindow();

        // Load all information from REST service
        Call<Location> currentLocation = mRestEndpoint.getLocation(Integer.parseInt(marker.getTitle()));
        currentLocation.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if(response.isSuccessful()) {
                    mCurrentLocation = response.body();
                    Log.d(LOG_TAG, "Location loaded and set as current location: " + mCurrentLocation.toString());

                    // Show text information and refresh Info Window
                    mInfoWindowAdapter.setInformation(mCurrentLocation.getName(), mCurrentLocation.getDescription());
                    marker.showInfoWindow();

                    // Fetch images, if available
                    if (mCurrentLocation.getImageInformation() != null && mCurrentLocation.getImageInformation().size() > 0) {
                        fetchImage(mCurrentLocation.getImageInformation().get(0));
                    }
                }
            }

            private void fetchImage(final ImageInformation imageInformation) {
                Picasso.with(mParentActivity)
                        .load(BuildConfig.REST_SERVICE_URL + "images/" + imageInformation.getId())
                        .into(mInfoWindowAdapter.getImageView(), new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                // Dirty...
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        marker.showInfoWindow();
                                    }
                                }, 500);
                            }

                            @Override
                            public void onError() {
                                Log.w(LOG_TAG, "Can't fetch the image from " + imageInformation);
                            }
                        });
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Toast.makeText(mParentActivity, R.string.error_loading_information, Toast.LENGTH_SHORT);
            }
        });
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.i(LOG_TAG, "Load new activity for " + mCurrentLocation);
        Intent intent = new Intent(mParentActivity, ReproducePhotoActivity.class);
        intent.putExtra("location", mCurrentLocation);
        mParentActivity.startActivity(intent);

    }

    /**
     * Load locations from the webservice and display with markers on Google Map
     */
    public void showMarkers() {
        // REST magic
        Call<List<Location>> allLocations = mRestEndpoint.getLocations("Dortmund");
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
            LatLng position = new LatLng(l.getLatitude(), l.getLongitude());
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
