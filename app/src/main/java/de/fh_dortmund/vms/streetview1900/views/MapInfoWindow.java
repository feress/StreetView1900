package de.fh_dortmund.vms.streetview1900.views;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import de.fh_dortmund.vms.streetview1900.R;

/**
 * Created by ress on 30.04.2016.
 */
public class MapInfoWindow implements GoogleMap.InfoWindowAdapter {

    private View mInfoWindow;

    public MapInfoWindow(Activity activity) {
        mInfoWindow = activity.getLayoutInflater().inflate(R.layout.map_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return mInfoWindow;
    }

    public void setInformation(String title, String description) {
        TextView titleView = (TextView) mInfoWindow.findViewById(R.id.info_window_title);
        TextView descriptionView = (TextView) mInfoWindow.findViewById(R.id.info_window_description);

        titleView.setText(title);
        descriptionView.setText(description);
    }

    public ImageView getImageView() {
        return (ImageView) mInfoWindow.findViewById(R.id.info_window_image);
    }

    public void resetInfoWindow() {
        ((TextView) mInfoWindow.findViewById(R.id.info_window_title)).setText("");
        ((TextView) mInfoWindow.findViewById(R.id.info_window_description)).setText("");

        ((ImageView) mInfoWindow.findViewById(R.id.info_window_image)).setImageResource(android.R.drawable.ic_menu_gallery);
    }
}
