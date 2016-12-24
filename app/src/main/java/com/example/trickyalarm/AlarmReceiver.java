package com.example.trickyalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Almaz on 21.12.2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * Function that do some action after alarm is on.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        //PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        //wakeLock.acquire();
        Intent intent1 = new Intent(context, SignalActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
        //wakeLock.release();
    }

    /**
     * Function that set a new alarm.
     * @param context
     * @param alarm alarm that we should to create.
     */
    public void setAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTime().getTimeInMillis(), pendingIntent);
    }

    /**
     * Function that cancel alarm.
     * @param context
     */
    public void cancelAlarm(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    /**
     * Function that set repeated alarm.
     * @param context
     * @param time in which alarm should work.
     * @param intervalOfRepeating interval of repeating.
     */
    public void  setRepeatedAlarm(Context context, int time, int intervalOfRepeating){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, intervalOfRepeating, pendingIntent);
    }
}
