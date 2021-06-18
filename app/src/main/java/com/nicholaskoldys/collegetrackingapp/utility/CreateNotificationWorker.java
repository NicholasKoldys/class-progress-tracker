package com.nicholaskoldys.collegetrackingapp.utility;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.nicholaskoldys.collegetrackingapp.CollegeProgressTrackingApp;
import com.nicholaskoldys.collegetrackingapp.R;

public class CreateNotificationWorker extends Worker {

    public CreateNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        createNotification();
        return Result.success();
    }

    void createNotification() {

        Context context = getApplicationContext();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Data data = getInputData();
            String title = data.getString(AlarmReceiver.ALARM_TITLE);
            String content = data.getString(AlarmReceiver.ALARM_CONTENT);
            String tag = data.getString(AlarmReceiver.ALARM_TAG);
            int uId = data.getInt(AlarmReceiver.ALARM_UID, 0);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context, CollegeProgressTrackingApp.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_info_blk)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content));

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(tag, uId, builder.build());
        }
    }
}