package com.elsys.refpro.refpromobile.http.dto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Mitko on 16.12.2017 г..
 */

public interface InfoService {

    @POST("/MatchInfo/Update")
    Call<ResponseBody> update(@Body UpdateDto body);
}
