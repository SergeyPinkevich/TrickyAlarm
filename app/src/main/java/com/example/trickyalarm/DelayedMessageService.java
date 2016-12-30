package com.example.trickyalarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

import java.util.Calendar;

/**
 * Created by Сергей Пинкевич on 29.12.2016.
 */

public class DelayedMessageService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";
    public static final int NOTIFICATION_ID = 998;
    private Calendar mCalendar;
    private NotificationManager mNotificationManager;


    public DelayedMessageService(Calendar calendar) {
        super("Delayed message service");
        mCalendar = calendar;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            // getTime of notification in milliseconds
            int notificationPeriod = intent.getIntExtra(AddAlarmActivity.NOTIFICATION_TIME, 8);
            mCalendar.add(Calendar.HOUR_OF_DAY, -notificationPeriod);
            long notificationTime = mCalendar.getTimeInMillis();

            // Calculating time of launch the notification in long format
            long currentTime = System.currentTimeMillis();
            long time = notificationTime - currentTime;
            String text = intent.getStringExtra(EXTRA_MESSAGE);
            showNotification(text, time);
        }
    }

    private void showNotification(final String text, long time) {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .setWhen(time)
                .build();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void cancelNotification(int id) {
        mNotificationManager.cancel(id);
    }
}
