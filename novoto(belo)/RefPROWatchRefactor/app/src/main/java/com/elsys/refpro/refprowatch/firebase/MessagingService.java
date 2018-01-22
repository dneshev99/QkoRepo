package com.elsys.refpro.refprowatch.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MEssage",remoteMessage.getMessageId());
        Log.d("Message data",remoteMessage.getData().toString());
    }
}
