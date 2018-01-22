package com.elsys.refpro.refpromobile.http;

import com.elsys.refpro.refpromobile.http.dto.UpdateMatchDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UpdateMatchService {

    @POST("/MatchInfo/Update")
    Call<ResponseBody> update(@Body UpdateMatchDto body);
}
