package com.elsys.refpro.refprowatch.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.elsys.refpro.refprowatch.R;
import com.elsys.refpro.refprowatch.login.Login;
import com.elsys.refpro.refprowatch.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("You have match to ref")
                        .setContentText("You received match information");

        Intent resultIntent = new Intent(this, Login.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, mBuilder.build());

        Map<String, String> matchId = remoteMessage.getData();

        SharedPreferences preferences;
        preferences = getSharedPreferences("MyPref" , 0);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString("matchId", String.valueOf(matchId.get(0)));


        Log.d("MEssage",remoteMessage.getMessageId());
        Log.d("Message data",remoteMessage.getData().toString());
    }
}
