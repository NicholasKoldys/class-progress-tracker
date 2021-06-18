package com.nicholaskoldys.collegetrackingapp.utility;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CancelNotificationWorker extends Worker {

    public CancelNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        cancelNotification();
        return ListenableWorker.Result.success();
    }

    void cancelNotification() {

        Context context = getApplicationContext();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Data data = getInputData();
            String tag = data.getString(AlarmReceiver.ALARM_TAG);
            int uId = data.getInt(AlarmReceiver.ALARM_UID, 0);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(tag, uId);
        }
    }
}
