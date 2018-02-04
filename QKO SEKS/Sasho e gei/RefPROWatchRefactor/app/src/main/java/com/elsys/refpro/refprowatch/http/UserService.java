package com.elsys.refpro.refprowatch.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/user/token")
    Call<Void> addFcmTokenForUser(@Body String token);
}
