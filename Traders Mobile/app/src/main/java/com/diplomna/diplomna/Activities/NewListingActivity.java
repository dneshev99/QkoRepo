package com.diplomna.diplomna.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.diplomna.diplomna.R;
import com.diplomna.diplomna.http.API;


import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewListingActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    @Inject
    SharedPreferences sharedPreferences;

    public Button createButton;
    private EditText txtItemName, txtUnit, txtPrice, txtDesc, txtQuant;

    public String getItemName(){
        return txtItemName.getText().toString().trim();
    }

    public String getUnit(){
        return txtUnit.getText().toString().trim();
    }

    public String getPrice(){
        return txtPrice.getText().toString().trim();
    }

    public String getDesc(){
        return txtDesc.getText().toString().trim();
    }

    public String getQuant(){
        return txtQuant.getText().toString().trim();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);
    }

    @OnClick(R.id.btnCreate)
    public void submit(View view){

        API service = retrofit.create(API.class);

        service.

    }

    public void init(){
        createButton = findViewById(R.id.btnCreate);
        txtItemName = findViewById(R.id.txtNameOfItem);
        txtUnit = findViewById(R.id.txtUnit);
        txtPrice = findViewById(R.id.txtPrice);
        txtDesc = findViewById(R.id.txtDescription);
        txtQuant = findViewById(R.id.txtQuantity);
    }








}
