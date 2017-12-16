package com.elsys.refpro.refpromobile.http;

import com.elsys.refpro.refpromobile.http.dto.MatchDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface CreateService {
    @POST("/MatchInfo/Create")
    Call<ResponseBody> create(@Body MatchDto body);
}
