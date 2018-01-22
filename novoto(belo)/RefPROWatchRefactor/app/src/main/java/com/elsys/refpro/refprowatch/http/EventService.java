package com.elsys.refpro.refprowatch.http;

import com.elsys.refpro.refprowatch.http.dto.MatchEventDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by user on 17.12.2017 г..
 */

public interface EventService {
    @POST("/match/newEvent")
    Call<ResponseBody> send(@Body MatchEventDTO body);
}
