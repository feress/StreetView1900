package de.fh_dortmund.vms.streetview1900.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.gms.games.appcontent.AppContentActionEntity;
import com.squareup.picasso.Picasso;

import de.fh_dortmund.vms.streetview1900.BuildConfig;
import de.fh_dortmund.vms.streetview1900.R;
import de.fh_dortmund.vms.streetview1900.api.model.Location;
import de.fh_dortmund.vms.streetview1900.views.Camera2RawFragment;

public class ReproducePhotoActivity extends AppCompatActivity {

    private static final String LOG_TAG = ReproducePhotoActivity.class.getName();

    private FrameLayout cameraPreviewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproduce_photo);


        Location location = (Location) getIntent().getSerializableExtra("location");

        String locationName = location.getName();
        if(location != null && locationName.trim().length() > 0) {
            setTitle(locationName);
        }

        final ImageView historicImage = (ImageView) findViewById(R.id.historic_image);
        Picasso.with(this)
                .load(BuildConfig.REST_SERVICE_URL + "images/" + location.getImageInformation().get(0).getId())
                .into(historicImage);


        historicImage.setImageAlpha(127);

        SeekBar opacitySlider = (SeekBar) findViewById(R.id.opacity_slider);
        opacitySlider.setMax(255);
        opacitySlider.setProgress(127);
        opacitySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                historicImage.setImageAlpha(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final Camera2RawFragment camera2RawFragment = Camera2RawFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.camera_preview_holder, camera2RawFragment)
                .commit();

        historicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Klicked on image");
                camera2RawFragment.takePicture();
            }
        });
    }


}
