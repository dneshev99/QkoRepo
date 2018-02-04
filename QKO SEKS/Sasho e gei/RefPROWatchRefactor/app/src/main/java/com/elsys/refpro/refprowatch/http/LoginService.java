package com.elsys.refpro.refprowatch.http;

import com.elsys.refpro.refprowatch.http.dto.UserDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/login")
    Call<ResponseBody> login(@Body UserDTO body);
}
