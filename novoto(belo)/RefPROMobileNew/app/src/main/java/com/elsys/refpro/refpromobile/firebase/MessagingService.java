package com.elsys.refpro.refpromobile.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Message id",remoteMessage.getMessageId());
        Log.d("Message data",remoteMessage.getData().toString());
    }
}
