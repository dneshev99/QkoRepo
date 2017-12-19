package com.elsys.refpro.refprowatch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by user on 17.12.2017 г..
 */

public interface SendService {
    @POST("/match/newEvent")
    Call<ResponseBody> send(@Body MatchEventDTO body);
}
