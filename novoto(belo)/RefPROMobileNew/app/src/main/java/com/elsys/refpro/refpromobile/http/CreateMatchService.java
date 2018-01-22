package com.elsys.refpro.refpromobile.http;

import com.elsys.refpro.refpromobile.http.dto.CreateMatchDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CreateMatchService {

    @POST("/MatchInfo/Create")
    Call<ResponseBody> create(@Body CreateMatchDto body);
}
