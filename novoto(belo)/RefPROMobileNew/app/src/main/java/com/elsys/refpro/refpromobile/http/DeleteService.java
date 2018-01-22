package com.elsys.refpro.refpromobile.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;


public interface DeleteService {

    @DELETE("/MatchInfo/Delete/{id}")
    Call<ResponseBody> delete(@Path("id") String id);
}
