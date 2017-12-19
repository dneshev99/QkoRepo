package com.myapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.github.silvaren.easyrs.tools.Histogram;
import io.github.silvaren.easyrs.tools.base.Utils;


public class ImageHistogramActivity extends AppCompatActivity {

    private RenderScript rs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipulate_image);


        rs = RenderScript.create(getApplicationContext());

        ImageView pictureView = (ImageView) findViewById(R.id.picture_view);
        ImageView histogramView = (ImageView) findViewById(R.id.histogramView);

        String image = getIntent().getExtras().getString("image");

        Glide.with(this).load(image).asBitmap().centerCrop().into(pictureView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap pictureBitmap = BitmapFactory.decodeFile(image,options);

        Bitmap histogramBitmap = createHistogram(pictureBitmap);

        histogramView.setImageBitmap(histogramBitmap);

    }


    private Bitmap createHistogram(Bitmap pictureBitmap) {
        int[] histograms;
            histograms = Histogram.rgbaHistograms(rs, pictureBitmap);
        return Utils.drawHistograms(histograms,4);
    }


}
