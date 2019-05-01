package com.capstone.safebeacon;

import android.app.Application;
import android.app.NotificationManager;

public class NotificationChannel extends Application {
    public static final String Channel_1_ID = "Channel1";
    public static final String Channel_2_ID = "Channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }
    private void createNotificationChannels() {
        android.app.NotificationChannel channel1 = new android.app.NotificationChannel(
                Channel_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("This is channel 1");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }
}
