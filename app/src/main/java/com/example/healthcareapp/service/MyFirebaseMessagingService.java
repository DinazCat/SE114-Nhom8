package com.example.healthcareapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.healthcareapp.MainActivity;
import com.example.healthcareapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    NotificationManager mNotificationManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        System.out.println("From: " + remoteMessage.getFrom());
        System.out.println("Noti: " + remoteMessage.getNotification());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
            if (remoteMessage.getNotification().getIcon() != null) {
                int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(resourceImage);
                } else {
                    builder.setSmallIcon(resourceImage);
                }

            } else {
                builder.setSmallIcon(R.drawable.baseline_notifications_active_24);
            }
            Intent resultIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

            builder.setContentTitle(remoteMessage.getNotification().getTitle());
            builder.setContentText(remoteMessage.getNotification ().getBody());
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
            builder.setAutoCancel(true);
            long[] pattern = {100,300,300,300};
            builder.setVibrate(pattern);
            builder.setPriority(Notification.PRIORITY_MAX);

            mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build. VERSION. SDK_INT >= Build. VERSION_CODES.O) {
                String channelId = "CHANNEL_ID";
                NotificationChannel channel = new NotificationChannel(channelId, "Channel title", NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }
            int notificationId = (int) System.currentTimeMillis();
            mNotificationManager.notify(notificationId, builder.build());
        }
    }
}
