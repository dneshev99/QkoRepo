package com.elsys.refpro.refpromobile.http;

import com.elsys.refpro.refpromobile.http.dto.NotificationDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FirebaseService {

    @POST("/fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationDTO notificationDTO);
}
