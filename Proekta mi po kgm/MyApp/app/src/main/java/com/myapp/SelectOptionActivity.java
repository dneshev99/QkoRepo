package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class SelectOptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Choose option:");
        setSupportActionBar(toolbar);

        final String imageURl = getIntent().getExtras().getString("image");

        Button histogramButton = (Button) findViewById(R.id.histogramButton);
        Button negativeButton = (Button) findViewById(R.id.negativeButton);

        histogramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ImageHistogramActivity.class);
                intent.putExtra("image",imageURl);
                startActivity(intent);
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ImageNegativeActivity.class);
                intent.putExtra("image",imageURl);
                startActivity(intent);
            }
        });

    }

}
