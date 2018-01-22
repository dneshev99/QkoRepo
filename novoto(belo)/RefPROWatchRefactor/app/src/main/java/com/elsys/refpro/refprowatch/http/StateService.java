package com.elsys.refpro.refprowatch.http;

import com.elsys.refpro.refprowatch.http.dto.MatchStateDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StateService {

    @POST("/match/state")
    Call<ResponseBody> send(@Body MatchStateDTO body);
}
