package com.nicholaskoldys.collegetrackingapp.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.DateFormat;
import java.util.Calendar;

import static com.nicholaskoldys.collegetrackingapp.utility.AlarmReceiver.ALARM_TAG;

public class AlarmBuilder {

    private static final String TAG_CONSTANT = "Tag : ";

    public static void setAlarm(Context context, @Constants.CollegeItemType int itemType, Calendar alarmDateTime, String title, int uniqueId) {

        //if(!alarmDateTime.before(Calendar.getInstance())) {

            String content = createNotificationContent(itemType, title, alarmDateTime);
            String tag = createNotificationTag(uniqueId);

            alarmDateTime.set(Calendar.SECOND, 0);
            Long triggerAlarmLong = alarmDateTime.getTimeInMillis();
            Intent data = setParcelableData(context, title, content, tag, uniqueId);
            PendingIntent alarmIntent = setCallbackPendingIntent(context, uniqueId, data);
            scheduleAlarm(context, triggerAlarmLong, alarmIntent);
        //}
    }

    public static void cancelAlarm(Context context, @Constants.CollegeItemType int itemType, Calendar alarmDateTime, String title, int uniqueId) {

        String content = createNotificationContent(itemType, title, alarmDateTime);
        String tag = createNotificationTag(uniqueId);

        Data inputData = new Data.Builder()
                .putString(ALARM_TAG, tag)
                .putInt(AlarmReceiver.ALARM_UID, uniqueId)
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CancelNotificationWorker.class)
                .setInputData(inputData)
                .build();
        workManager.enqueue(workRequest);

        Intent nullIntent = setParcelableData(context, title, content, tag, uniqueId);

        PendingIntent cancelOutIntent = PendingIntent.getBroadcast(context, uniqueId, nullIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(cancelOutIntent);

        Toast.makeText(context, "Alarm Canceled", Toast.LENGTH_LONG).show();
    }

    private static Intent setParcelableData(Context context, String title, String content, String tag, int uniqueId) {
        Intent data = new Intent(context, AlarmReceiver.class);
        data.putExtra(AlarmReceiver.ALARM_TITLE, title);
        data.putExtra(AlarmReceiver.ALARM_CONTENT, content);
        data.putExtra(ALARM_TAG, tag);
        data.putExtra(AlarmReceiver.ALARM_UID, uniqueId);
        return data;
    }

    private static PendingIntent setCallbackPendingIntent(Context context, int uniqueId, Intent data) {
        return PendingIntent.getBroadcast(context, uniqueId, data, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static void scheduleAlarm(Context context, Long alarmDateTime, PendingIntent alarmIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDateTime, alarmIntent);
        Toast.makeText(context, "Alarm Scheduled", Toast.LENGTH_LONG).show();
    }

    private static String createNotificationContent(@Constants.CollegeItemType int itemType, String title, Calendar alarmDateTime) {
        StringBuilder contentSB = new StringBuilder();
        contentSB.append("\"");
        if(title != null) {
            contentSB.append(title);
        }
        contentSB.append("\"\n");
        if(itemType == Constants.CollegeItemType.GOAL_TYPE) {
            contentSB.append("You set this Goal at ");
        }
        if(itemType == Constants.CollegeItemType.ASSESSMENT_TYPE) {
            contentSB.append("Assessment is Due - ");
        }
        if(itemType == Constants.CollegeItemType.COURSE_TYPE) {
            contentSB.append("Course Alert ");
        }
        if(alarmDateTime != null) {
            contentSB.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(alarmDateTime.getTime()));
        }
        return contentSB.toString();
    }

    private static String createNotificationTag(int uniqueId) {
        StringBuilder tagSB = new StringBuilder();
        tagSB.append(TAG_CONSTANT);
        tagSB.append(uniqueId);
        return tagSB.toString();
    }
}
