package com.myapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.RenderScript;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.github.silvaren.easyrs.tools.Lut;
import io.github.silvaren.easyrs.tools.params.SampleParams;

public class ImageNegativeActivity extends AppCompatActivity {

    private RenderScript rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_negative);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Image negative");
        setSupportActionBar(toolbar);

        ImageView negativeView = (ImageView) findViewById(R.id.negativeView);

        rs = RenderScript.create(getApplicationContext());

        String image = getIntent().getExtras().getString("image");


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap pictureBitmap = BitmapFactory.decodeFile(image,options);

        Bitmap negativeBitmap = Lut.applyLut(rs, pictureBitmap, SampleParams.Lut.negative());

        negativeView.setImageBitmap(negativeBitmap);
    }

}
