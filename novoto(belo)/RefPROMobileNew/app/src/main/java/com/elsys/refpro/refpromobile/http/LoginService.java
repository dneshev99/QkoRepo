package com.elsys.refpro.refpromobile.http;

import com.elsys.refpro.refpromobile.http.dto.AccountDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/login")
    Call<ResponseBody> login(@Body AccountDto body);
}
