package com.example.trickyalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Сергей Пинкевич on 05.01.2017.
 */

public class NotificationPublisher extends BroadcastReceiver {

    private Context mContext;
    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";

    public NotificationPublisher(Context context) {
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }

    public static int getNotificationId(String alarmId) {
        return (int)(Long.valueOf(alarmId) % Integer.MAX_VALUE);
    }

    public void cancelNotification(Context context, int notificationId, Alarm alarm) {
        // Cancel launch the notification
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.getID().hashCode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        // Clear notification in Status Bar, if it exists there
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    /**
     * Create text for notification
     * @param alarm
     */
    public void createNotification(Alarm alarm) {
        int notificationTimeInHours = alarm.getNotificationTime();

        // Create String from string.xml file
        String notificationText = mContext.getString(R.string.go_bed) + " " +
                String.valueOf(notificationTimeInHours) + " " + mContext.getString(R.string.hours);

        // Create and setup notification
        Notification noti = getNotification(notificationText);
        if (notificationTimeInHours > 0)
            scheduleNotification(noti, alarm, notificationTimeInHours);
    }

    /**
     *
     * @param notification notification, which was created
     * @param alarm Alarm which is added to alarm list
     * @param timeForNotification number of hours where notification should be shown. If 0 it isn't been shown
     */
    private void scheduleNotification(Notification notification, Alarm alarm, int timeForNotification) {
        Intent notificationIntent = new Intent(mContext, NotificationPublisher.class);
        int id = NotificationPublisher.getNotificationId(alarm.getID());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, alarm.getID().hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(alarm.getTime().getTimeInMillis() - (3600000 * timeForNotification));

        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    /**
     * Return the notification
     * @param content text which is shown for user
     * @return
     */
    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContentTitle("Tricky Alarm");
        builder.setContentText(content);
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        return builder.build();
    }
}
