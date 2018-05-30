package com.diplomna.diplomna.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diplomna.diplomna.DTOs.AccountType;
import com.diplomna.diplomna.DTOs.UserDTO;
import com.diplomna.diplomna.R;
import com.diplomna.diplomna.application.DIApplication;
import com.diplomna.diplomna.http.API;
import com.diplomna.diplomna.http.HttpInfo;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {


    @Inject
    Retrofit retrofit;

    public Button loginButton, registerButton;
    private EditText txtPass, txtEmail;
    private final String TOKEN_PREFIX = "Bearer";
    @Inject
    SharedPreferences preferences;

    public String getPass() {
        return txtPass.getText().toString().trim();
    }

    public String getEmail() {
        return txtEmail.getText().toString().trim();
    }

    //get from database user type
    private String userType = "CUSTOMER";

    private boolean validateLogin(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(this, "Validation Passed", Toast.LENGTH_SHORT).show();
        return true;
    }


    public void init(){
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        txtPass = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.textEmail);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((DIApplication) this.getApplicationContext()).getApplicationComponent().inject(this);

        init();

        String userJwtToken = preferences.getString("token", "N/A");
        if(!"N/A".equals(userJwtToken)) {
            userJwtToken=userJwtToken.replace(TOKEN_PREFIX,"");
            int indexOfLastDot = userJwtToken.lastIndexOf('.');
            String withoutSignature = userJwtToken.substring(0, indexOfLastDot+1);
            Jwt<Header,Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
            Claims tokenBody = untrusted.getBody();
            String userName = tokenBody.getSubject();
            boolean isTokenValid = tokenBody.getExpiration().after(new Date(System.currentTimeMillis()));
            Log.d("token", isTokenValid + "");
            Log.d("token", userName);
            if(isTokenValid) {
                Intent app = new Intent(getApplicationContext(), MainActivityCustomer.class);
                app.putExtra("Username", userName);
                startActivity(app);
            }
        }
    }

    @OnClick(R.id.loginButton)
    public void submit(View view) {
        String passString = getPass();
        String emailString = getEmail();
        API service = retrofit.create(API.class);
        UserDTO user = new UserDTO();
        user.setPassword(passString);
        user.setUsername(emailString);
        service.login(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("login",response.headers().get(HttpInfo.jwtTokenHeaderName));
                    String jwtTokenResponse = response.headers().get(HttpInfo.jwtTokenHeaderName);
                    if(StringUtils.isNotBlank(jwtTokenResponse)){
                        preferences.edit().putString("token",jwtTokenResponse).apply();

                    }
                }else{
                    Log.d("login","not successful");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retro",t.getMessage());
            }
        });
        if(validateLogin(emailString, passString)) {
            if (userType.equals(AccountType.DEALER)) {
                Intent intent = new Intent(LoginActivity.this, MainActivityDealer.class);
                startActivity(intent);
            } else if (userType.equals(AccountType.CUSTOMER)) {
                Intent intent = new Intent(LoginActivity.this, MainActivityCustomer.class);
                startActivity(intent);
            }
        }
    }
}
