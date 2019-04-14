package com.capstone.safebeacon;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String Channel_1_ID = "Channel 1";
    public static final String Channel_2_ID = "Channel 2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }
    public void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            NotificationChannel channel1 = new NotificationChannel(
                    Channel_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    Channel_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
