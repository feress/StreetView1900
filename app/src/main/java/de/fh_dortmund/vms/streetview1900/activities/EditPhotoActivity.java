package de.fh_dortmund.vms.streetview1900.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import de.fh_dortmund.vms.streetview1900.R;

public class EditPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        File image = (File) getIntent().getSerializableExtra("image");
        ImageView imageView = (ImageView) findViewById(R.id.edit_image_view);
        Picasso.with(this).load(image).fit().centerCrop().into(imageView);
    }
}
