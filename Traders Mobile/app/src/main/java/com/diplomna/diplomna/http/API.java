package com.diplomna.diplomna.http;


import com.diplomna.diplomna.DTOs.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {

    @POST("/login")
    Call<Void> login(@Body UserDTO userDTO);

    @GET("/")
    Call<Void> getCardView(@Body UserDTO userDTO);
}
