package de.fh_dortmund.vms.streetview1900.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import de.fh_dortmund.vms.streetview1900.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class EditPhotoActivity extends AppCompatActivity {

    private static final String LOG_TAG = EditPhotoActivity.class.getName();

    private PhotoViewAttacher mAttacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        File image = (File) getIntent().getSerializableExtra("image");
        final ImageView imageView = (ImageView) findViewById(R.id.edit_image_view);
        mAttacher = new PhotoViewAttacher(imageView);

        Picasso.with(this).load(image).fit().centerCrop().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.i(LOG_TAG, "Image loaded.");
                mAttacher.update();
            }

            @Override
            public void onError() {
                Log.e(LOG_TAG, "Cannot load image.");
            }
        });
    }
}
