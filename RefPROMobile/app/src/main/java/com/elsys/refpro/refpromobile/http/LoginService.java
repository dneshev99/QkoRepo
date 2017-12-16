package com.elsys.refpro.refpromobile.http;

import com.elsys.refpro.refpromobile.http.dto.AccountDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by user on 3.12.2017 г..
 */

public interface LoginService {

    @POST("/login")
    Call<ResponseBody> login(@Body AccountDto body);

}
