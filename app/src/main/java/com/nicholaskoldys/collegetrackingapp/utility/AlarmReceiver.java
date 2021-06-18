package com.nicholaskoldys.collegetrackingapp.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ALARM_TITLE = "title";
    public static final String ALARM_CONTENT = "content";
    public static final String ALARM_TAG = "tag";
    public static final String ALARM_UID = "alarmUniqueId";

    @Override
    public void onReceive(Context context, Intent intent) {

        Data data = new Data.Builder()
                .putString(ALARM_TITLE, intent.getStringExtra(ALARM_TITLE))
                .putString(ALARM_CONTENT, intent.getStringExtra(ALARM_CONTENT))
                .putString(ALARM_TAG, intent.getStringExtra(ALARM_TAG))
                .putInt(ALARM_UID, intent.getIntExtra(ALARM_UID, 0))
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CreateNotificationWorker.class)
                .setInputData(data)
                .build();

        workManager.enqueue(workRequest);
    }
}