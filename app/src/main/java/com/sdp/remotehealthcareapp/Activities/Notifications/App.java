package com.sdp.remotehealthcareapp.Activities.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class App extends Application {

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                "channel1",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("This is channel 1");

        NotificationChannel channel2 = new NotificationChannel(
                CHANNEL_2_ID,
                "channel2",
                NotificationManager.IMPORTANCE_LOW
        );
        channel2.setDescription("This is channel 1");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
        manager.createNotificationChannel(channel2);
    }
}
