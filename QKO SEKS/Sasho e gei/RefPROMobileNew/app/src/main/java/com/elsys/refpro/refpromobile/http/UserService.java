package com.elsys.refpro.refpromobile.http;

import com.elsys.refpro.refpromobile.http.dto.UserDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("/login")
    Call<ResponseBody> login(@Body UserDTO body);

    @POST("/user/token")
    Call<Void> addFcmTokenForUser(@Body String token);
}
