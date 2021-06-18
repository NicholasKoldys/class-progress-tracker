package com.nicholaskoldys.collegetrackingapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


/**
 * THIS CLASS HAS TO BE THE NAME OF THE APPLICATION IN THE MANIFEST TO WORK
 */
public class CollegeProgressTrackingApp extends Application {

    public static final String CHANNEL_ID = "ASSESSMENT_NOTIFICATION";
    private static Boolean CHANNEL_STATUS = false;
    public static final Boolean CHANNELS_SET = true;
    public static final Boolean CHANNELS_NOT_SET = false;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void setChannelStatus(Boolean channelStatus) {

        CHANNEL_STATUS = channelStatus;
    }

    public static Boolean getChannelStatus() {

        return CHANNEL_STATUS == CHANNELS_SET;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Assessment Notifications";
            String description = "Displays assessment information scheduled by the user under each course.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            setChannelStatus(CHANNELS_SET);
        } else {
            setChannelStatus(CHANNELS_NOT_SET);
        }
    }
}
