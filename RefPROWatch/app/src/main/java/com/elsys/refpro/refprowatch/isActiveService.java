package com.elsys.refpro.refprowatch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Mitko on 17.12.2017 г..
 */

public interface isActiveService {

    @POST("/match/isActive")
    Call<ResponseBody> send(@Body MatchStateDTO body);
}
